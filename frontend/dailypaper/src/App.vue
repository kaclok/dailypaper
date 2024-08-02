<script setup>
import {Singleton, getInstance} from "@/framework/services/Singleton.js";

import {DateTimeUtil} from "@/framework/utils/DateTimeUtil.js";

import CpDatePicker from '@/logic/ui/components/CpDatePicker.vue'
import CpCard from '@/logic/ui/components/CpCard.vue'
import CpPie from '@/logic/ui/components/CpPie.vue'

import {DailyLogic} from '@/logic/system/DailyLogic.js'
import {I18N} from "@/config/I18N.js";

let commits = ref(null);

// https://www.axios-http.cn/docs/cancellation
let getAllCtrl = new AbortController();
let editCtrl = new AbortController();

let selectedDate = ref(0);

let params = new URLSearchParams(window.location.search);
let account = params.get('username');
let curAccount = ref(account);

// 默认饼图legend都选中
let selectedLegend = ref({
    [I18N.ATTEND]: true,
    [I18N.UN_ATTEND]: true,
});
let loading = ref(false);

function onDateChanged(date) {
    let sec = date / 1000;
    Singleton.getInstance(DailyLogic).RequestGetAll(sec, getAllCtrl.signal, () => {
        loading.value = true;
    }, (r) => {
        loading.value = false;

        // 触发响应式UI刷新
        refreshCommits();
    });

    selectedDate.value = sec;
}

function onLegendSelectChanged(params) {
    selectedLegend.value = params.selected;

    refreshCommits();
}

function refreshCommits() {
    let attend = selectedLegend.value[I18N.ATTEND];
    let unAttend = selectedLegend.value[I18N.UN_ATTEND];
    // 更新commits
    if (attend && unAttend) {
        commits.value = Singleton.getInstance(DailyLogic).GetCommits();
    } else if (attend || unAttend) {
        commits.value = Singleton.getInstance(DailyLogic).GetAttendList(attend);
    } else {
        commits.value = [];
    }
}

function onEdit(userId, cardAccount, oldContent, content) {
    if (curAccount.value && (curAccount.value !== cardAccount)) {
        window.alert('只能提交自己的日报内容');
        return;
    }

    if (content != null && content.trim() !== "") {
        if (content === oldContent) {
            window.alert('提交内容无改动');
            /*            ElMessage({
                            showClose: false,
                            message: '提交内容无改动',
                            type: 'error',
                            center: true,
                            duration: 300000,
                        });*/
            return;
        }

        Singleton.getInstance(DailyLogic).RequestEdit(selectedDate.value, userId, content, editCtrl.signal, () => {
            loading.value = true;
        }, (r) => {
            loading.value = false;

            if (r) {
                // 因为commits.value监听dailyLogic.result.date.commits, 每次edit修改某个commit之后也会
                // 触发commits.value的响应式UI刷新
                // window.alert('编辑成功');
                /*ElMessage({
                    showClose: false,
                    message: '编辑成功',
                    type: 'success',
                    center: true,
                    duration: 2000,
                });*/
            } else {
                window.alert('提交失败');
                /*ElMessage({
                    showClose: false,
                    message: '编辑失败',
                    type: 'error',
                    center: true,
                    duration: 2000,
                })*/
                return;
            }
        });
    } else {
        window.alert('提交内容不能为空');
        /*ElMessage({
            showClose: false,
            message: '编辑内容不能为空',
            type: 'warning',
            center: true,
            duration: 2000,
        });*/
        return;
    }
}

onMounted(() => {
    /* 因为未onMounted之前，组件不会触发事件，所以需要手动触发*/
    onDateChanged(DateTimeUtil.nowDate());
});

// 清理定时器，事件监听器，异步函数
onUnmounted(() => {
    getAllCtrl.abort();
    editCtrl.abort();
});
</script>

<template>
    <div class="root">
        <CpDatePicker @onDateChanged="onDateChanged" :targetDate="DateTimeUtil.nowDate()"/>
        <!--cp_chart 没有搞懂这里没有ref的响应式代码，为什么也能即时刷新-->
        <CpPie @onLegendSelectChanged="onLegendSelectChanged" :attand="Singleton.getInstance(DailyLogic).GetAttendCount(true)"
               :unAttand="Singleton.getInstance(DailyLogic).GetAttendCount(false)" :selected="selectedLegend"/>
        <!--        <span style="font-size: 40px; color: #a0cfff;">数字化中心日报</span>-->
        <div class="infinite-list-root" v-loading="loading">
            <CpCard v-for="card in commits"
                    :key="card.userId"
                    :date="selectedDate"
                    :curAccount="curAccount"
                    :id="card.userId"
                    :name="card.name"
                    :account="card.account"
                    :time="card.time"
                    :content="card.content"
                    @onEdit="onEdit"/>
        </div>
    </div>
</template>

<style>
.root {
    background: url("@/assets/imgs/bg-smlj.jpg") no-repeat center border-box;

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
    top: 30px;
    left: 0;
    right: 0;

    height: 600px;
    width: 100vw;
    padding: 0;
    margin: 0;

    display: grid;
    grid-template-columns: repeat(5, 300px);

    overflow: auto;
    justify-content: space-evenly;
}
</style>