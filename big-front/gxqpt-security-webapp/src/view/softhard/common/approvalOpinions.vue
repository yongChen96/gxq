<!-- 意见审批组件 -->
<template>
    <Card :bordered="false" class="marginTop">
        <p slot="title">审批意见：</p>
        <div class="comment" style="text-align:center">
            <ul>
                <li v-for="recordsResItem in applyRecordsRes"><span>{{recordsResItem.createTime}} </span><span>{{recordsResItem.applyUname}}</span><span v-if="recordsResItem.passFlag=='1'">已同意。同意意见：</span><span v-if="recordsResItem.passFlag=='2'">不同意。不同意意见：</span><span>{{recordsResItem.apprOpinion}}</span></li>
            </ul>
        </div>
    </Card>
</template>

<script>
import api from '@/api/axiosApi'
import softhardApiList from '@/api/softhardApiList'
export default {
    data(){
        return{
            applyRecordsRes: [],
        }
    },
    mounted(){
        if(this.$route.params.departData){
            this.getApplyRecordsByApplyId();
        }
    },
    methods:{
        getApplyRecordsByApplyId() { // 根据ApplyId查询审批记录
            api(softhardApiList.getApplyRecordsByApplyId, {
                "keyid": this.$route.params.departData.id
            }).then((res) => {
                if(res.status == 200 && res.data.data) {
                    this.applyRecordsRes = res.data.data;
                }
            }, (err) => {
                //dong something...
            })
        },
    }
}
</script>

<style lang="less" scoped>
.comment {
    ul {
        list-style: none;
        li {
            text-align: left;
        }
    }
}
</style>
