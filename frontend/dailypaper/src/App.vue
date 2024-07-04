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

import {ElNotification as ep_notification} from 'element-plus'
import apiDaily from '#/daily.js'
import timeUtil from "@/utils/date_time_util.js";

import cp_date_picker from './components/cp_date_picker.vue'
import cp_card from '$/cp_card.vue'

let info = null;

const requestGetAll = async function (date) {
    let rlt = await apiDaily.GetAll(date);
    if (rlt.data.result) {
        info = rlt.data.date;
    }
}

const requestEdit = async function (date, userId, content) {
    let rlt = await apiDaily.Edit(date, userId, content);
    if (rlt.data.result) {

    }
}

function onDateChanged(date) {
    let sec = date / 1000;
    requestGetAll(sec);
}

function onEdit(userId, content) {
    if (content != null && content.trim() !== "") {
        requestEdit(info.date, userId, content);
    } else {
        ep_notification({
            title: '警告',
            message: '编辑内容不能为空',
            type: 'warning',
            duration: 3000,
            position: 'bottom-left',
        })
    }
}

function getOne(id) {
    return {
        id: id,
        name: "user:" + id,
        content: (id % 2 !== 0 ? "content: " + id : null),
        time: (id % 2 === 0 ? 0 : 1720076930),
        /*time: 1720076930,*/
    }
}

function getUserCount() {
    if (info == null) {
        return 13;
    }
    return info.total;
}

onMounted(() => {
    /* 因为未onMounted之前，组件不会触发事件，所以需要手动触发*/
    onDateChanged(timeUtil.nowDate());
});
</script>

<template>
    <div>
        <cp_date_picker @onDateChanged="onDateChanged" :targetDate="timeUtil.nowDate()"/>
        <ul class="infinite-list" style="overflow: auto; justify-content: flex-start; display: flex;">
            <cp_card v-for="i in getUserCount()" class="infinite-list-item" :key="i" :id="getOne(i).id"
                     :name="getOne(i).name"
                     :content="getOne(i).content" :time="getOne(i).time" @onEdit="onEdit"/>
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