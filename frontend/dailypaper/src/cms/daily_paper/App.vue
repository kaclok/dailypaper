<script setup>
import {Singleton} from "@/framework/services/Singleton.js";
import {SessionStorageService} from "@/framework/services/SessionStorageService.js";

import {DateTimeUtil} from "@/framework/utils/DateTimeUtil.js";

import CpDatePicker from '@/cms/daily_paper/ui/components/CpDatePicker.vue'
import CpDateRangePicker from "@/cms/daily_paper/ui/components/CpDateRangePicker.vue";
import CpCard from '@/cms/daily_paper/ui/components/CpCard.vue'
import CpPie from '@/cms/daily_paper/ui/components/CpPie.vue'

import {SysDaily} from '@/cms/daily_paper/system/SysDaily.js'
import {t} from "@/framework/services/LocaleService";
import {ExcelService} from "@/framework/services/ExcelService";
import axios from "axios";
import {TokenService} from "@/framework/services/TokenService.js";

let canMounted = false;
let account = SessionStorageService.getStore("Account");
if (__DEV__) {
    account = "SMLJ23659" // SMLJ19030
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

// https://www.axios-http.cn/docs/cancellation
let getAllCtrl = new AbortController();
let editCtrl = new AbortController();
let exportAllCtrl = new AbortController();
let exportOneCtrl = new AbortController();

let beginTimestamp = null;
let endTimestamp = null;

let selectedDate = ref(0);
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
            refreshCommits();
            weeklyPlan.value = Singleton.getInstance(SysDaily)._tableList;
            departmentTitle.value = Singleton.getInstance(SysDaily)._departmentName;
            departmentId.value = Singleton.getInstance(SysDaily)._departmentId;
        }
    });

    selectedDate.value = sec;
}

function onDateRangeChanged(dateRange) {
    console.log("onDateRangeChanged: " + dateRange[0] + "," + dateRange[1]);
    beginTimestamp = Math.round(dateRange[0] / 1000);
    endTimestamp = Math.round(dateRange[1] / 1000);
}

function onLegendSelectChanged(params) {
    selectedLegend.value = params.selected;

    refreshCommits();
}

function refreshCommits() {
    let attend = selectedLegend.value[t('cms.daily_paper.ATTEND')];
    let unAttend = selectedLegend.value[t('cms.daily_paper.UN_ATTEND')];
    // 更新commits
    if (attend && unAttend) {
        dailyPlan.value = Singleton.getInstance(SysDaily).GetSelfCommits(curAccount.value);
    } else if (attend || unAttend) {
        dailyPlan.value = Singleton.getInstance(SysDaily).GetAttendList(attend);
    } else {
        dailyPlan.value = [];
    }
}

function onEdit(userId, cardAccount, oldContent, content, oldTomorrowPlan, tomorrowPlan, oldTomorrowArrangement, tomorrowArrangement) {
    /*if (curAccount.value && (curAccount.value !== cardAccount)) {
        window.alert('只能提交自己的日报内容');
        return;
    }*/

    if ((content === undefined || content === null || content.trim() === "") &&
        (tomorrowArrangement === undefined || tomorrowArrangement === null || tomorrowArrangement.trim() === "")) {
        ElMessage({
            showClose: true,
            message: '今日工作内容 和 明日工作安排 至少填写一个',
            type: 'warning',
            center: true,
            duration: 2000,
        });

    } else {
        if (content === oldContent && oldTomorrowPlan === tomorrowPlan && oldTomorrowArrangement === tomorrowArrangement) {
            ElMessage({
                showClose: true,
                message: '提交内容无改动',
                type: 'error',
                center: true,
                duration: 2000,
            });
            return;
        }

        Singleton.getInstance(SysDaily).RequestEdit(selectedDate.value, userId, content, tomorrowPlan, tomorrowArrangement, editCtrl.signal, () => {
            loading.value = true;
        }, (r) => {
            loading.value = false;

            if (r) {
                // 因为commits.value监听dailyLogic.result.date.commits, 每次edit修改某个commit之后也会
                // 触发commits.value的响应式UI刷新
                // window.alert('编辑成功');
                ElMessage({
                    showClose: true,
                    message: '编辑成功',
                    type: 'success',
                    center: true,
                    duration: 2000,
                });
            } else {
                // window.alert('提交失败');
                ElMessage({
                    showClose: true,
                    message: '编辑失败',
                    type: 'error',
                    center: true,
                    duration: 2000,
                })
            }
        });
    }
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
    editCtrl.abort();
    exportAllCtrl.abort();
    exportOneCtrl.abort();

    SessionStorageService.removeStore("AuthCode");
});

function onClickDelete(rowIndex, row) {
    weeklyPlan.value.splice(rowIndex, 1)
}

function onClickSave(rowIndex, row) {

}

function onAddItem() {
    weeklyPlan.value.push({});
}

function tableRowClassName(row, rowIndex) {
    if (rowIndex === 1) {
        return 'warning-row'
    } else if (rowIndex === 3) {
        return 'success-row'
    }
    return ''
}

function onCellEnter(row, column, cell, event) {
    row.isEdit = true
}

function onCellLeave(row, column, cell, event) {
    row.isEdit = false
}

</script>

<template>
    <div class="root">
        <CpDatePicker @onDateChanged="onDateChanged" :targetDate="DateTimeUtil.nowDate()"/>
        <a v-if="departmentId === '1030016010' || departmentId === '30015'"
           href="https://www.kdocs.cn/l/cgOq47yrcANr" target="_blank"
           style="position: absolute; left: 260px; top: 30px; color: white; background: #0000FF; border-radius: 50%;">考勤表</a>
        <!--cp_chart 没有搞懂这里没有ref的响应式代码，为什么也能即时刷新-->
        <div style="display: flex; position: relative; left: 340px;  align-items: center;">
            <CpPie @onLegendSelectChanged="onLegendSelectChanged" :attand="Singleton.getInstance(SysDaily).GetAttendCount(true)"
                   :unAttand="Singleton.getInstance(SysDaily).GetAttendCount(false)" :selected="selectedLegend"/>

            <span style="font-size: 70px; color: #a0cfff; margin-left: 100px; height: 160px; width:
            580px;
                overflow: hidden; white-space: nowrap; padding-top: 20px; align-items: center;">{{ departmentTitle }}
            </span>
        </div>
        <CpDateRangePicker @onDateRangeChanged="onDateRangeChanged"/>
        <el-button @click="onExportAll" v-cd="3" circle :dark="true" type="warning" style="position: absolute; right: 30px; top: 30px">导出
        </el-button>

        <!--        <div style="width: 100%">
                    &lt;!&ndash; https://element-plus.org/zh-CN/component/table.html &ndash;&gt;
                    <el-table :data="tableList"
                              height="200"
                              max-height="400"
                              stripe
                              border
                              :row-class-name="tableRowClassName"
                              @cell-mouse-enter="onCellEnter"
                              @cell-mouse-leave="onCellLeave"
                              style="">
                        &lt;!&ndash; <el-table-column fixed type="selection" width="40"/> &ndash;&gt;
                        <el-table-column fixed type="index" label="序号" width="54"/>
                        &lt;!&ndash; <el-table-column fixed prop="id" label="id" width="60"/> &ndash;&gt;
                        <el-table-column prop="dutyPerson" label="责任人" width="90">
                            &lt;!&ndash;                    <template slot-scope="scope">
                                                    <el-input v-if="scope.row.isEdit" class="item" v-model="scope.row.dutyPerson" placeholder="请输入责任人"></el-input>
                                                    <div v-else class="txt">{{ scope.row.dutyPerson }}</div>
                                                </template>&ndash;&gt;
                        </el-table-column>
                        <el-table-column prop="finishTime" label="完成时间" sortable width="110">
                            &lt;!&ndash;                    <template slot-scope="scope">
                                                    <el-input v-if="scope.row.isEdit" class="item" v-model="scope.row.finishTime" placeholder="请输入完成时间"></el-input>
                                                    <div v-else class="txt">{{ scope.row.finishTime }}</div>
                                                </template>&ndash;&gt;
                        </el-table-column>
                        <el-table-column prop="content" label="工作内容" show-overflow-tooltip width="1360">
                            &lt;!&ndash;                    <template slot-scope="scope">
                                                    <el-input v-if="scope.row.isEdit" class="item" v-model="scope.row.content" placeholder="请输入工作内容"></el-input>
                                                    <div v-else class="txt">{{ scope.row.content }}</div>
                                                </template>&ndash;&gt;
                        </el-table-column>

                        <el-table-column prop="comment" label="备注" width="80">
                            &lt;!&ndash;                    <template slot-scope="scope">
                                                    <el-input v-if="scope.row.isEdit" class="item" v-model="scope.row.comment" placeholder="请输入备注"></el-input>
                                                    <div v-else class="txt">{{ scope.row.comment }}</div>
                                                </template>&ndash;&gt;
                        </el-table-column>

                        <el-table-column fixed="right" label="操作" min-width="66">
                            <template #default="scope">
                                <el-button link type="primary" :disabled="!Singleton.getInstance(SysDaily)._curUserIsLeader" size="small"
                                           @click.prevent="onClickSave(scope.$index, scope.row)">保存
                                </el-button>
                                <el-button link type="danger" :disabled="!Singleton.getInstance(SysDaily)._curUserIsLeader" size="small"
                                           @click.prevent="onClickDelete(scope.$index, scope.row)">删除
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>

                    <el-button :disabled="!Singleton.getInstance(SysDaily)._curUserIsLeader" style="font-size: 15px; width: 100%"
                               @click="onAddItem">添加
                    </el-button>
                </div>-->

        <div class="infinite-list-root" v-loading="loading">
            <CpCard v-for="card in dailyPlan"
                    :key="card.userId"
                    :date="selectedDate"
                    :curAccount="curAccount"
                    :id="card.userId"
                    :name="card.name"
                    :account="card.account"
                    :tomorrowPlan="card.tomorrowPlan"
                    :tomorrowArrangement="card.tomorrowArrangement"
                    :time="card.time"
                    :content="card.content"
                    :isLeader="card.isLeader"
                    @onEdit="onEdit"
            />
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