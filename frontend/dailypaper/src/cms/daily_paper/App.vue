<script setup>
import {Singleton, getInstance} from "@/framework/services/Singleton.js";

import {DateTimeUtil} from "@/framework/utils/DateTimeUtil.js";

import CpDatePicker from '@/cms/daily_paper/ui/components/CpDatePicker.vue'
import CpDateRangePicker from "@/cms/daily_paper/ui/components/CpDateRangePicker.vue";
import CpCard from '@/cms/daily_paper/ui/components/CpCard.vue'
import CpPie from '@/cms/daily_paper/ui/components/CpPie.vue'

import {SysDaily} from '@/cms/daily_paper/system/SysDaily.js'
import {t} from "@/framework/services/LocaleService";
import {ExcelService} from "@/framework/services/ExcelService";

let commits = ref(null);

// https://www.axios-http.cn/docs/cancellation
let getAllCtrl = new AbortController();
let editCtrl = new AbortController();
let exportAllCtrl = new AbortController();
let exportOneCtrl = new AbortController();

let beginTimestamp = null;
let endTimestamp = null;

let selectedDate = ref(0);

let params = new URLSearchParams(window.location.search);
let account = params.get('username');
let curAccount = ref(account);

// 默认饼图legend都选中
let selectedLegend = ref({
    [t('cms.daily_paper.UN_ATTEND')]: true,
    [t('cms.daily_paper.ATTEND')]: true,
});
let loading = ref(false);
let departmentTitle = ref('');
let departmentId = ref(0);

function onDateChanged(date) {
    let sec = date / 1000;
    Singleton.getInstance(SysDaily).RequestGetAll(account, sec, getAllCtrl.signal, () => {
        loading.value = true;
    }, (r) => {
        loading.value = false;

        // 触发响应式UI刷新
        refreshCommits();
        departmentTitle.value = Singleton.getInstance(SysDaily)._departmentName;
        departmentId.value = Singleton.getInstance(SysDaily)._departmentId;
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
        commits.value = Singleton.getInstance(SysDaily).GetSelfCommits(curAccount.value);
    } else if (attend || unAttend) {
        commits.value = Singleton.getInstance(SysDaily).GetAttendList(attend);
    } else {
        commits.value = [];
    }
}

function onEdit(userId, cardAccount, oldContent, content, oldTomorrowPlan, tomorrowPlan) {
    if (curAccount.value && (curAccount.value !== cardAccount)) {
        window.alert('只能提交自己的日报内容');
        return;
    }

    if (content != null && content.trim() !== "") {
        if (content === oldContent && oldTomorrowPlan === tomorrowPlan) {
            //window.alert('提交内容无改动');
            ElMessage({
                showClose: true,
                message: '提交内容无改动',
                type: 'error',
                center: true,
                duration: 2000,
            });
            return;
        }

        Singleton.getInstance(SysDaily).RequestEdit(selectedDate.value, userId, content, tomorrowPlan, editCtrl.signal, () => {
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
    } else {
        // window.alert('提交内容不能为空');
        ElMessage({
            showClose: true,
            message: '编辑内容不能为空',
            type: 'warning',
            center: true,
            duration: 2000,
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
        ExcelService.ExportAOAToExcel1(final, r.data.colNames, 'export_all', true);
    });
}

onMounted(() => {
    /* 因为未onMounted之前，组件不会触发事件，所以需要手动触发*/
    onDateChanged(DateTimeUtil.nowDate());
});

// 清理定时器，事件监听器，异步函数
onUnmounted(() => {
    getAllCtrl.abort();
    editCtrl.abort();
    exportAllCtrl.abort();
    exportOneCtrl.abort();
});
</script>

<template>
    <div class="root">
        <CpDatePicker @onDateChanged="onDateChanged" :targetDate="DateTimeUtil.nowDate()"/>
        <a v-if="departmentId === 30015" href="https://www.kdocs.cn/l/cgHAbfHG8Fm2?from=docs&startTime=1724384203679" target="_blank"
           style="position: absolute; left: 260px; top: 30px; color: white; background: #0000FF; border-radius: 50%;">考勤表</a>
        <!--cp_chart 没有搞懂这里没有ref的响应式代码，为什么也能即时刷新-->
        <div style="display: flex; position: relative; left: 340px;  align-items: center;">
            <CpPie @onLegendSelectChanged="onLegendSelectChanged" :attand="Singleton.getInstance(SysDaily).GetAttendCount(true)"
                :unAttand="Singleton.getInstance(SysDaily).GetAttendCount(false)" :selected="selectedLegend"/>
            <span style="font-size: 60px; font-style: italic; color: #a0cfff; margin-left: 100px; height: 160px; width:
            580px;
                overflow: hidden; white-space: nowrap; padding-top: 20px; align-items: center;">{{ departmentTitle }}</span>
        </div>
        <CpDateRangePicker @onDateRangeChanged="onDateRangeChanged"/>
        <el-button @click="onExportAll" v-cd-s="3" circle :dark="true" type="warning" style="position: absolute; right: 30px; top: 30px">导出
        </el-button>
        
        <div class="infinite-list-root" v-loading="loading">
            <CpCard v-for="card in commits"
                    :key="card.userId"
                    :date="selectedDate"
                    :curAccount="curAccount"
                    :id="card.userId"
                    :name="card.name"
                    :account="card.account"
                    :tomorrowPlan="card.tomorrowPlan"
                    :time="card.time"
                    :content="card.content"
                    @onEdit="onEdit"
            />
        </div>
    </div>
</template>

<style>
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
    left: 0;
    right: 0;

    height: 750px;
    width: 100vw;
    padding: 0;
    margin: 0;

    display: grid;
    grid-template-columns: repeat(6, 300px);

    overflow: auto;
    justify-content: space-evenly;
}
</style>