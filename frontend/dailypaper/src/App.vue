<script setup>
// 需要的组件：
// 1、滚动列表 https://element-plus.org/zh-CN/component/infinite-scroll.html
//      滚动条 https://element-plus.org/zh-CN/component/scrollbar.html
// 2、通知 https://element-plus.org/zh-CN/component/notification.html
// 3、空状态 https://element-plus.org/zh-CN/component/empty.html
// 4、输入框 文本域 https://element-plus.org/zh-CN/component/input.html
// 5、日期选择器 https://element-plus.org/zh-CN/component/date-picker.html
// 6、按钮 https://element-plus.org/zh-CN/component/button.html
import {onMounted, ref} from "vue";

import {ElMessage} from 'element-plus'
import apiDaily from '#/daily.js'
import timeUtil from "@/utils/date_time_util.js";
import timeService from "@/services/time_service.js";

import cp_date_picker from './components/cp_date_picker.vue'
import cp_card from '$/cp_card.vue'

let info = ref(null);
let selectedDate = ref(0);
let loading = ref(false);

const requestGetAll = async function (date) {
    loading.value = true;
    let rlt = await apiDaily.GetAll(date);
    loading.value = false;
    if (rlt.data.result) {
        info.value = rlt.data.data;
    }

    timeService.initTime(rlt.data.timestamp);
}

const requestEdit = async function (date, userId, content) {
    loading.value = true;
    let rlt = await apiDaily.Edit(date, userId, content);
    loading.value = false;

    if (rlt.data.result) {
        for (let i = 0; i < info.value.commits.length; i++) {
            let cur = info.value.commits[i];
            if (cur.userId === userId) {
                cur.content = content;
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

function getUserCount() {
    if (info.value == null) {
        return 13;
    }
    return info.value.total;
}

onMounted(() => {
    /* 因为未onMounted之前，组件不会触发事件，所以需要手动触发*/
    onDateChanged(timeUtil.nowDate());
});
</script>

<template>
    <div>
        <cp_date_picker @onDateChanged="onDateChanged" :targetDate="timeUtil.nowDate()"/>
        <ul class="infinite-list" style="overflow: auto; justify-content: flex-start; display: flex;"
            v-loading="loading">
            <cp_card v-for="i in getUserCount()" class="infinite-list-item"
                     :key="i"
                     :date="selectedDate"
                     :id="getOne(i).userId"
                     :name="getOne(i).name"
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