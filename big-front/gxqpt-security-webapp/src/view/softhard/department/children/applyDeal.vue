<!-- 申请处理 -->
<template>
    <div class="ivu-layout serviceDetail">
        <h2>申请处理</h2>
        <unitApplyinfo style="margin-top:30px;"></unitApplyinfo>
        <Row class="bottom-bar" style="text-align: center;background:#fff;">
            <Col>
                <div class="btnList">
                    <Button type="default" @click="goback(-1)">返回</Button>
                    <router-link to="/server/allocatio">
                        <Button type="primary">配置资源</Button>
                    </router-link>
                    <router-link to="/server/resource">
                        <Button type="primary">资源不足</Button>
                    </router-link>
                </div>
            </Col>
        </Row>
    </div>
</template>

<script>
import unitApplyinfo from './../../common/unitApplyinfo'
export default {
    data(){
        return{
        }
    },
    methods:{
        goback(n){
            this.$router.go(n)
        },
    },
    components: {
        unitApplyinfo,
    },
}
</script>

<style lang='less' scoped>
.serviceDetail{
    background:#eee;
    padding: 20px;
    width:80%;
    height:auto;
}
.bottom-bar{
    margin-top:0;
}
.btnList{
    padding:10px;
    button{
        margin:0 20px;
    }
}
</style>
