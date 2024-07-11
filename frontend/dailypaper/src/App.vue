<script setup>
import {onMounted, ref, onUnmounted} from "vue";

import {ElMessage} from 'element-plus'
import apiDaily from '#/daily.js'
import timeUtil from "@/utils/date_time_util.js";
import timeService from "@/services/time_service.js";

import cp_date_picker from './components/cp_date_picker.vue'
import cp_card from '$/cp_card.vue'
import cp_chart from '$/cp_chart.vue'

let getAllCtrl = new AbortController();
let editCtrl = new AbortController();
let info = ref(null);
let selectedDate = ref(0);
let loading = ref(false);

const requestGetAll = async function (date) {
    loading.value = true;
    let rlt = await apiDaily.GetAll(date, getAllCtrl.signal);
    loading.value = false;
    if (rlt.data.result && date === rlt.data.data.date) {
        // 网络消息回来之后，如果和之前的info.value没有区别，则不会触发UI响应式刷新
        info.value = rlt.data.data;
    }

    // 同步时间
    timeService.initTime(rlt.data.timestamp);
}

const requestEdit = async function (date, userId, content) {
    loading.value = true;
    let rlt = await apiDaily.Edit(date, userId, content, getAllCtrl.signal);
    loading.value = false;

    // 同步时间
    timeService.initTime(rlt.data.timestamp);

    if (rlt.data.result) {
        for (let i = 0; i < info.value.commits.length; i++) {
            let cur = info.value.commits[i];
            if (cur.userId === userId) {
                cur.content = content;
                cur.time = timeService.getSvrTime();
                break;
            }
        }

        ElMessage({
            showClose: false,
            message: '编辑成功',
            type: 'success',
        })
    } else {
        ElMessage({
            showClose: false,
            message: '编辑失败',
            type: 'error',
        })
    }
}

function onDateChanged(date) {
    let sec = date / 1000;
    requestGetAll(sec);

    selectedDate.value = sec;
}

function onEdit(userId, content) {
    if (content != null && content.trim() !== "") {
        requestEdit(selectedDate.value, userId, content);
    } else {
        ElMessage({
            showClose: false,
            message: '编辑内容不能为空',
            type: 'warning',
        })
    }
}

function getOne(id) {
    if (info.value == null || info.value.commits == null || info.value.commits.length <= 0) {
        return {
            userId: id,
            name: "",
            content: null,
        };
    }

    let rlt = info.value.commits.find((ele) => {
        return ele.userId === id;
    });
    return rlt;
}

function getTotal() {
    if (info.value == null) {
        return 13;
    }
    return info.value.total;
}

function getAttandCount() {
    let cnt = 0;
    if (info.value != null) {
        for (let i = 0; i < info.value.commits.length; i++) {
            let one = info.value.commits[i];
            if (one.time !== 0) {
                ++cnt;
            }
        }
    }
    return cnt;
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
</script>

<template>
    <div>
        <cp_date_picker @onDateChanged="onDateChanged" :targetDate="timeUtil.nowDate()"/>
        <cp_chart :attand="getAttandCount()" :unAttand="getTotal() - getAttandCount()"/>
        <ul class="infinite-list" style="overflow: auto; justify-content: flex-start; display: flex;"
            v-loading="loading">
            <cp_card v-for="i in getTotal()" class="infinite-list-item"
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