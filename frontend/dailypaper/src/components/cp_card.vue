<script setup>
import {ref} from 'vue'
import zhCn from "element-plus/es/locale/lang/zh-cn";

const props = defineProps(["id", "name", "content"]);

</script>

<template>
  <el-card style="max-width: 480px">
    <template #header>
      <div class="card-header">
        <span>{{props.name}}}</span>
      </div>
    </template>
    <div>
      <div v_if="props.content != null">
        <p v-for="o in 4" :key="o" class="text item">{{ 'List item ' + o }}</p>
      </div>
      <el-empty v_if="props.content == null" description="未填写内容" />
    </div>
  </el-card>
</template>

<style scoped>
</style>
