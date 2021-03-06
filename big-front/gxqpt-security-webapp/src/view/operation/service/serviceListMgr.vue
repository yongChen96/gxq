<template>
  <Layout>
    <Content>
      <Breadcrumb>
        <BreadcrumbItem>运维服务管理</BreadcrumbItem>
        <BreadcrumbItem>服务目录管理</BreadcrumbItem>
      </Breadcrumb>
      <Card>
        <Row>
          <Col span="24">
            <Form
              ref="formValidate"
              inline
              :label-width="100"
              :model="searchData.name">
              <FormItem label="服务目录：">
                <Input
                  type="text"
                  v-model="searchData.name">
                </Input>
              </FormItem>
              <FormItem :label-width="20">
                <Button type="primary" @click="getSearchTree">查询</Button>
              </FormItem>
            </Form>
          </Col>
        </Row>
        <Row>
          <Col span="6">
            <Tree :data="ztreeData" :render="renderContent" id="serviceList"></Tree>
          </Col>
          <Col span="16">
            <template v-if="currentNodeInfo.level === 4">
              <Form :model="currentNodeInfo" :label-width="100">
                <FormItem label="服务名称：">
                  <Row>
                    <Col span="24">
                        <Input :value="currentNodeInfo.serviceName" disabled></Input>
                    </Col>
                  </Row>
                </FormItem>
                <FormItem label="服务简介：">
                  <Row>
                    <Col span="24">
                        <Input type="textarea" :value="currentNodeInfo.serviceInfo" :autosize="{minRows: 5, maxRows: 5}" disabled></Input>
                    </Col>
                  </Row>
                </FormItem>
                <FormItem label="服务成果：">
                  <Row>
                    <Col span="24">
                        <Input type="textarea" :value="currentNodeInfo.serviceResult" :autosize="{minRows: 5, maxRows: 5}" disabled></Input>
                    </Col>
                  </Row>
                </FormItem>
              </Form>
            </template>
          </Col>
        </Row>
      </Card>
      <Modal v-model="modal" :title="actTitle" :mask-closable="false">
        <Form ref="modalForm" inline :label-width="90" :rules="ruleValidate" :model="modalFormData">
          <!-- 不是添加最后一级或者编辑最后一级 -->
          <template v-if="notOperateLastLevel">
            <FormItem :label="inputLabel" prop="type" style="width: 100%;">
              <Input type="text" v-model="modalFormData.type" :maxlength="100" placeholder="限制输入100个字符以内"></Input>
            </FormItem>
          </template>
          <template v-else>
            <FormItem label="服务名称" prop="serviceName" style="width: 100%;">
              <Input type="text" v-model="modalFormData.serviceName" :maxlength="100" placeholder="限制输入100个字符以内"></Input>
            </FormItem>
            <FormItem label="服务成果" prop="serviceResult" style="width: 100%;">
              <Input type="textarea"
              v-model="modalFormData.serviceResult"
              :rows="5"
              :maxlength="1000" placeholder="限制输入1000个字符以内"
              :autosize="{minRows: 3, maxRows: 5}"></Input>
            </FormItem>
            <FormItem label="服务简介" style="width: 100%;" prop="serviceInfo">
              <Input
                type="textarea"
                v-model="modalFormData.serviceInfo"
                :rows="5"
                :maxlength="1000" placeholder="限制输入1000个字符以内"
                :autosize="{minRows: 3, maxRows: 5}">
              </Input>
            </FormItem>
          </template>
        </Form>
        <div slot="footer">
          <Button type="default" @click="modal = false" size="large">取消</Button>
          <Button type="primary" @click="handleSubmit" size="large">确定</Button>
        </div>
      </Modal>
    </Content>
  </Layout>
</template>

<script>
import {mapState} from 'vuex'
import api from '@/api/axiosApi'
import {changeTreeData} from '@/assets/js/utils'
import operationApiList from '@/api/operationApiList'

// 操作类型
const actTypes = {
  edit: 'edit',
  create: 'create'
}

export default {
  data() {
    return {
      searchData: {
        name: ''
      },
      // 树的所有节点数组
      treeRoot: [],
      // 当前操作的节点对象，包含父节点的nodekey
      currentNode: {},
      actTypes,
      modal: false,
      // 当前操作的类型
      currentAct: 'edit',
      // 当前操作的节点的信息
      currentNodeInfo: {
        // 自生的id
        id: '',
        // 父节点的id，如果没有父节点就为-1
        parentId: ''
      },
      // 新增或者编辑时的表单
      modalFormData: {
        type: '',
        serviceName: '',
        serviceResult: '',
        serviceInfo: ''
      },
      ruleValidate: {
        type: [{required: true, message: '不可为空', trigger: 'blur'}],
        serviceName: [{required: true, message: '不可为空', trigger: 'blur'}],
        serviceInfo: [{required: true, message: '不可为空', trigger: 'blur'}]
      },
      ztreeData: []
    }
  },
  computed: {
    actTitle () {
      return this.currentAct === actTypes.edit ? '编辑分类' : '新增分类'
    },
    saveOrUpdateUrl() {
      return this.currentAct === actTypes.create ? 'serviceSave' : 'serviceUpdate'
    },
    inputLabel () {
      return ['', '服务类别', '服务对象', '服务分类'][this.currentNodeInfo.level + 1 || 0]
    },
    // 不是操作最后一级？
    notOperateLastLevel () {
      // 新建，但是level不是3，因此不是最后一级，因为只能添加子类
      if (this.currentAct === actTypes.create && this.currentNodeInfo.level !== 3) {
        return true
      }
      // 编辑，但是level不是4，因此不是编辑最后一级
      if (this.currentAct === actTypes.edit && this.currentNodeInfo.level !== 4) {
        return true
      }
      return false
    },
    ...mapState(['authButton'])
  },
  methods: {
    renderContent(h, { root, node, data }) {
      const vm = this
      const defaultBtnStyle = {
        marginRight: '8px'
      }
      const buttonProps = {
        type: 'ghost',
        size: 'small',
      }
      // 添加按钮
      const createBtn = h('Button', {
        props: {
          icon: 'plus',
          ...buttonProps
        },
        style: {
          color: 'green',
          ...defaultBtnStyle
        },
        on: {
          click: () => {
            this.treeRoot = root
            this.currentNode = node
            this.currentNodeInfo = data
            this.currentAct = actTypes.create
            this.openModal()
          }
        }
      })
      // 编辑按钮
      const editBtn = h('Button', {
        props: {
          icon: 'edit',
          ...buttonProps
        },
        style: {
          color: 'blue',
          ...defaultBtnStyle
        },
        on: {
          click: () => {
            this.treeRoot = root
            this.currentNode = node
            this.currentNodeInfo = data
            this.currentAct = actTypes.edit
            this.openModal()
          }
        }
      })
      // 删除按钮
      const deleteBtn = h('Button', {
        props: {
          icon: 'close',
          ...buttonProps
        },
        style: {
          color: 'red',
          ...defaultBtnStyle
        },
        on: {
          click: () => {
            this.treeRoot = root
            this.currentNode = node
            this.currentNodeInfo = data
            this.serviceDelete(data.id)
          }
        }
      })
      let title = data.title
      const btns = new Set()
      if (vm.authButton.includes('ops_server_manage_add')) {
        btns.add(createBtn)
      }
      if (vm.authButton.includes('ops_server_manage_edit')) {
        btns.add(editBtn)
      }
      if (vm.authButton.includes('ops_server_manage_delete')) {
        btns.add(deleteBtn)
      }
      // 如果有子节点，那就不能删除
      if (data.children && data.children.length > 0) {
        btns.delete(deleteBtn)
      }
      // 最后一级不能添加
      if (data.level === 4) {
        btns.delete(createBtn)
        title = data.serviceName
      }
      return h('span', {
        attrs: {
          class: 'doc-tree-title'
        }
      }, [
        h('span', {
          on: {
            click: () => {
              // 点击文字也可以展开树结构
              Vue.set(data, 'expand', !data.expand)
              this.currentNodeInfo = data
            }
          },
          attrs: {
            class: 'title'
          }
        }, title),
        h('span', {
          attrs: {
            class: 'act-btns-container'
          }
        }, [...btns])
      ])
    },
    // 关键字查询
    getSearchTree() {
      const vm = this
      api(operationApiList.serviceTree,{id: '', serviceName: vm.searchData.name, timestamp: Date.now()})
      .then(res => {
        if (res.data.errcode === 0) {
          const rootNode = [{
            children: res.data.data,
            title: '服务目录管理',
            level: 0,
            id: '-1',
            expand: true
          }]
          const ztreeData = JSON.parse(JSON.stringify(rootNode).replace(/type/g, 'title'))
          if (vm.searchData.name) {
            vm.setExpand(ztreeData[0])
          }
          vm.ztreeData = ztreeData
        }
      }, error => {console.log(error)})
    },
    setExpand (data) {
      data.expand = true
      if (typeof data.children === 'object') {
        data.children.map(item => {
          this.setExpand(item)
        })
      }
    },
    // 获取树结构,isAct代表是不是操作之后的刷新
    getTreeData(isAct, node) {
      const vm = this
      api(operationApiList.serviceTree, {
        id: '',
        serviceName: '',
        timestamp: Date.now()
      }).then(res => {
        if (res.data.errcode === 0) {
          // 操作--新增
          if (isAct) {
            const opts = {
              root: vm.treeRoot,
              node: vm.currentNode,
              data: vm.currentNodeInfo,
              nodeInfo: node,
              name: 'type',
              type: 'createChild'
            }
            // 根据新增的是第几层来决定显示哪个字段,此处默认为type
            if (node.level == 4) {
              opts.name = 'serviceName'
            }
            changeTreeData(opts)
          } else {
            const rootNode = [{
              children: res.data.data,
              title: '服务目录管理',
              level: 0,
              id: '-1',
              expand: true
            }]
            this.ztreeData = JSON.parse(JSON.stringify(rootNode).replace(/type/g, 'title'))
          }
        }
      }, error => {console.log(error)})
    },
    // 保存或者更新节点信息
    saveOrUpdate() {
      const vm = this
      const {parentId, id, level} = {...vm.currentNodeInfo}
      let data = {
        sort: 1,
        ...vm.modalFormData
      }
      // 编辑
      if (vm.currentAct === actTypes.edit) {
        data.id = id
        data.parentId = parentId
        data.level = level
      } else {
        // 新增子类
        data.parentId = id
        data.level = level + 1
        // if (vm.modalFormData.type === '1') {
        //   data.parentId = id
        // } else {
        //   data.parentId = parentId
        // }
      }
      api(operationApiList[vm.saveOrUpdateUrl], data)
      .then(res => {
        if (res.data.errcode === 0) {
          // 编辑
          if (vm.currentAct === actTypes.edit) {
            const opts = {
              root: vm.treeRoot,
              node: vm.currentNode,
              data: vm.currentNodeInfo,
              nodeInfo: res.data.data,
              type: 'edit',
              name: 'type'
            }
            if (res.data.data.level == 4) {
              vm.currentNodeInfo.serviceName = res.data.data.serviceName
              vm.currentNodeInfo.serviceInfo = res.data.data.serviceInfo
              vm.currentNodeInfo.serviceResult = res.data.data.serviceResult
              opts.name = 'serviceName'
            }
            changeTreeData(opts)
          } else {
            vm.getTreeData(true, res.data.data)
          }
          vm.modal = false
        }
      }, error => {console.log(error)})
    },
    handleSubmit () {
      const vm = this
      vm.$refs.modalForm.validate(valid => {
        if (valid) {
          vm.saveOrUpdate()
        } else {
          this.$Message.error('表单验证失败!')
        }
      })
    },
    // 删除节点信息
    serviceDelete(id) {
      const vm = this
      vm.$Modal.confirm({
        title: '删除确认',
        content: '确认删除吗？',
        onOk: () => {
          api(operationApiList.serviceDelete, {
            id: id
          }).then(res => {
            if (res.data.errcode === 0) {
              // vm.currentNodeInfo = []
              vm.$Message.success('删除成功！')
              changeTreeData({
                root: vm.treeRoot,
                node: vm.currentNode,
                data: vm.currentNodeInfo,
                type: 'delete'
              })
              vm.modal = false
              vm.currentNodeInfo.level = 3
            }
          }, error => {console.log(error)})
        }
      })
    },
    // 打开表单，并且初始化
    openModal() {
      this.modal = true
      this.$refs.modalForm.resetFields()
      // 编辑
      if (this.currentAct === actTypes.edit) {
        this.modalFormData = {
          type: this.currentNodeInfo.title,
          serviceName: this.currentNodeInfo.serviceName,
          serviceResult: this.currentNodeInfo.serviceResult,
          serviceInfo: this.currentNodeInfo.serviceInfo
        }
      }
    }
  },
  mounted() {
    this.getTreeData()
  }
}
</script>

<style type="text/css" scoped>
.w168 {
  width: 168px;
}
</style>

<style lang="less">
#serviceList{
  .doc-tree-title{
    display: inline-block;
    width: 100%;
    vertical-align: middle;
    .title{
      display: inline-block;
      height: 24px;
      float: left;
      line-height: 24px;
      cursor: pointer;
    }
    .act-btns-container{
      display: none;
      margin-left: 10px;
    }
    &:hover{
      .act-btns-container{
        display: inline-block;
      }
    }
  }
}

</style>
