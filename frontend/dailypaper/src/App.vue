<script setup>
import {onMounted, ref, onUnmounted} from "vue";

import {ElMessage} from 'element-plus'
import timeUtil from "@/utils/date_time_util.js";

import cp_date_picker from './components/cp_date_picker.vue'
import cp_card from '$/cp_card.vue'
import cp_chart from '$/cp_chart.vue'

import dailyLogic from '@/logic/dailyLogic.js'
import i18n from "@/config/i18n.js";

let commits = ref(null);
let getAllCtrl = new AbortController();
let editCtrl = new AbortController();

let selectedDate = ref(0);
let selectedLegend = ref([0, 1]);
let loading = ref(false);

function onDateChanged(date) {
    let sec = date / 1000;
    dailyLogic.RequestGetAll(sec, getAllCtrl.signal, () => {
        loading.value = true;
    }, (r) => {
        loading.value = false;

        // 触发响应式UI刷新
        commits.value = dailyLogic.GetCommits();
    });

    selectedDate.value = sec;
}

function onLegendSelectChanged(params) {
    // 出勤是否选中
    selectedLegend.value[0] = params.selected[i18n.ATTEND] === true ? 0 : -1;
    // 缺勤是否选中
    selectedLegend.value[1] = params.selected[i18n.UN_ATTEND] === true ? 1 : -1;
}

function onEdit(userId, content) {
    if (content != null && content.trim() !== "") {
        dailyLogic.RequestEdit(selectedDate.value, userId, content, editCtrl.signal, () => {
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
    onDateChanged(timeUtil.nowDate());
});

// 清理定时器，事件监听器，异步函数
onUnmounted(() => {
    getAllCtrl.abort();
    editCtrl.abort();
});

function getOne(id) {
    if (commits.value == null || commits.value.length <= 0) {
        return {
            userId: 0,
            name: "",
            content: null,
        };
    }

    return commits.value.find((ele) => {
        return ele.userId === id;
    });
}
</script>

<template>
    <div>
        <cp_date_picker @onDateChanged="onDateChanged" :targetDate="timeUtil.nowDate()"/>
        <cp_chart @onLegendSelectChanged="onLegendSelectChanged" :attand="dailyLogic.GetAttendCount(true)"
                  :unAttand="dailyLogic.GetAttendCount(false)"/>
        <ul class="infinite-list" style="overflow: auto; justify-content: flex-start; display: flex;"
            v-loading="loading">
            <cp_card v-for="i in dailyLogic.GetTotalCount()" class="infinite-list-item"
                     :key="i"
                     :date="selectedDate"
                     :id="getOne(i).userId"
                     :name="getOne(i).name"
                     :time="getOne(i).time"
                     :content="getOne(i).content"
                     @onEdit="onEdit"/>
        </ul>
    </div>
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