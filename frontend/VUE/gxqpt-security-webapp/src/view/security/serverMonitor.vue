<template>
	<Layout>
		<Content>
			<Breadcrumb>
				<BreadcrumbItem>服务器监控</BreadcrumbItem>
				<BreadcrumbItem>服务监控</BreadcrumbItem>
			</Breadcrumb>
			<Card>
				<Form ref="formValidate" inline :label-width="72" :model="searchFormData">
					<FormItem label="服务器地址">
						<Input type="text" v-model="searchFormData.serverIp">
						</Input>
					</FormItem>
					<FormItem label="服务名称">
						<Input type="text" v-model="searchFormData.name">
						</Input>
					</FormItem>
					<FormItem label="处理状态">
						<Select type="text" v-model="searchFormData.status">
							<Option value="-1">全部</Option>
							<Option value="0">未处理</Option>
							<Option value="1">已处理</Option>
						</Select>
					</FormItem>
					<FormItem :label-width="30">
						<Button type="primary" @click="search">查询</Button>
					</FormItem>
				</Form>
				<hy-table border ref="table" :data="data" :columns="columns" :current="pageOption.current" :total="pageOption.total" :page-size="pageOption.pageSize" @on-change="pageChange" @on-page-size-change="changePageSize" show-sizer show-elevator/>
			</Card>
			<Modal title="服务监控异常处理" class="modal-no-footer" v-model="dealModalShow" :width="600" :mask-closable="false">
				<dealModal v-if="dealModalShow" @close="closeModal" ref="dealModal" :isCheck="isCheck" :id="selectedId" />
			</Modal>
		</Content>
	</Layout>
</template>

<script>
	import dealModal from './serverMachineMonitor/serverDealModal.vue'
	import api from '@/api/axiosApi'
	import apiList from '@/api/securityApiList'
	import { mapState } from 'vuex'
	export default {
		data() {
			return {
				dealModalShow: false,
				selectedId: '', //点击处理记录生成的Id
				isCheck: false,
				searchFormData: {
					status:'-1'
				},
				pageOption: { //分页配置参数
					current: 1,
					total: 0,
					pageSize: 10
				},
				columns: [{
						type: 'index',
						title: '序号',
						width: 60,
						align: 'center'
					},
					{
						title: '服务器地址',
						key: 'serverIp'
					},
					{
						title: '服务名称',
						key: 'name'
					},
					{
						title: '用户',
						key: 'account'
					},
					{
						title: '预警日期',
						key: 'warnTime'
					},
					{
						title: '处理状态',
						key: 'status',
						render(h, params) {
							let text;
							if(params.row.status == 0) {
								text = '未处理';
							} else {
								text = '已处理';
							}
							return h('span', text);
						}
					},
					{
						title: '处理人',
						key: 'handleUser'
					},
					{
						title: '处理时间',
						key: 'handleTime'
					},
					{
						title: '操作',
						key: 'address',
						align: 'center',
						render: (h, params) => {
							const vm = this;
							const status = params.row.status
							const id = params.row.id
							switch(status) {
								case 0:
									return h('Button', {
										props: {
											type: 'primary',
											size: 'small'
										},
										style: {
											// marginRight: '5px',
											display:this.checkButton('security_server_monitor_service_handler_log')?'inline-block':'none'
										},
										on: {
											click() {
												vm.deal(id)
											}
										}
									}, '处理记录')
								case 1:
									return h('Button', {
										props: {
											type: 'primary',
											size: 'small'
										},
										style: {
											// marginRight: '5px',
											display:this.checkButton('security_server_monitor_service_view')?'inline-block':'none'
										},
										on: {
											click() {
												vm.check(id)
											}
										}
									}, '查看')
								default:
									return h('span', '--')
							}
						}
					}
				],
				data: []
			}
		},
		computed:{
			...mapState([
				'authButton'
			])
		},
		methods: {
			init() {
				this.getServerMonitor();
      },
      search() {
				this.pageOption.current=1;
				this.getServerMonitor();
      },
			getServerMonitor() {
				console.log(this.searchFormData);
				let params = {
					data: this.searchFormData,
					pageNo: this.pageOption.current,
					pageSize: this.pageOption.pageSize
				}
				api(apiList.getServerMonitor, params).then((res) => {
					if(res.status == 200) {
						this.data = res.data.data.list;
						this.pageOption.total = res.data.data.total;
					}
				}, (err) => {
					//do something...
				})
			},
			// 提交信息
			dealModalSubmit() {
				this.$refs.dealModal.submit()
			},
			// 点击处理
			deal(id) {
				this.selectedId = id;
				this.dealModalShow = true
				this.isCheck = false
			},
			closeModal(type) {
				this.dealModalShow = false;
				if(type == 1) {
					this.getServerMonitor();
				}
			},
			// 点击查看
			check(id) {
				this.selectedId = id;
				this.isCheck = true
				this.dealModalShow = true
			},
			pageChange(num) { //页码改变的回调
				this.pageOption.current = num;
				this.getServerMonitor();
			},
			changePageSize(num) { //切换每页条数时的回调
				this.pageOption.pageSize = num;
				this.getServerMonitor();
			},
			checkButton(code){
				if(this.authButton.indexOf(code) >= 0){
					return true;
				}else{
					return false;
				}
			}
		},
		components: {
			dealModal: dealModal
		},
		created() {
			this.$nextTick(()=>{
				this.init();
			})
		}
	}
</script>

<style lang="less" scoped>
	.modal-no-footer /deep/ .ivu-modal-footer {
		display: none;
	}
	
	.page-split {
		margin-top: 10px;
	}
</style>