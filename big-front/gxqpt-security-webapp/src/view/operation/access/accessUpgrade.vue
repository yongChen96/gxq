<template>
  <Layout style="min-height: 500px;">
    <Content>
      <Breadcrumb>
        <BreadcrumbItem>接入管理</BreadcrumbItem>
        <BreadcrumbItem>我的应用</BreadcrumbItem>
        <BreadcrumbItem>应用升级</BreadcrumbItem>
      </Breadcrumb>
      <Card>
        <Form
          inline
          :label-width="100"
          :model="searchCondition">
          <Row>
            <Col span="24">
              <FormItem label="发布日期：">
                <DatePicker
                  v-model="publishTime"
                  format="yyyy-MM-dd"
                  type="daterange"
                  placeholder="请选择日期">
                </DatePicker>
              </FormItem>
              <FormItem label="版本号：">
                <Input
                  type="text"
                  v-model="searchCondition.versionNumber">
                </Input>
              </FormItem>
              <FormItem :label-width="20">
                <Button type="primary" @click="preSearch" style="margin-right: 20px;">查询</Button>
              </FormItem>
              <FormItem style="float: right;">
                <Button type="primary" @click="publishNewVertion">发布新版本</Button>
                <router-link to="/accessList">
                  <Button type="primary">返回</Button>
                </router-link>
              </FormItem>
            </Col>
          </Row>
        </Form>
        <hy-table
          highlight-row
          border
          :current="pageInfo.pageNo"
          :columns="tableList.columns"
          :data="tableList.data"
          :total="Number(pageInfo.total)"
          :pageSize="Number(pageInfo.pageSize)"
          pageType="small"
          show-elevator
          show-sizer
          class="access-upgrade"
          @on-change="pageNoChange"
          @on-page-size-change="pageSizeChange" />
        <Modal
          v-model="showModal"
          :mask-closable="false"
          :title="currentAct == ACTS.edit ? '修改' : '发布新版本'">
          <Form
            ref="modalForm"
            :label-width="100"
            :model="modalForm"
            :rules="formValidate">
            <FormItem label="系统名称：">
              <Input
                type="text"
                v-model="modalForm.systemName" disabled>
              </Input>
            </FormItem>
            <FormItem label="版本号：" prop="versionNumber">
              <Input
                type="text"
                :maxlength="10"
                v-model="modalForm.versionNumber">
              </Input>
            </FormItem>
            <FormItem label="版本名称：" prop="versionName">
              <Input
                type="text"
                :maxlength="60"
                v-model="modalForm.versionName">
              </Input>
            </FormItem>
            <FormItem label="发布服务器：" prop="publishingServer">
              <Input
                type="text"
                :maxlength="60"
                v-model="modalForm.publishingServer">
              </Input>
            </FormItem>
            <FormItem label="是否启用：">
              <RadioGroup v-model="modalForm.status">
                <Radio :label="1">启用</Radio>
                <Radio :label="2">不启用</Radio>
              </RadioGroup>
            </FormItem>
            <FormItem label="发布文件：" prop="files" required>
              <hyUpload
                ref="fileUpload"
                action="/api/file/p/file/simple"
                :onSuccess="fileUploadSuccess"
                :format="['jar','html','css', 'js', 'jpg', 'png', 'mp3', 'mp4']"
                :default-file-list="defaultFiles"/>
            </FormItem>
            <FormItem label="升级内容：" prop="upgradeContent">
              <Input
                type="textarea"
                :maxlength="800"
                :autosize="{minRows: 3, maxRows: 3}"
                v-model="modalForm.upgradeContent">
              </Input>
            </FormItem>
            <FormItem label="备注：">
              <Input
                type="textarea"
                :maxlength="800"
                :autosize="{minRows: 3, maxRows: 3}"
                v-model="modalForm.remark">
              </Input>
            </FormItem>
          </Form>
          <div slot="footer">
            <Button class="modalBtn" type="default" @click="showModal = false" size="large">取消</Button>
            <Button class="modalBtn" type="primary" @click="saveInfo" size="large">确定</Button>
          </div>
        </Modal>
        <Modal
          v-model="detailModal"
          :mask-closable="false"
          title="版本查看">
          <Form :label-width="100">
            <FormItem label="系统名称：">
              <Input
                type="text"
                v-model="modalDetail.systemName" disabled>
              </Input>
            </FormItem>
            <FormItem label="版本号：" prop="versionNumber">
              <Input
                type="text"
                v-model="modalDetail.versionNumber" disabled>
              </Input>
            </FormItem>
            <FormItem label="版本名称：" prop="versionName">
              <Input
                type="text"
                v-model="modalDetail.versionName" disabled>
              </Input>
            </FormItem>
            <FormItem label="发布服务器：" prop="publishingServer">
              <Input
                type="text"
                v-model="modalDetail.publishingServer" disabled>
              </Input>
            </FormItem>
            <FormItem label="是否启用：">
              <RadioGroup v-model="modalDetail.status">
                <Radio :label="1" disabled>启用</Radio>
                <Radio :label="2" disabled>不启用</Radio>
              </RadioGroup>
            </FormItem>
            <FormItem label="发布文件：">
              <Row v-for="file in modalDetail.fileList" :key="file.id">
                <Col span="18">
                  <Input
                    type="text"
                    v-model="file.fileName" disabled>
                  </Input>
                </Col>
                <Col span="6" style="text-align: right;">
                  <a :href="`/api/file/file/download?url=${file.fileUrl}&filename=${file.fileName}`" target="_blank">
                    <Button type="primary">下载</Button>
                  </a>
                </Col>
              </Row>
            </FormItem>
            <FormItem label="升级内容：" prop="upgradeContent">
              <Input
                type="textarea"
                :autosize="{minRows: 3, maxRows: 3}"
                v-model="modalDetail.upgradeContent" disabled>
              </Input>
            </FormItem>
            <FormItem label="备注：">
              <Input
                type="textarea"
                :autosize="{minRows: 3, maxRows: 3}"
                v-model="modalDetail.remark" disabled>
              </Input>
            </FormItem>
          </Form>
          <div slot="footer">
            <Button class="modalBtn" type="default" @click="detailModal = false" size="large">关闭</Button>
          </div>
        </Modal>
      </Card>
    </Content>
  </Layout>
</template>

<script>
import api from '@/api/axiosApi'
import operationApiList from '@/api/operationApiList'
// 文件上传
import hyUpload from '@/components/hengyun/hyUpload'

const ACTS = {
  // 发布新版本
  create: 'create',
  // 查看详情
  detail: 'detail',
  // 编辑版本信息
  edit: 'edit'
}

function defaultDisplay(h, params, key) {
  return h('span', params.row[key] || '--')
}

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
  components: {
    hyUpload
  },
  data () {
    const vm = this
    const defaultDateRange = getDateRange(Date.now())
    const slgLineStyle =  {
      maxHeight: '36px',
      overflow: 'hidden',
      margin: '10px 0',
      textOverflow: 'ellipsis',
      whiteSpace: 'nowrap'
    }
    return {
      ACTS,
      detailModal: false,
      currentAct: '',
      searchCondition: {
        // 版本号
        versionNumber: ''
      },
      // publishTime: [defaultDateRange.start, defaultDateRange.end],
      publishTime: [],
      modalForm: {
        systemName: vm.$route.query.name,
        versionNumber: '',
        versionName: '',
        publishingServer: '',
        status: 1,
        upgradeContent: '',
        remark: '',
        files: []
      },
      // 编辑时文件列表
      defaultFiles: [],
      tableList: {
        columns: [{
          type: 'index',
          title: '序号',
          width: 60,
          align: 'center'
        },
        {
          title: '系统名称',
          key: 'systemName',
          render: (h, params) => {
            return defaultDisplay(h, params, 'systemName')
          }
        },
        {
          title: '发布日期',
          key: 'createTime',
          render: (h, params) => {
            return defaultDisplay(h, params, 'createTime')
          }
        },
        {
          title: '版本名称',
          key: 'versionName',
          render: (h, params) => {
            return defaultDisplay(h, params, 'versionName')
          }
        },
        {
          title: '发布版本号',
          key: 'versionNumber',
          render: (h, params) => {
            const version = params.row.versionNumber
            return h('span', version ? `V${version}` : '--')
          }
        },
        {
          title: '升级内容',
          key: 'upgradeContent',
          render: (h, params) => {
            const html = params.row.upgradeContent || '--'
            return h('div', {
              style: {...slgLineStyle},
              attrs: {
                title: html
              }
            }, html)
          }
        },
        {
          title: '发布服务器',
          key: 'publishingServer',
          render: (h, params) => {
            return defaultDisplay(h, params, 'publishingServer')
          }
        },
        {
          title: '状态',
          key: 'status',
          render: (h, params) => {
            const status = params.row.status
            return h('span', ['--', '启用', '禁用'][status])
          }
        },
        {
          title: '操作',
          key: 'status',
          minWidth: 120,
          render: (h, params) => {
            const {id, status} = params.row
            const detail = h('Button', {
              props: {
                type: 'primary',
                size: 'small'
              },
              style: {
                marginRight: '5px'
              },
              domProps: {
                innerHTML: '详情'
              },
              on: {
                click () {
                  vm.showDeails(id)
                }
              }
            })
            const open = h('Button', {
              props: {
                type: 'primary',
                size: 'small'
              },
              style: {
                marginRight: '5px'
              },
              domProps: {
                innerHTML: '启用'
              },
              on: {
                click () {
                  vm.openOrClose(id, 1)
                }
              }
            })
            const close = h('Button', {
              props: {
                type: 'primary',
                size: 'small'
              },
              style: {
                marginRight: '5px'
              },
              domProps: {
                innerHTML: '禁用'
              },
              on: {
                click () {
                  vm.openOrClose(id, 2)
                }
              }
            })
            const edit = h('Button', {
              props: {
                type: 'primary',
                size: 'small'
              },
              style: {
                marginRight: '5px'
              },
              domProps: {
                innerHTML: '修改'
              },
              on: {
                click () {
                  vm.preOpenEdit(params)
                }
              }
            })
            const btns = [edit, detail]
            if (status == 1) {
              btns.push(close)
            } else {
              btns.push(open)
            }
            return h('div', btns)
          }
        }],
        data: []
      },
      serverList: [],
      showModal: false,
      pageInfo: {
        pageNo: 1,
        pageSize: 10,
        total: 0
      },
      formValidate: {
        versionNumber: [
          {required: true, message: '不可为空', triggre: 'blur'},
          {validator: (rule, value, cb) => {
            try {
              const num = Number(vm.modalForm.versionNumber)
              if (!isNaN(num)) {
                cb()
              }
              cb(new Error('只能填写数字'))
            } catch (e) {
              cb(new Error('只能填写数字'))
            }
          }, trigger: 'blur'}
        ],
        versionName: [{required: true, message: '不可为空', triggre: 'blur'}],
        publishingServer: [{required: true, message: '不可为空', triggre: 'blur'}],
        upgradeContent: [{required: true, message: '不可为空', triggre: 'blur'}],
        files: [{validator: (rule, value, cb) => {
          if (vm.modalForm.files.length === 0) {
            cb(new Error('文件不可为空'))
          } else {
            cb()
          }
        }}]
      },
      modalDetail: {}
    }
  },
  methods: {
    async preOpenEdit (params) {
      const vm = this
      vm.currentAct = ACTS.edit
      vm.modalForm = {
        ...params.row,
        files: []
      }
      vm.modalForm.fileList = vm.modalForm.fileList || []
      const defaultFiles = []
      // 这里是为了处理最后上传的数据结构
      const detail = await api(operationApiList.getApplition, {
        id: params.row.id
      })
      detail.data.data.fileList.map(({fileName, fileSize, fileType, fileUrl}) => {
        defaultFiles.push({
          url: fileUrl,
          name: fileName,
          response: {
            data: {
              list: [{
                submittedFileName: fileName,
                size: fileSize,
                ext: fileType,
                url: fileUrl,
                id: params.row.id
              }]
            }
          }
        })
      })
      vm.modalForm.files = defaultFiles
      vm.defaultFiles = defaultFiles
      vm.showModal = true
    },
    pageNoChange(pageNo) {
      this.pageInfo.pageNo = pageNo
      this.search()
    },
    pageSizeChange(pageSize) {
      this.pageInfo.pageSize = pageSize
      this.search()
    },
    // 获取主机列表
    // getHostName() {
    //   const vm = this
    //   api(operationApiList.getHostName)
    //   .then(res => {
    //     if (res.data.errcode === 0) {
    //       vm.serverList = res.data.data
    //     }
    //   }, error => {console.log(error)})
    // },
    preSearch() {
      this.pageInfo.pageNo = 1
      this.search()
    },
    // 获取列表
    search (pageNo, pageSize) {
      const vm = this
      api(operationApiList.appUpgradePage, {
        pageNo: pageNo || vm.pageInfo.pageNo,
        pageSize: pageSize || vm.pageInfo.pageSize,
        data: {
          startTime: getDateRange(vm.publishTime[0]).end ? `${getDateRange(vm.publishTime[0]).end} 00:00:00` : '',
          endTime: getDateRange(vm.publishTime[1]).end ? `${getDateRange(vm.publishTime[1]).end} 23:59:59` : '',
          appId: vm.$route.query.id,
          ...vm.searchCondition
        }
      }).then(res => {
        if (res.data.errcode === 0) {
          const result = res.data.data
          vm.pageInfo.pageNo = result.pageNum
          vm.pageInfo.total = result.total
          vm.tableList.data = result.list
        }
      }, error => {console.log(error)})
    },
    saveInfo () {
      const vm = this
      vm.$refs.modalForm.validate(valid => {
        if (valid) {
          const files = []
          vm.modalForm.files.map(value => {
            const file = value.response.data.list[0]
            files.push({
              fileName: file.submittedFileName,
              fileSize: file.size,
              fileType: file.ext,
              fileUrl: file.url,
              fileId: file.id
            })
          })
          vm.modalForm.files = files
          const url = vm.currentAct == ACTS.edit ? 'updateApplication' : 'appSave'
          api(operationApiList[url], {
            systemId: vm.$route.query.id,
            ...vm.modalForm
          }).then(res => {
            if (res.data.errcode === 0) {
              vm.$Message.success({
                content: '操作成功'
              })
              vm.showModal = false
              vm.search()
            }
          }, error => {console.log(error)})
        }
      })
    },
    // 启用
    openOrClose (id, status) {
      const vm = this
      vm.$Modal.confirm({
        title: '确认提示',
        content: `确定${status === 1 ? '启用' : '禁用'}吗？`,
        onOk: () => {
          api(operationApiList.appUpdate, {
            id,
            status
          }).then(res => {
            if (res.data.errcode === 0) {
              vm.$Message.success({
                content: '操作成功'
              })
              vm.search()
            }
          }, error => {console.log(error)})
        }
      })
    },
    // 点击发布新版本
    publishNewVertion () {
      const vm = this
      vm.currentAct = ACTS.create
      vm.showModal = true
    },
    // 点击详情
    showDeails (id) {
      const vm = this
      vm.currentAct = ACTS.detail
      vm.detailModal = true
      api(operationApiList.getApplition, {
        id
      }).then(res => {
        if (res.data.errcode === 0) {
          vm.modalDetail = res.data.data
        }
      }).catch(e => {console.log(e)})
    },
    fileUploadSuccess(resp, file, fileList) {
      if (resp.errcode == 0) {
        this.modalForm.files = fileList
        this.$refs.modalForm.validateField('files', res => {})
      } else {
        this.$Message.info(resp.errmsg)
      }
    }
  },
  mounted () {
    this.search()
    // this.getHostName()
  },
  watch: {
    showModal () {
      // 关闭时清空文件数据
      if (this.showModal === false) {
        this.$nextTick(() => {
          this.$refs.fileUpload.clearFiles()
          this.$refs.modalForm.resetFields()
          this.modalForm.remark = ''
        })
      }
    }
  }
}
</script>

<style lang='less'>
.access-upgrade{
  span.handle{
    margin: 0 5px;
    display: inline-block;
  }
}
</style>
