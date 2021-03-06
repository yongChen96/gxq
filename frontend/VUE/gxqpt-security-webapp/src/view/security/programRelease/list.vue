<template>
	<Layout>
		<Content>
			<Breadcrumb>
				<BreadcrumbItem>安全保障平台</BreadcrumbItem>
				<BreadcrumbItem>程序发布管理</BreadcrumbItem>
			</Breadcrumb>
			<Card>
				<Form ref="formValidate" :model="formData" inline :label-width="100">
					<FormItem label="系统名称" :label-width="64">
						<Select v-model="formData.name" style="min-width: 150px;">
							<Option value="-1">全部</Option>
							<Option v-for="(item, idx) in visibleForSelf" :value="item.name" :key="idx">{{item.name}}</Option>
						</Select>
					</FormItem>
					<FormItem label="审核状态">
						<Select v-model="formData.auditStatus" style="min-width: 80px;">
							<Option value="-1">全部</Option>
							<Option value="1">已审核</Option>
							<Option value="0">未审核</Option>
						</Select>
					</FormItem>
					<FormItem label="日期范围">
						<DatePicker type="daterange" v-model="dateRange" :editable="false" style="min-width: 260px;"></DatePicker>
					</FormItem>
					<FormItem :label-width="30">
						<Button type="primary" @click="preSearch" v-if="checkButton('program_release_search')">查询</Button>
					</FormItem>
					<router-link to="/programRelease/create">
						<Button type="primary" v-if="checkButton('program_release_newversion')">发布新版本</Button>
					</router-link>
				</Form>
				<hy-table
					border
					ref="selection"
					:data="data"
					:current="pageOption.current"
					:columns="columns"
					:total="pageOption.total"
					:page-size="pageOption.pageSize"
					@on-change="pageChange"
					@on-page-size-change="changePageSize"
					show-sizer
					show-elevator/>
			</Card>
		</Content>
		<Modal v-model="modal" title="审核" width="60%" :mask-closable="false">
			<examine :id="selectedId" ref="examine" @examine="examineSave"></examine>
			<div slot="footer">
				<Button type="primary" @click="handleSubmit('examine')">确认</Button>
				<Button type="text" @click="closeModal">取消</Button>
			</div>
		</Modal>
	</Layout>
</template>

<script>
	import examine from './examine'
	import api from '@/api/axiosApi'
	import apiList from '@/api/securityApiList'
	import { mapState } from 'vuex'
	function getDateRange(time) {
	  if (!time) {
	    return ''
	  }
	  // 结束日期
	  const endDate = new Date(time)
	  // 当前日期往前推30天
	  const startDate = new Date(time - 30 * 24 * 60 * 60 * 1000)
	  return {
	    start: `${startDate.getFullYear()}-${startDate.getMonth() + 1}-${startDate.getDate()}`,
	    end: `${endDate.getFullYear()}-${endDate.getMonth() + 1}-${endDate.getDate()}`
	  }
	}
	export default {
		data() {
		    const slgLineStyle =  {
		      	maxHeight: '36px',
		      	overflow: 'hidden',
		      	margin: '10px 0',
		      	textOverflow: 'ellipsis',
		      	whiteSpace: 'nowrap'
		    }
		    const defaultRange = getDateRange(Date.now())
			return {
				modal: false, //模态框显示状态
				// systemNames: this.$store.state.visibleForSelf,
				formData: {
					name: '-1', //系统名称
					startDate: defaultRange.start, //开始时间
					endDate: defaultRange.end, //结束时间
					auditStatus: "-1" //审批状态
				},
				selectedId: '', //点击审批时记录的当前系统版本的id
				dateRange: [defaultRange.start, defaultRange.end],
				pageOption: {
					current: 1,
					total: 0,
					pageSize: 10
				},
				upgradeConentText:'',
				columns: [{
						type: 'index',
						title: '序号',
						width: 60,
						align: 'center'
					},
					{
						title: '系统名称',
						key: 'name'
					},
					{
						title: '发布日期',
						key: 'upgradeTime'
					},
					{
						title: '版本名称',
						key: 'versionName'
					},
					{
						title: '发布版本号',
						key: 'versionId'
					},
					{
						title: '升级内容',
						key: 'upgradeConent',
						// type: 'html',
				        render: (h, params) => {
				        	let html = '--'
				        	const contentArr = params.row.upgradeConent.match(/>[^<]+/g);
		                    if (contentArr) {
		                        html = contentArr.join('').replace(/>/g, '');
		                    }
				            return h('div', {
				                style: {...slgLineStyle},
				                attrs: {
				                  title: html
				                }
				            },html)
				        }
					},
					{
						title: '发布服务器',
						key: 'serverIp'
					},
					{
						title: '审核状态',
						key: 'auditStatus',
						render(h, params) {
							if(params.row.auditStatus == 0) {
								return h('div', [
									h('span', {
										style: {
											color: 'red'
										}
									}, '未审核')
								])
							} else {
								return h('div', [
									h('span', {}, '已审核')
								])
							}
						}
					},
					{
						title: '操作',
						key: 'act',
						width: 200,
						align: 'center',
						render: (h, params) => {
							const apply = h('Button', {
								props: {
									type: 'primary',
									size: 'small'
								},
								domProps: {
									innerHTML: '审核'
								},
								style: {
									marginRight: '5px',
									display: this.checkButton('program_release_audit') ? 'inline-block' : 'none'
								},
								on: {
									click: () => {
										this.modal = true;
										this.selectedId = params.row.id;
									}
								}
							});
							const del = h('Button', {
								props: {
									type: 'primary',
									size: 'small'
								},
								domProps: {
									innerHTML: '删除'
								},
								style: {
									marginRight: '5px',
									display: this.checkButton('program_release_audit') ? 'inline-block' : 'none'
								},
								on: {
									click: () => {
										this.delReleaseItem(params.row.id)
									}
								}
							});
							const look = h('Button', {
								props: {
									type: 'primary',
									size: 'small'
								},
								domProps: {
									innerHTML: '查看'
								},
								style: {
									marginRight: '5px',
									display:this.checkButton('program_release_view')?'inline-block':'none'
								},
								on: {
									click: () => {
										this.$router.push({
											path: "/programRelease/" + params.row.id,
										});
									}
								}
							});
							if(params.row.auditStatus == 0) {
								return h('div', [apply, look, del]);
							} else {
								return h('div', [look]);
							}
						}
					},
				],
				data: []
			}
		},
		computed: {
			...mapState([
				'authButton',
				'visibleForSelf'
			])
		},
		components: {
			examine
		},
		methods: {
			init() {
				// this.getSystemName();
				this.getProgram();
			},
			// getSystemName() {
			// 	api(apiList.getSystemName).then((res) => {
			// 		if(res.status == 200 && res.data.data) {
			// 			this.systemNames = res.data.data;
			// 		}
			// 	}, (err) => {
			// 		//dong something...
			// 	})
			// },
			preSearch() {
				this.pageOption.current = 1
				this.getProgram()
			},
			getProgram() { //获取程序
				let params = {
					data: JSON.parse(JSON.stringify(this.formData)),
					pageNo: this.pageOption.current,
					pageSize: this.pageOption.pageSize
        }
        let endDay = (parseInt(params.data.endDate.substring(params.data.endDate.length-2)) + 1).toString()
        if (parseInt(endDay) < 10) endDay = '0' + endDay
        params.data.endDate = params.data.endDate.substring(0, 8) + endDay
				api(apiList.getProgram, params).then((res) => {
					if(res.status == 200 && res.data.data.list) {
						this.data = res.data.data.list;
						this.data.forEach(item=>{
							item.versionId = 'V' +item.versionId;//版本加V
						})
						console.log(this.data);
						if(this.data.length>0){
							this.pageOption.pageSize = res.data.data.pageSize;
							this.pageOption.total = res.data.data.total;
							this.pageOption.current = res.data.data.pageNum;
						};
					}
				}, (err) => {
					//dong something...
				})
			},
			delReleaseItem(id){
				this.$Modal.confirm({
					title: '删除确认',
					content: '确认删除吗？',
					onOk: () => {
						api(apiList.deleteProgram, {id:id}).then((res) => {
							if(res.status == 200 && res.data.errcode == 0) {
								this.$Message.success('删除成功！');
								this.init();
							}else{
								this.$Message.info(res.data.errmsg)
							}
						}, (err) => {})
					}
				})
			},
			examineSave(param) {
				param.id = this.selectedId;
				api(apiList.auditProgram, param).then((res) => {
					if(res.status == 200) {
						this.modal = false;
						this.getProgram();
					} else {
						this.$Message.error('审核失败');
					}
				}, (err) => {
					//do something...
				})
			},
			handleSubmit(name) {
				this.$refs[name].handleSubmit("examineData");
			},
			closeModal() {
				this.modal = false;
			},
			pageChange(num) { //页码改变的回调
				this.pageOption.current = num;
				this.getProgram();
			},
			changePageSize(num) { //切换每页条数时的回调
				this.pageOption.pageSize = num;
				this.getProgram();
			},
			checkButton(code) {
				if(this.authButton.indexOf(code) >= 0) {
					return true;
				} else {
					return false;
				}
			},
		},
		watch: {
			dateRange: function(val, oldVal) {
				let d1 = new Date(val[0]);
        let d2 = new Date(val[1]);
        let day1 = d1.getDate()
        if (day1 < 10) day1 = '0' + day1
        let day2 = d2.getDate()
        if (day2 < 10) day2 = '0' + day2
				this.formData.startDate = d1.getFullYear() + '-' + (d1.getMonth() + 1) + '-' + day1;
				this.formData.endDate = d2.getFullYear() + '-' + (d2.getMonth() + 1) + '-' + day2;
			}
		},
		created() {
			this.$nextTick(() => {
				this.init();
			})
		}
	}
</script>

<style type="text/css" scoped>
	.w168 {
		width: 168px;
	}
</style>