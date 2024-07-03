<script lang="ts" setup>

import zhCn from "element-plus/es/locale/lang/zh-cn";

const model = defineModel("selectedDate");

</script>

<template>
  <div class="date-picker-root">
    <div>Value：{{ model / 1000 }}</div>
    <div class="date-picker">
      <el-config-provider :locale="zhCn">
        <el-date-picker
            v-model="model"
            type="date"
            placeholder="选择日期"
            format="YYYY/MM/DD"
            value-format="x"
            size="small"
        />
      </el-config-provider>
    </div>
  </div>
</template>

<style scoped>
.date-picker-root {
  display: flex;
  width: 100%;
  padding: 0;
  flex-wrap: wrap;
}

.date-picker-root .date-picker {
  padding: 30px 0;
  text-align: center;
  border-right: solid 1px var(--el-border-color);
  flex: 1;
}

.date-picker-root .date-picker:last-child {
  border-right: none;
}
</style>
