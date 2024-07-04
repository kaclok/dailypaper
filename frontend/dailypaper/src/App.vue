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
import apiDaily from '@/api/daily.js'
import timeUtil from "@/utils/timeutil.js";

import cp_date_picker from './components/cp_date_picker.vue'
import cp_card from './components/cp_card.vue'

const info = ref("");

const requestGetAll = async function (date) {
  let rlt = await apiDaily.GetAll(date);
  if (rlt.data.result) {
    info.value = rlt.data.date;
  }
}

const requestEdit = async function (date, userId, content) {
  let rlt = await apiDaily.Edit(date, userId, content);
  if (rlt.data.result) {

  }
}

function onDateChanged(date) {
  let sec = date / 1000;
  console.log('onDateChanged:' + sec);
  requestGetAll(sec);
}

onMounted(() => {
  /* 因为未onMounted之前，组件不会触发事件，所以需要手动触发*/
  onDateChanged(timeUtil.nowDate());
});
</script>

<template>
  <div>
    <cp_date_picker @onDateChanged="onDateChanged" :targetDate="timeUtil.nowDate()"/>
  </div>
</template>
