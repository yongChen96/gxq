<template>
    <Layout>
      <Content>
        <Breadcrumb>
          <BreadcrumbItem>故障管理</BreadcrumbItem>
          <BreadcrumbItem>我的任务</BreadcrumbItem>
        </Breadcrumb>
        <Card>
          <Form :model="seachData" ref="formValidate" inline :label-width="90">
            <FormItem label="故障名称">
              <Input class="queryItem" type="text" v-model="seachData.faultName"> </Input>
            </FormItem>
            <FormItem label="故障级别">
              <Select class="queryItem" v-model="seachData.faultLevel">
                <Option value="">全部</Option>
                <Option value="1">一级</Option>
                <Option value="2">二级</Option>
                <Option value="3">三级</Option>
              </Select>
            </FormItem>
            <FormItem label="产生时间">
              <DatePicker class="queryItem" :editable="false" @on-change="changeTime" format="yyyy/MM/dd" type="daterange" placement="bottom-end" placeholder="请选择日期"></DatePicker>
            </FormItem>
            <FormItem label="故障状态">
              <Select class="queryItem" v-model="seachData.status">
                <Option value="0">全部</Option>
                <Option value="1">未处理</Option>
                <Option value="2">处理中</Option>
                <Option value="3">已完结</Option>
              </Select>
            </FormItem>
            <FormItem label="系统名称">
              <Select class="queryItem" v-model="seachData.systemId">
                <Option value="">全部</Option>
                <Option v-for="item in systemList" :value="item.appId" :key="item.appId">{{item.name}}</Option>
              </Select>
            </FormItem>
            <FormItem label="故障主机">
              <Input class="queryItem" type="text" v-model="seachData.faultHost"> </Input>
            </FormItem>
            <FormItem :label-width="20">
              <Button type="primary" v-if="authButton.includes('ops_fault_mytask_query')" @click="preSearch">查询</Button>
            </FormItem>
          </Form>
          <hy-table
            highlight-row
            border
            :current="pageInfo.pageNo"
            :columns="columns"
            :data="data"
            :total="Number(pageInfo.total)"
            :pageSize="Number(pageInfo.pageSize)"
            pageType="small"
            show-elevator
            show-sizer
            ref="selection"
            @on-page-change="pageChange" />
        </Card>
      </Content>
      <Modal v-model="isTrueModel" title="处理" id="reportDeal" width="540" :mask-closable="false">
        <Form :model="isTrueData" ref="dataIsTrue" :rules="updateIsTrueValid" inline :label-width="90">
          <FormItem label="是否属实">
            <Select class="dataStyle" v-model="isTrueData.isTrue">
              <Option value="1">属实</Option>
              <Option value="2">不属实</Option>
            </Select>
          </FormItem>
          <FormItem label="理由" prop="dealOpinion" v-if="isTrueData.isTrue=='2'">
            <Input class="dataStyle" v-model="isTrueData.dealOpinion" type="textarea" :rows="5" :maxlength="200"></Input>
          </FormItem>
        </Form>
        <div slot="footer">
          <Button type="default" @click="closeIsTrueModel">关闭</Button>
          <Button type="primary" @click="updateIstrue('dataIsTrue')">确定</Button>
        </div>
      </Modal>
      <Modal v-model="dealReport" title="处理报告上传" id="reportDeal" width="60%" :mask-closable="false">
        <deal-report ref="doccreate" v-on:examine="createSave" :dealReport="dealReport"></deal-report>
        <div slot="footer">
          <Button type="default" @click="closeDealReport">关闭</Button>
          <Button type="primary" @click="handleSubmit('doccreate')">确定</Button>
        </div>
      </Modal>
      <Modal v-model="dealbgView" title="处理报告" width="60%" :mask-closable="false">
        <dealbg-view :dataView="dataView"></dealbg-view>
        <div slot="footer">
          <Button type="text" @click="closedealbgModel">关闭</Button>
        </div>
      </Modal>
    </Layout>
</template>

<script>
/* eslint-disable */
import {mapState} from 'vuex'
import dealbgView from './dealView'
import dealReport from './dealReport'
import api from '@/api/axiosApi'
import operationApiList from '@/api/operationApiList'
export default {
  data() {
    const vm = this
    return {
      systemList: [], //系统名称列表
      dealReport: false,
      isTrueModel: false,
      dealbgView: false,
      seachData: {}, //查询参数
      columns: [{
          type: 'index',
          width: 60,
          title: '序号',
          align: 'center'
        },
        {
          title: '故障名称',
          key: 'faultName'
        },
        {
          title: '故障类别',
          key: 'faultType'
        },
        {
          title: '故障级别',
          key: 'faultLevel',
          maxWidth: 100,
          render: (h, params) => {
            return h('div', ['--', '一级', '二级', '三级'][params.row.faultLevel])
          }
        },
        {
          title: '系统名称',
          key: 'systemName'
        },
        {
          title: '故障主机',
          key: 'faultHost'
        },
        {
          title: '故障状态',
          key: 'status',
          maxWidth: 100,
          render: (h, params) => {
            var statusText;
            switch(params.row.status) {
              case 1:
                statusText = "未处理";
                break;
              case 2:
                statusText = "处理中";
                break;
              case 3:
                statusText = "已完结";
                break;
              default:
                statusText = "";
                break;
            };
            return h('span', statusText);
          }
        },
        {
          title: '产生时间',
          key: 'happenTime',
          width: 160,
        },
        {
          title: '操作',
          key: 'act',
          align: 'center',
          width: 180,
          render: (h, params) => {
            const deal = h('Button', {
              props: {
                type: 'primary',
                size: 'small'
              },
              style: {
                marginRight: '5px'
              },
              on: {
                click: () => {
                  let id = params.row.id;
                  this.dealIsTrue(id);
                }
              }
            }, '处理');
            const detail = h('Button', {
              props: {
                type: 'primary',
                size: 'small'
              },
              style: {
                marginRight: '5px'
              },
              on: {
                click: () => {
                  this.$router.push({
                    name: 'stopDetail',
                    params: {
                      id: params.row.id
                    }
                  })
                }
              }
            }, '详情');
            const dealTask = h('Button', {
              props: {
                type: 'primary',
                size: 'small'
              },
              style: {
                marginRight: '5px'
              },
              on: {
                click: () => {
                  let ledgerId = params.row.id;
                  this.dealUpload(ledgerId);
                }
              }
            }, '处理报告上传');
            // const dealDetail = h('Button', {
            //   props: {
            //     type: 'primary',
            //     size: 'small'
            //   },
            //   style: {
            //     marginRight: '5px'
            //   },
            //   on: {
            //     click: () => {
            //       this.dealbgView = true;
            //       let ledgerId = params.row.id;
            //       this.faultBgGetById(ledgerId);
            //     }
            //   }
            // }, '处理报告');
            let actBtn = [];
            switch(params.row.status) {
              case 1:
                if (vm.authButton.includes('ops_fault_mytask_handle')) {
                  actBtn.push(deal)
                }
                break;
              case 2:
                if (vm.authButton.includes('ops_fault_mytask_upload')) {
                  actBtn.push(dealTask)
                }
                break;
              // case 3:
              //   if(params.row.isTrue=="1"){
              //     if (vm.authButton.includes('ops_fault_mytask_baogao')) {
              //       actBtn.push(dealDetail)
              //     }
              //   }
              //   break
            }
            if (vm.authButton.includes('ops_fault_mytask_detail')) {
              actBtn.push(detail)
            }
            return h('div', actBtn);
          }
        },
      ],
      data: [],
      pageInfo: { //分页参数
        pageNo: 1,
        total: 0,
        pageSize: 10
      },
      dataView: {}, //详情数据
      updateIsTrueValid: { //处理提交字段校验
        dealOpinion: [{
          required: true,
          message: '该项为必填项，请填写相应数据！',
          trigger: 'blur'
        }],
      },
      isTrueData: { //处理提交数据
        isTrue: "1"
      },
      actId: "",
    }
  },
  computed: {
    ...mapState(['authButton'])
  },
  components: {
    dealbgView,
    dealReport
  },
  mounted() {
    this.getSystemList(); //获取系统列表
    this.pageMyFaultList(); //查询我的任务故障信息
  },
  methods: {
      preSearch() {
          this.pageInfo.pageNo = 1
          this.pageMyFaultList()
      },
    getSystemList() { //获取系统列表
      const vm = this
      api(operationApiList.findAppByAdminWithEnable)
      .then(res => {
        if(res.data.errcode === 0) {
          vm.systemList = res.data.data
        }
      }, (error) => {
        console.log(error)
      })
    },
        pageChange(pageNo, pageSize) {
            this.pageInfo.pageNo = pageNo
            this.pageInfo.pageSize = pageSize
            this.pageMyFaultList()
        },
    closeDealReport() {
      this.dealReport = false;
      this.actId="";
    },
    closeIsTrueModel() {
      this.actId="";
      this.isTrueModel = false;
    },
    closedealbgModel() {
      this.actId="";
      this.dealbgView = false;
    },
    changeTime(val) { //选择时间
      if(val[0] && val[1]) {
        var startTime = val[0].replace(/\//g, "-");
        var endTime = val[1].replace(/\//g, "-");
        this.seachData.startTime = startTime + " 00:00:00";
        this.seachData.endTime = endTime + " 23:59:59";
      } else {
        this.seachData.startTime = "";
        this.seachData.endTime = "";
      }
    },
    pageMyFaultList() { //查询我的任务故障信息
      const vm = this;
      let searchString = (JSON.stringify(vm.seachData)).replace(/\ +/g, "");
      let searchJson = JSON.parse(searchString);
      searchJson.startTime = this.seachData.startTime
      searchJson.endTime = this.seachData.endTime
      var formData = {
        "data": searchJson,
        "pageNo": this.pageInfo.pageNo,
        "pageSize": this.pageInfo.pageSize
      };
      api(operationApiList.pageMyFaultList, formData).then(res => {
        if(res.data.errcode === 0) {
          vm.data = res.data.data.list;
          vm.pageInfo.pageSize = res.data.data.pageSize;
          vm.pageInfo.total = res.data.data.total;
          vm.pageInfo.pageNo = res.data.data.pageNum;
        }
      }, (error) => {
        console.log(error)
      })
    },
    faultGetById(ledgerId) { //根据Id查询详情信息
      api(operationApiList.faultGetById, {
        "id": ledgerId
      }).then((res) => {
        if(res.status == 200 && res.data.data) {
          this.dataView = res.data.data;
          if(this.dealReport){
            this.$refs['doccreate'].getDetail(this.dataView);
          };
        }
      }, (err) => {
        //dong something...
      })
    },
    faultBgGetById(ledgerId) { //根据Id查询处理报告详情信息
      api(operationApiList.faultGetById, {
        "id": ledgerId
      }).then((res) => {
        if(res.status == 200 && res.data.data) {
          this.dataView = res.data.data;
        }
      }, (err) => {
        //dong something...
      })
    },
    dealIsTrue(id) { //打开处理页面
      this.actId = id;
      this.isTrueData = {
        isTrue: '1',
        dealOpinion: ''
      }
      this.isTrueModel = true;
    },
    updateIstrue(name) {//处理提交方法
      let dealData = this.isTrueData;
      dealData.id = this.actId;
      if(this.isTrueData.isTrue == "1") {
        this.updateIsTrueSave(dealData);
      }else{
        this.$refs[name].validate((valid) => {
          if(valid) {
            this.updateIsTrueSave(dealData);
          }
        })
      };
    },
    updateIsTrueSave(dealData) {//处理
      api(operationApiList.updateIsTrue, dealData).then((res) => {
        if(res.status == 200 && res.data.data) {
          this.success();
          this.closeIsTrueModel();
          this.pageMyFaultList();
          this.actId="";
        } else {
          this.error(res.data.errmsg);
        }
      }, (err) => {
        //dong something...
      })
    },
    dealUpload(id){ //打开处理报告上传页面
      this.actId = id;
      this.dealReport = true;
      this.faultGetById(id);
    },
    handleSubmit(name) {
      this.$refs[name].handleSubmit("docData");
    },
    createSave(param) {//处理报告上传
      console.log(param);
      api(operationApiList.updateFaultManage, param).then((res) => {
        if(res.status == 200 && res.data.data) {
          this.success();
          this.closeDealReport();
          this.pageMyFaultList();
          this.actId="";
        } else {
          this.error(res.data.errmsg);
        }
      }, (err) => {
        //dong something...
      })
      this.createState = false;
    },
    success() { //成功提示
      this.$Message.success({
        content: '处理成功！',
        duration: 3
      });
    },
    error(errormsg) { //错误提示
      this.$Message.error({
        content: errormsg,
        duration: 3
      });
    },
  }
}
</script>

<style type="text/css" scoped>
  .wAuto {
    width: 100%;
  }

  .queryItem {
    width: 230px;
  }

  .dataStyle {
    width: 400px;
  }

  .flow {
    margin-top: 20px;
  }
</style>
