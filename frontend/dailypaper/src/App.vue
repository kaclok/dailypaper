<script setup>
import {onMounted, ref, onUnmounted} from "vue";

import {ElMessage} from 'element-plus'
import TimeUtil from "@/utils/DateTimeUtil.js";

import CpDatePicker from './components/CpDatePicker.vue'
import CpCard from './components/CpCard.vue'
import CpPie from './components/CpPie.vue'

import DailyLogic from '@/logic/DailyLogic.js'
import I18n from "@/config/I18n.js";

let commits = ref(null);
let getAllCtrl = new AbortController();
let editCtrl = new AbortController();

let selectedDate = ref(0);
// 默认饼图legend都选中
let selectedLegend = ref({
    [I18n.ATTEND]: true,
    [I18n.UN_ATTEND]: true,
});
let loading = ref(false);

function onDateChanged(date) {
    let sec = date / 1000;
    DailyLogic.RequestGetAll(sec, getAllCtrl.signal, () => {
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
    let attend = selectedLegend.value[I18n.ATTEND];
    let unAttend = selectedLegend.value[I18n.UN_ATTEND];
    // 更新commits
    if (attend && unAttend) {
        commits.value = DailyLogic.GetCommits();
    } else if (attend || unAttend) {
        commits.value = DailyLogic.GetAttendList(attend);
    } else {
        commits.value = [];
    }
}

function onEdit(userId, content) {
    if (content != null && content.trim() !== "") {
        DailyLogic.RequestEdit(selectedDate.value, userId, content, editCtrl.signal, () => {
            loading.value = true;
        }, (r) => {
            loading.value = false;

            if (r) {
                // 因为commits.value监听dailyLogic.result.date.commits, 每次edit修改某个commit之后也会
                // 触发commits.value的响应式UI刷新
                ElMessage({
                    showClose: false,
                    message: '编辑成功',
                    type: 'success',
                });
            } else {
                ElMessage({
                    showClose: false,
                    message: '编辑失败',
                    type: 'error',
                })
            }
        });
    } else {
        ElMessage({
            showClose: false,
            message: '编辑内容不能为空',
            type: 'warning',
        });
    }
}

onMounted(() => {
    /* 因为未onMounted之前，组件不会触发事件，所以需要手动触发*/
    onDateChanged(TimeUtil.nowDate());
});

// 清理定时器，事件监听器，异步函数
onUnmounted(() => {
    getAllCtrl.abort();
    editCtrl.abort();
});
</script>

<template>
    <CpDatePicker @onDateChanged="onDateChanged" :targetDate="TimeUtil.nowDate()"/>
    <!--cp_chart 没有搞懂这里没有ref的响应式代码，为什么也能即时刷新-->
    <CpPie @onLegendSelectChanged="onLegendSelectChanged" :attand="DailyLogic.GetAttendCount(true)"
           :unAttand="DailyLogic.GetAttendCount(false)" :selected="selectedLegend"/>
    <ul class="infinite-list" style="overflow: auto; justify-content: flex-start; display: flex;"
        v-loading="loading">
        <CpCard v-for="card in commits" class="infinite-list-item"
                :key="card.userId"
                :date="selectedDate"
                :id="card.userId"
                :name="card.name"
                :time="card.time"
                :content="card.content"
                @onEdit="onEdit"/>
    </ul>
</template>

<style>
.infinite-list {
    height: 300px;
    padding: 0;
    margin: 0;
    list-style: none;
}

.infinite-list .infinite-list-item {
}

.infinite-list .infinite-list-item + .list-item {
    margin-top: 10px;
}
</style>