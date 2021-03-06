package com.hengyunsoft.platform.supervise.repository.example;

import java.io.Serializable;
import java.util.Date;

public class BizAppSuperviseExample extends com.hengyunsoft.db.example.BaseExample<BizAppSuperviseExample.Criteria> implements Serializable {
    //------------- 手动新增字段，请写在此后面 -------------
    private static final long serialVersionUID = 1L;

    public abstract static class GeneratedCriteria extends com.hengyunsoft.db.example.BaseGeneratedCriteria implements Serializable {

        public GeneratedCriteria() {
            super();
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(java.util.List<java.lang.Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(java.util.List<java.lang.Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNull() {
            addCriterion("app_id is null");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNotNull() {
            addCriterion("app_id is not null");
            return (Criteria) this;
        }



        public Criteria andAppIdEqualTo(String value) {
            addCriterion("app_id =", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLike(String value) {
            addCriterion("app_id like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotLike(String value) {
            addCriterion("app_id not like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andSuperviseTypeIsNull() {
            addCriterion("supervise_type is null");
            return (Criteria) this;
        }

        public Criteria andSuperviseTypeIsNotNull() {
            addCriterion("supervise_type is not null");
            return (Criteria) this;
        }

        public Criteria andSuperviseTypeEqualTo(Short value) {
            addCriterion("supervise_type =", value, "superviseType");
            return (Criteria) this;
        }

        public Criteria andSuperviseTypeNotEqualTo(Short value) {
            addCriterion("supervise_type <>", value, "superviseType");
            return (Criteria) this;
        }

        public Criteria andSuperviseTypeGreaterThan(Short value) {
            addCriterion("supervise_type >", value, "superviseType");
            return (Criteria) this;
        }

        public Criteria andSuperviseTypeGreaterThanOrEqualTo(Short value) {
            addCriterion("supervise_type >=", value, "superviseType");
            return (Criteria) this;
        }

        public Criteria andSuperviseTypeLessThan(Short value) {
            addCriterion("supervise_type <", value, "superviseType");
            return (Criteria) this;
        }

        public Criteria andSuperviseTypeLessThanOrEqualTo(Short value) {
            addCriterion("supervise_type <=", value, "superviseType");
            return (Criteria) this;
        }

        public Criteria andSuperviseTypeIn(java.util.List<java.lang.Short> values) {
            addCriterion("supervise_type in", values, "superviseType");
            return (Criteria) this;
        }

        public Criteria andSuperviseTypeNotIn(java.util.List<java.lang.Short> values) {
            addCriterion("supervise_type not in", values, "superviseType");
            return (Criteria) this;
        }

        public Criteria andSuperviseTypeBetween(Short value1, Short value2) {
            addCriterion("supervise_type between", value1, value2, "superviseType");
            return (Criteria) this;
        }

        public Criteria andSuperviseTypeNotBetween(Short value1, Short value2) {
            addCriterion("supervise_type not between", value1, value2, "superviseType");
            return (Criteria) this;
        }

        public Criteria andAppIdIn(java.util.List<java.lang.String> values) {
            addCriterion("app_id in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotIn(java.util.List<java.lang.String> values) {
            addCriterion("app_id not in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andOnlineLimitIsNull() {
            addCriterion("online_limit is null");
            return (Criteria) this;
        }

        public Criteria andOnlineLimitIsNotNull() {
            addCriterion("online_limit is not null");
            return (Criteria) this;
        }

        public Criteria andOnlineLimitEqualTo(Integer value) {
            addCriterion("online_limit =", value, "onlineLimit");
            return (Criteria) this;
        }

        public Criteria andOnlineLimitGreaterThan(Integer value) {
            addCriterion("online_limit >", value, "onlineLimit");
            return (Criteria) this;
        }

        public Criteria andOnlineLimitGreaterThanOrEqualTo(Integer value) {
            addCriterion("online_limit >=", value, "onlineLimit");
            return (Criteria) this;
        }

        public Criteria andOnlineLimitLessThan(Integer value) {
            addCriterion("online_limit <", value, "onlineLimit");
            return (Criteria) this;
        }

        public Criteria andOnlineLimitLessThanOrEqualTo(Integer value) {
            addCriterion("online_limit <=", value, "onlineLimit");
            return (Criteria) this;
        }

        public Criteria andOnlineLimitIn(java.util.List<java.lang.Integer> values) {
            addCriterion("online_limit in", values, "onlineLimit");
            return (Criteria) this;
        }

        public Criteria andOnlineLimitNotIn(java.util.List<java.lang.Integer> values) {
            addCriterion("online_limit not in", values, "onlineLimit");
            return (Criteria) this;
        }

        public Criteria andOnlineLimitBetween(Integer value1, Integer value2) {
            addCriterion("online_limit between", value1, value2, "onlineLimit");
            return (Criteria) this;
        }

        public Criteria andDutyUserIsNull() {
            addCriterion("duty_user is null");
            return (Criteria) this;
        }

        public Criteria andDutyUserIsNotNull() {
            addCriterion("duty_user is not null");
            return (Criteria) this;
        }

        public Criteria andDutyUserEqualTo(String value) {
            addCriterion("duty_user =", value, "dutyUser");
            return (Criteria) this;
        }

        public Criteria andDutyUserLike(String value) {
            addCriterion("duty_user like", value, "dutyUser");
            return (Criteria) this;
        }

        public Criteria andDutyUserNotLike(String value) {
            addCriterion("duty_user not like", value, "dutyUser");
            return (Criteria) this;
        }

        public Criteria andDutyUserIn(java.util.List<java.lang.String> values) {
            addCriterion("duty_user in", values, "dutyUser");
            return (Criteria) this;
        }

        public Criteria andDutyUserNotIn(java.util.List<java.lang.String> values) {
            addCriterion("duty_user not in", values, "dutyUser");
            return (Criteria) this;
        }

        public Criteria andConfirmUserIsNull() {
            addCriterion("confirm_user is null");
            return (Criteria) this;
        }

        public Criteria andConfirmUserIsNotNull() {
            addCriterion("confirm_user is not null");
            return (Criteria) this;
        }

        public Criteria andConfirmUserEqualTo(String value) {
            addCriterion("confirm_user =", value, "confirmUser");
            return (Criteria) this;
        }

        public Criteria andConfirmUserLike(String value) {
            addCriterion("confirm_user like", value, "confirmUser");
            return (Criteria) this;
        }

        public Criteria andConfirmUserNotLike(String value) {
            addCriterion("confirm_user not like", value, "confirmUser");
            return (Criteria) this;
        }

        public Criteria andConfirmUserIn(java.util.List<java.lang.String> values) {
            addCriterion("confirm_user in", values, "confirmUser");
            return (Criteria) this;
        }

        public Criteria andConfirmUserNotIn(java.util.List<java.lang.String> values) {
            addCriterion("confirm_user not in", values, "confirmUser");
            return (Criteria) this;
        }

        public Criteria andCopyUserIsNull() {
            addCriterion("copy_user is null");
            return (Criteria) this;
        }

        public Criteria andCopyUserIsNotNull() {
            addCriterion("copy_user is not null");
            return (Criteria) this;
        }

        public Criteria andCopyUserEqualTo(String value) {
            addCriterion("copy_user =", value, "copyUser");
            return (Criteria) this;
        }

        public Criteria andCopyUserLike(String value) {
            addCriterion("copy_user like", value, "copyUser");
            return (Criteria) this;
        }

        public Criteria andCopyUserNotLike(String value) {
            addCriterion("copy_user not like", value, "copyUser");
            return (Criteria) this;
        }

        public Criteria andCopyUserIn(java.util.List<java.lang.String> values) {
            addCriterion("copy_user in", values, "copyUser");
            return (Criteria) this;
        }

        public Criteria andCopyUserNotIn(java.util.List<java.lang.String> values) {
            addCriterion("copy_user not in", values, "copyUser");
            return (Criteria) this;
        }

        public Criteria andScheduledTimeIsNull() {
            addCriterion("scheduled_time is null");
            return (Criteria) this;
        }

        public Criteria andScheduledTimeIsNotNull() {
            addCriterion("scheduled_time is not null");
            return (Criteria) this;
        }

        public Criteria andScheduledTimeGreaterThan(Date value) {
            addCriterion("scheduled_time >", value, "scheduledTime");
            return (Criteria) this;
        }

        public Criteria andScheduledTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("scheduled_time >=", value, "scheduledTime");
            return (Criteria) this;
        }

        public Criteria andScheduledTimeLessThan(Date value) {
            addCriterion("scheduled_time <", value, "scheduledTime");
            return (Criteria) this;
        }

        public Criteria andScheduledTimeLessThanOrEqualTo(Date value) {
            addCriterion("scheduled_time <=", value, "scheduledTime");
            return (Criteria) this;
        }

        public Criteria andScheduledTimeBetween(Date value1, Date value2) {
            addCriterion("scheduled_time between", value1, value2, "scheduledTime");
            return (Criteria) this;
        }

        public Criteria andScheduledTimeLessThanOrEqualToFooter(Date value) {
            addCriterion("scheduled_time <=", value, "scheduledTime");
            return (Criteria) this;
        }

        public Criteria andScheduledTimeGreaterThanOrEqualToTop(Date value) {
            addCriterion("scheduled_time >=", value, "scheduledTime");
            return (Criteria) this;
        }

        public Criteria andRunStatusIsNull() {
            addCriterion("run_status is null");
            return (Criteria) this;
        }

        public Criteria andRunStatusIsNotNull() {
            addCriterion("run_status is not null");
            return (Criteria) this;
        }

        public Criteria andRunStatusEqualTo(Boolean value) {
            addCriterion("run_status =", value, "runStatus");
            return (Criteria) this;
        }

        public Criteria andDescIsNull() {
            addCriterion("desc_ is null");
            return (Criteria) this;
        }

        public Criteria andDescIsNotNull() {
            addCriterion("desc_ is not null");
            return (Criteria) this;
        }

        public Criteria andDescEqualTo(String value) {
            addCriterion("desc_ =", value, "desc");
            return (Criteria) this;
        }

        public Criteria andDescLike(String value) {
            addCriterion("desc_ like", value, "desc");
            return (Criteria) this;
        }

        public Criteria andDescNotLike(String value) {
            addCriterion("desc_ not like", value, "desc");
            return (Criteria) this;
        }

        public Criteria andDescIn(java.util.List<java.lang.String> values) {
            addCriterion("desc_ in", values, "desc");
            return (Criteria) this;
        }

        public Criteria andDescNotIn(java.util.List<java.lang.String> values) {
            addCriterion("desc_ not in", values, "desc");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNull() {
            addCriterion("create_user is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNotNull() {
            addCriterion("create_user is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserEqualTo(Long value) {
            addCriterion("create_user =", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThan(Long value) {
            addCriterion("create_user >", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThanOrEqualTo(Long value) {
            addCriterion("create_user >=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThan(Long value) {
            addCriterion("create_user <", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThanOrEqualTo(Long value) {
            addCriterion("create_user <=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserIn(java.util.List<java.lang.Long> values) {
            addCriterion("create_user in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotIn(java.util.List<java.lang.Long> values) {
            addCriterion("create_user not in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserBetween(Long value1, Long value2) {
            addCriterion("create_user between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualToFooter(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualToTop(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIsNull() {
            addCriterion("update_user is null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIsNotNull() {
            addCriterion("update_user is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserEqualTo(Long value) {
            addCriterion("update_user =", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThan(Long value) {
            addCriterion("update_user >", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThanOrEqualTo(Long value) {
            addCriterion("update_user >=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThan(Long value) {
            addCriterion("update_user <", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThanOrEqualTo(Long value) {
            addCriterion("update_user <=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIn(java.util.List<java.lang.Long> values) {
            addCriterion("update_user in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotIn(java.util.List<java.lang.Long> values) {
            addCriterion("update_user not in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserBetween(Long value1, Long value2) {
            addCriterion("update_user between", value1, value2, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualToFooter(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualToTop(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andLevelIsNull() {
            addCriterion("level is null");
            return (Criteria) this;
        }

        public Criteria andLevelIsNotNull() {
            addCriterion("level is not null");
            return (Criteria) this;
        }

        public Criteria andLevelEqualTo(Integer value) {
            addCriterion("level =", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThan(Integer value) {
            addCriterion("level >", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("level >=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThan(Integer value) {
            addCriterion("level <", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThanOrEqualTo(Integer value) {
            addCriterion("level <=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelIn(java.util.List<java.lang.Integer> values) {
            addCriterion("level in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotIn(java.util.List<java.lang.Integer> values) {
            addCriterion("level not in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelBetween(Integer value1, Integer value2) {
            addCriterion("level between", value1, value2, "level");
            return (Criteria) this;
        }
    }

    /**
    b_biz_app_superviseaddClassComment 472
     *
     * @mbggenerated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria implements Serializable {

        public Criteria() {
            super();
        }
    }
}