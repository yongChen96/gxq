<template>
    <Card :bordered="false">
        <p slot="title">审核详情</p>
        <Form ref="evalData" :model="serverData" :label-width="100" class="clearfix">
            <Row :gutter="40">
                <Col span="6">
                    <FormItem label="处理结果：" prop="results">
                        <!-- <Select v-model="serverData.status" :disabled='changeDisabled'>
                            <Option value="1">同意</Option>
                            <Option value="2">不同意</Option>
                        </Select> -->
                        <Input :value="serverData.status == 2 ? '驳回' : '同意'" :disabled='changeDisabled'></Input>
                    </FormItem>
                </Col>
                <Col span="24">
                    <FormItem label="处理意见：" label-position="left" prop="opinion">
                        <Input v-model="serverData.auditOpinion" type="textarea" :autosize="{minRows: 2,maxRows: 5}" style="resize:none;" :disabled='changeDisabled'></Input>
                    </FormItem>
                </Col>
                <template v-if="serverData.status != 2">
                    <Col span="12">
                        <FormItem label="处理人：" prop="man">
                            <Input v-model="serverData.dealUser" :disabled='changeDisabled'></Input>
                        </FormItem>
                    </Col>
                    <Col span="12">
                        <FormItem label="联系方式：" prop="contact">
                            <Input v-model="serverData.mainmobile" :disabled='changeDisabled'></Input>
                        </FormItem>
                    </Col>
                    <Col span="24">
                        <FormItem label="主要工作内容：" prop="content">
                            <Input v-model="serverData.content" type="textarea" :autosize="{minRows: 3,maxRows: 5}" style="resize:none;" :disabled='changeDisabled'></Input>
                        </FormItem>
                    </Col>
                    <Col span="24">
                        <FormItem label="协助人：" prop="helpMan">
                            <Input v-model="serverData.assistant" :disabled='changeDisabled'></Input>
                        </FormItem>
                    </Col>
                </template>
            </Row>
        </Form>
    </Card>
</template>

<script>
import api from '@/api/axiosApi'
import operationApiList from '@/api/operationApiList'
export default {
    props: ['serverData'],
    data() {
        return{
            changeDisabled: true,
            systemName: '',
        }
    }
}
</script>

<style scoped>
.ivu-form-item >>> .ivu-form-item-content{
    height:auto;
}
.ivu-input-wrapper >>> textarea{
    resize: none;
}
</style>
