<template>
	<Layout>
		<Content>
			<Breadcrumb>
				<!-- <BreadcrumbItem>申请管理</BreadcrumbItem> -->
				<BreadcrumbItem>新增主机信息</BreadcrumbItem>
			</Breadcrumb>
			<Card>
				<Row>
					<Col span="12"> 主机信息：
					</Col>
					<Col span="12">
					<Button class="btnCreate" type="primary" @click="createEngine">新增</Button>
					</Col>
				</Row>
				<Table :columns="columns" :data="data" :border='true' class="access-list"></Table>
				<div class="pageFooter">
					<Button type="default" @click="goback">取消</Button>
					<Button type="primary" @click="hasTableData">保存</Button>
					<Button type="primary" @click="hostSubmit">提交</Button>
				</div>
			</Card>
		</Content>
		<Modal v-model="delectState" title="删除确认" width="300" :mask-closable="false">
			<div>您确定删除吗？</div>
			<div slot="footer">
				<Button type="primary" @click="deleteMachine">确定</Button>
				<Button type="text" @click="delectClose">关闭</Button>
			</div>
		</Modal>
		<Modal v-model="editState" title="编辑主机" width="70%" :mask-closable="false">
			<engine-edit ref="engineEdit" v-on:examine="saveHost"></engine-edit>
			<div slot="footer">
				<Button type="primary" @click="editSubmit('engineEdit')">确定</Button>
				<Button type="text" @click="editClose">关闭</Button>
			</div>
		</Modal>
	</Layout>
</template>

<script>
	import engineEdit from './engineEdit'
	import api from '@/api/axiosApi'
	import softhardApiList from '@/api/softhardApiList'
	import hyTable from '@/components/hengyun/table'
	export default {
		data() {
			return {
				delectState: false,
				editState: false,
				actId: null,
				columns: [{ //列表表头数据
						type: 'index',
						title: '序号',
						width: 60,
						align: 'center'
					},
					{
						title: '主机IP',
						key: 'ip'
					},
					{
						title: '所属集群',
						key: 'cluster'
					},
					{
						title: '型号',
						key: 'model'
					},
					{
						title: '处理器类型',
						key: 'cpuType'
					},
					{
						title: '处理器数量',
						key: 'cpuCount',
						width: 110
					},
					{
						title: '网卡数量',
						key: 'netcardCount',
						width: 110
					},
					{
						title: '内存',
						key: 'memory',
						width: 110
					},
					{
						title: '磁盘大小',
						key: 'diskSize',
						width: 110
					},
					{
						title: '操作',
						key: 'address',
						width: 130,
						render: (h, params) => {
							const vm = this;
							const delect = h('Button', {
								props: {
									type: 'error',
									size: 'small'
								},
								on: {
									click: () => {
										var index = params.index;
										vm.delectEngine(index);
									}
								}
							}, '删除');
							return h('div', [delect]);
						}
					}
				],
				data: [], //表格数据
				indexDelete: -1,
			}
		},
		components: {
			engineEdit,
		},
		mounted() {
			this.getAddHostByApplyId(); //审批列表数据查询
		},
		methods: {
			getAddHostByApplyId() { // 审批列表数据查询
				this.data = [];
				let keyid = this.$route.params.keyid;
				this.actId = keyid;
				api(softhardApiList.getAddHostByApplyId, {
					"keyid": keyid
				}).then((res) => {
					if(res.status == 200 && res.data.data) {
						this.data = res.data.data;
					}
				}, (err) => {
					//dong something...
				})
			},
			saveHost(param) { //保存主机数据
				let machineManageItem = {
					"applyKeyid": this.actId,
					"cluster": param.cluster,
					"cpuCount": param.cpuCount,
					"cpuType": param.cpuType,
					"diskSize": param.diskSize,
					"id": null,
					"ip": param.ip,
					"memory": param.memory,
					"model": param.model,
					"netcardCount": param.netcardCount,
					"stepCode": "YIYUN_HANDLER"
				};
				this.data.push(machineManageItem);
				this.editState = false;
			},
			deleteMachine() { //删除主机数据
				if(this.indexDelete>=0){
					this.data.splice(this.indexDelete,1);
					this.delectClose();
				}
			},
			// 操作
			createEngine() { //新增
				this.editState = true;
			},
			editSubmit(name) { //新增提交按钮触发事件
				this.$refs[name].handleSubmit("engine");
			},
			editClose() { //关闭编辑
				this.editState = false;
			},
			delectEngine(index) { //删除
				this.indexDelete = index;
				this.delectState = true;
			},
			delectClose() { //关闭删除
				this.delectState = false;
				this.indexDelete = -1;
			},
			goback() {//返回
				this.$router.go(-1);
			},
			hasTableData () {
				if (this.data.length < 1) {
					this.$Message.error('请添加主机！');
				} else {
					this.hostSave()
				}
			},
			hostSave() {//保存保存主机数据集合
				let _this = this;
				api(softhardApiList.yysaveMachines,{"machines":[...this.data]}).then((res) => {
					if(res.status == 200 && res.data.data) {
						this.getAddHostByApplyId();
						this.$Message.success({
							content: '保存成功！',
							duration: 3
						});
					}else{
						this.error(res.data.errmsg);
					};
				}, (err) => {
					//dong something...
				})
			},
			hostSubmit() {//提交
				api(softhardApiList.yiYunApproveSubmit, {
					"applyKeyid": this.actId,
					"machineManage":[...this.data],
					"stepCode":"YIYUN_ADD_HOST"
				}).then((res) => {
					if(res.status == 200 && res.data.data) {
						this.$Message.success({
							content: '保存成功！',
							duration: 3
						});
						this.$router.push({
							path: '/gxyyApply'
						});
					}else{
						this.error(res.data.errmsg);
					};
				}, (err) => {
					//dong something...
				})
			},
			//提示
			success() { //成功提示
				this.$Message.success({
					content: '编辑成功！',
					duration: 3
				});
			},
			error(errormsg) { //错误提示
				this.$Message.error({
					content: errormsg,
					duration: 3
				});
			},
		},
		watch: {
			editState(val) {
				if(!val) {
					this.$refs['engineEdit'].reload();
				}
			}
		}
	}
</script>

<style lang='less'>
	button.btnCreate {
		float: right;
		margin-right: 0;
		margin-bottom: 10px;
	}
	
	.access-list {
		span.handle {
			margin: 0 5px;
			display: inline-block;
			cursor: pointer;
			&:hover {
				color: #57a3f3;
			}
		}
	}
	
	.ivu-btn {
		margin-right: 5px;
	}
	
	.pageFooter{
		text-align: center;
		margin: 20px 0;
	}
</style>