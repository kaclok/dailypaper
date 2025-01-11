<script setup>
import {Singleton} from "@/framework/services/Singleton.js";
import {SessionStorageService} from "@/framework/services/SessionStorageService.js";
import {DateTimeUtil} from "@/framework/utils/DateTimeUtil.js";
import CpDatePicker from '@/cms/daily_paper/ui/components/CpDatePicker.vue'
import CpDateRangePicker from "@/cms/daily_paper/ui/components/CpDateRangePicker.vue";

import {SysDaily} from '@/cms/daily_paper/system/SysDaily.js'
import {t} from "@/framework/services/LocaleService";
import {ExcelService} from "@/framework/services/ExcelService";
import axios from "axios";

let canMounted = false;
let account = SessionStorageService.getStore("Account");
if (__DEV__) {
    account = "SMLJ19030" // SMLJ19030 // SMLJ20012
}
console.log("account: " + account);
if (account === null) {
    // window.location.href
    let params = new URLSearchParams(window.location.search);
    let authCode = params.get('code');

    onGotAuthCode(authCode, onGotToken);
} else {
    canMounted = true;
}

// http://10.8.54.110:8790/auth/authorize?response_type=code&scope=openid&client_id=dailypaper&redirect_uri=http://10.8.54.127:5175
/*function reqAuthCode(onSuccess) {

    axios.get("http://10.8.54.110:8790/auth/authorize", {
        params: {
            response_type: "code",
            scope: "openid",
            client_id: "dailypaper",
            redirect_uri: "http://10.8.54.127:5175"
        },
    }).then((response) => {
        onSuccess(response);
    }).catch((error) => {
        console.log("reqAuthCode: " + error);
    })
}*/
function onGotAuthCode(authCode, onSuccess) {
    axios.post("http://10.8.54.110:8790/auth/token", {}, {
        params: {
            code: authCode,
            grant_type: "authorization_code",
            client_id: "dailypaper",
            redirect_uri: "http://10.8.54.127:5175"
        }
    }).then((response) => {
        console.table(response.data);
        if (response.data.status === 0) {
            onSuccess(response);
        } else {
            if (account === null) {
                account = "SMLJ23659";

                // 暂时屏蔽，让所有人都可以编辑日报，否则只能"SMLJ23659"可以编辑
                // curAccount = ref(account);
            }

            _onMounted();
        }
    })
}

function onGotToken(r) {
    let headers = {
        'Content-Type': 'application/json',
        "Authorization": `${r.data.token_type} ${r.data.access_token}`
    }

    axios.post("http://10.8.54.110:8790/auth/userinfo/v2", {}, {headers: headers}).then((response) => {
        account = response.data.data.account;
        curAccount = ref(account);

        sessionStorage.setItem("Account", account);
        console.log("onGotToken: " + account);

        _onMounted();
    })
}

let weeklyPlan = ref([]);
let dailyPlan = ref([]);
let people = ref([]);
let freedPeople = ref([])
let curIsLeader = ref(false);

// https://www.axios-http.cn/docs/cancellation
let exportAllCtrl = new AbortController();
let getAllCtrl = new AbortController();

let editDailyCtrl = new AbortController();
let editWeeklyCtrl = new AbortController();
let deleteWeeklyCtrl = new AbortController();

let beginTimestamp = null;
let endTimestamp = null;

let selectedDate = ref(0);
let weekBeginDate = ref(new Date());
let weekEndDate =  ref(new Date());
let curAccount = ref(account);

// 默认饼图legend都选中
let selectedLegend = ref({
    [t('cms.daily_paper.UN_ATTEND')]: true,
    [t('cms.daily_paper.ATTEND')]: true,
});
let loading = ref(false);
let departmentTitle = ref('');
let departmentId = ref("");

function onDateChanged(date) {
    // TokenService.getRemoteAT()

    let sec = date / 1000;
    Singleton.getInstance(SysDaily).RequestGetAll(account, sec, getAllCtrl.signal, () => {
        loading.value = true;
    }, (r) => {
        loading.value = false;

        if (r) {
            // 触发响应式UI刷新
            weeklyPlan.value = Singleton.getInstance(SysDaily)._weeklyPlan;
            dailyPlan.value = Singleton.getInstance(SysDaily)._dailyPlan;
            people.value = Singleton.getInstance(SysDaily)._people;
            freedPeople.value = Singleton.getInstance(SysDaily).getFreedPeople();

            departmentTitle.value = Singleton.getInstance(SysDaily)._departmentName;
            departmentId.value = Singleton.getInstance(SysDaily)._departmentId;
            curIsLeader.value = Singleton.getInstance(SysDaily)._curUserIsLeader;
        }
    });

    selectedDate.value = sec;
    weekBeginDate.value = DateTimeUtil.getWeekBegin(sec, 0);
    weekEndDate.value = DateTimeUtil.getWeekBegin(sec, 7);
}

function onDateRangeChanged(dateRange) {
    console.log("onDateRangeChanged: " + dateRange[0] + "," + dateRange[1]);
    beginTimestamp = Math.round(dateRange[0] / 1000);
    endTimestamp = Math.round(dateRange[1] / 1000);
}

function onExportAll() {
    if (beginTimestamp == null || endTimestamp == null) {
        // window.alert('请选择导出时间区间');
        ElMessage({
            showClose: true,
            message: '请选择导出时间区间',
            type: 'warning',
            center: true,
            duration: 2000,
        });
        return;
    }

    Singleton.getInstance(SysDaily).RequestExportAll(beginTimestamp, endTimestamp, exportAllCtrl.signal, () => {
        loading.value = true;
    }, (r) => {
        loading.value = false;

        let final = r.data.rows.map((ele) => {
            let d = DateTimeUtil.toDateTime(ele.time);
            d = DateTimeUtil.formatDate(d);
            let arr = Object.values(ele.contents);
            arr.unshift(d);
            return arr;
        });
        ExcelService.ExportAOAToExcel1(final, r.data.colNames, 'export_all', false);
    });
}

function _onMounted() {
    /!* 因为未onMounted之前，组件不会触发事件，所以需要手动触发*!/
    onDateChanged(DateTimeUtil.nowDate());
}

onMounted(() => {
    if (canMounted) {
        _onMounted();
    }
});

// 清理定时器，事件监听器，异步函数
onUnmounted(() => {
    getAllCtrl.abort();
    editDailyCtrl.abort();
    editWeeklyCtrl.abort();
    deleteWeeklyCtrl.abort();
    exportAllCtrl.abort();

    SessionStorageService.removeStore("AuthCode");
});

function onClickWeeklySave(rowIndex, row) {
    let sec = DateTimeUtil.toTimestamp(row.finishTime);
    if (!row.dutyPerson || !sec) {
        ElMessage({
            showClose: true,
            message: '责任人或者完成时间没有选择',
            type: 'warning',
            center: true,
            duration: 2000,
        });
        return;
    }

    const index = people.value.findIndex(person => person.userName === row.dutyPerson);
    let userId = null
    if (index !== -1) {
        userId = people.value[index].userId
    }

    Singleton.getInstance(SysDaily).RequestEditWeeklyPlan(selectedDate.value, userId, row.content, sec, row.comment,
        editWeeklyCtrl.signal, () => {
            loading.value = true;
        }, (r) => {
            loading.value = false;
            if (r) {
                // 因为commits.value监听dailyLogic.result.date.commits, 每次edit修改某个commit之后也会
                // 触发commits.value的响应式UI刷新
                // window.alert('编辑成功');
                ElMessage({
                    showClose: true,
                    message: '保存成功',
                    type: 'success',
                    center: true,
                    duration: 2000,
                });
            } else {
                ElMessage({
                    showClose: true,
                    message: '保存失败',
                    type: 'error',
                    center: true,
                    duration: 2000,
                })
            }
        });
}

function onClickWeeklyDelete(rowIndex, row) {
    weeklyPlan.value.splice(rowIndex, 1);

    if (!row.dutyPerson) {
        return;
    }

    Singleton.getInstance(SysDaily).RequestDeleteWeeklyPlan(selectedDate.value, row.userId, deleteWeeklyCtrl.signal, () => {
        loading.value = true;
    }, (r) => {
        loading.value = false;
        if (r) {
            // 因为commits.value监听dailyLogic.result.date.commits, 每次edit修改某个commit之后也会
            // 触发commits.value的响应式UI刷新
            // window.alert('编辑成功');
            ElMessage({
                showClose: true,
                message: '删除成功',
                type: 'success',
                center: true,
                duration: 2000,
            });
        } else {
            ElMessage({
                showClose: true,
                message: '删除失败',
                type: 'error',
                center: true,
                duration: 2000,
            })
        }
    });
}

function onSelectChanged(row) {
    const index = freedPeople.value.findIndex((item) => {
        return item === row.dutyPerson;
    })
    if(index !== -1) {
        freedPeople.value.splice(index, 1)
    }
}

function onAddWeekly() {
    weeklyPlan.value.push({});
}

function onClickDailySave(rowIndex, row) {
    if (!row.content && !row.tomorrowPlan && !row.tomorrowArrangement) {
        ElMessage({
            showClose: true,
            message: '今日工作内容、明日工作计划、明日工作安排 至少填写一个',
            type: 'warning',
            center: true,
            duration: 2000,
        });
        return;
    }

    Singleton.getInstance(SysDaily).RequestEditDailyPlan(selectedDate.value, row.userId, row.content, row.tomorrowPlan, row.tomorrowArrangement, editDailyCtrl.signal, () => {
        loading.value = true;
    }, (r) => {
        loading.value = false;
        if (r) {
            // 因为commits.value监听dailyLogic.result.date.commits, 每次edit修改某个commit之后也会
            // 触发commits.value的响应式UI刷新
            // window.alert('编辑成功');
            ElMessage({
                showClose: true,
                message: '保存成功',
                type: 'success',
                center: true,
                duration: 2000,
            });
        } else {
            ElMessage({
                showClose: true,
                message: '保存失败',
                type: 'error',
                center: true,
                duration: 2000,
            })
        }
    });
}

function tableRowClassName(row) {
    if (row.row.isLeader) {
        return 'warning-row'
    } else {
        return 'success-row'
    }
}

function isToday() {
    let midNight = selectedDate.value;
    let todayMidNight = DateTimeUtil.nowDate().getTime() / 1000;
    return midNight >= todayMidNight/* - 86400 * 1*/;
}

function isSelf(account) {
    return curAccount.value === account;
}

</script>

<template>
    <div class="root">
        <CpDatePicker @onDateChanged="onDateChanged" :targetDate="DateTimeUtil.nowDate()"/>
        <div style="display: flex; position: relative; left: 540px; align-items: center;">
            <span style="font-size: 70px; color: #a0cfff; margin-left: 100px; height: 120px; width:
            580px;
                overflow: hidden; white-space: nowrap; padding-top: 20px; align-items: center;
                letter-spacing: -100px; animation: expand 2s ease-in-out forwards;">{{ departmentTitle }}
            </span>
        </div>
        <CpDateRangePicker @onDateRangeChanged="onDateRangeChanged"/>
        <el-button @click="onExportAll" v-cd="3" circle :dark="true" type="warning" style="position: absolute; right: 30px; top: 30px">导出
        </el-button>

        <div>
            <div style="width: 100%; height: 100%;">
                <span style="font-size: 20px; color: #a0cfff;"> 本周工作安排({{DateTimeUtil.formatDate(weekBeginDate)}} ->
                    {{DateTimeUtil.formatDate(weekEndDate)}}):
                </span>
                <!-- https://element-plus.org/zh-CN/component/table.html -->
                <el-table :data="weeklyPlan"
                          min-height="200"
                          stripe
                          border
                          :row-class-name="tableRowClassName">
                    <!-- <el-table-column fixed type="selection" width="40"/> -->
                    <el-table-column fixed type="index" label="序号" width="54"/>
                    <!-- <el-table-column fixed prop="id" label="id" width="60"/> -->
                    <el-table-column prop="dutyPerson" label="责任人" width="110">
                        <template #default="scope1">
                            <el-select
                                :disabled="!(curIsLeader && isToday())"
                                class="item"
                                v-model="scope1.row.dutyPerson"
                                placeholder="请选择"
                                @change="onSelectChanged(scope1.row)"
                                size="small"
                                style="width: 80px">
                                <el-option
                                    v-for="x in freedPeople"
                                    :value="x"
                                />
                            </el-select>
                        </template>
                    </el-table-column>
                    <el-table-column prop="finishTime" label="完成时间" sortable width="250">
                        <template #default="scope2">
                            <el-date-picker
                                :disabled="!(curIsLeader && isToday())"
                                type="date" class="item" v-model="scope2.row.finishTime"></el-date-picker>
                        </template>
                    </el-table-column>
                    <el-table-column prop="content" label="工作安排" show-overflow-tooltip width="750">
                        <template #default="scope3">
                            <el-input
                                :disabled="!(curIsLeader && isToday())"
                                type="textarea" autosize class="item" v-model="scope3.row.content" style="width: 720px"
                                placeholder="请输入"></el-input>
                        </template>
                    </el-table-column>

                    <el-table-column prop="comment" label="备注" width="630px">
                        <template #default="scope4">
                            <el-input :disabled="!(curIsLeader && isToday())" style="width: 540px"
                                      type="textarea" autosize class="item" v-model="scope4.row.comment" placeholder="请输入"></el-input>
                        </template>
                    </el-table-column>

                    <el-table-column fixed="right" label="操作" min-width="100">
                        <template #default="scope">
                            <el-button link type="primary" :disabled="!(curIsLeader && isToday())" size="small"
                                       @click.prevent="onClickWeeklySave(scope.$index, scope.row)">保存
                            </el-button>
                            <el-button link type="danger" :disabled="!(curIsLeader && isToday())" size="small"
                                       @click.prevent="onClickWeeklyDelete(scope.$index, scope.row)">删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>

                <el-button-group style="width: 100%;">
                    <!--                    <el-button :disabled="!(curIsLeader && isToday())" style="font-size: 15px; width: 50%"
                                                   @click="onAddWeekly">添加
                                        </el-button>
                                        <el-button :disabled="!(curIsLeader && isToday())" style="font-size: 15px; width: 50%"
                                                   @click="onSaveWeekly">保存
                                        </el-button>-->

                    <el-button :disabled="!(curIsLeader && isToday())" style="font-size: 15px; width: 100%"
                               @click="onAddWeekly">添加
                    </el-button>
                </el-button-group>
            </div>

            <div style="width: 100%; height: 100%;">
                <span style="font-size: 20px; color: #a0cfff;"> 每日工作内容: </span>
                <!-- https://element-plus.org/zh-CN/component/table.html -->
                <el-table
                    :data="dailyPlan"
                    border
                    :row-class-name="tableRowClassName">
                    <el-table-column fixed type="index" label="序号" width="54"/>
                    <el-table-column prop="name" label="姓名" width="75">
                    </el-table-column>
                    <el-table-column prop="account" label="账户" width="100">
                    </el-table-column>
                    <el-table-column prop="content" label="今日工作内容" width="510">
                        <template #default="scope2">
                            <el-input :disabled="!(isSelf(scope2.row.account) && isToday())" type="textarea" autosize class="item"
                                      v-model="scope2.row.content"
                                      style="width: 485px"
                                      placeholder="请输入"></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column prop="tomorrowPlan" label="明日工作计划" show-overflow-tooltip width="510">
                        <template #default="scope3">
                            <el-input :disabled="!(isSelf(scope3.row.account) && isToday())" type="textarea" autosize class="item"
                                      v-model="scope3.row.tomorrowPlan" style="width: 475px"
                                      placeholder="请输入"></el-input>
                        </template>
                    </el-table-column>

                    <el-table-column prop="tomorrowArrangement" label="明日工作安排" width="510">
                        <template #default="scope4">
                            <el-input :disabled="!(curIsLeader && isToday())" type="textarea" autosize class="item"
                                      v-model="scope4.row.tomorrowArrangement"
                                      style="width: 485px"
                                      placeholder="请输入"></el-input>
                        </template>
                    </el-table-column>

                    <el-table-column fixed="right" label="操作" min-width="60">
                        <template #default="scope">
                            <el-button :disabled="!((curIsLeader || isSelf(scope.row.account)) && isToday())"
                                       link type="primary" size="small"
                                       @click.prevent="onClickDailySave(scope.$index, scope.row)">保存
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
    </div>
</template>

<style lang='scss'>
.root {
    background: url("@/cms/daily_paper/assets/imgs/bg-smlj.jpg") no-repeat center border-box;
    /* 背景图片全屏化 */
    background-size: cover;

    height: 100vh;
    width: 100vw;

    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    overflow: scroll;
}

.infinite-list-root {
    position: relative;
    top: 6px;
    left: 0;
    right: 0;

    height: 750px;
    width: 100vw;
    padding: 0;
    margin: 0;

    display: grid;
    grid-template-columns: repeat(5, 360px);

    overflow: auto;
    justify-content: space-evenly;
}

.el-table .warning-row {
    --el-table-tr-bg-color: var(--el-color-warning-light-9);
}

.el-table .success-row {
    --el-table-tr-bg-color: var(--el-color-success-light-9);
}

@keyframes expand {
    0% {
        letter-spacing: -100px;
    }
    100% {
        letter-spacing: 0;
    }
}

.item {
    width: 100px;
    /* 调整elementUI中样式 如果不需要调整请忽略 */

    .el-input__inner {
        height: 24px !important;
    }
}

.txt {
    line-height: 24px;
    padding: 0 9px;
    box-sizing: border-box;
}
</style>