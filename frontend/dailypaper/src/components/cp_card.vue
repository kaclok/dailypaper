<script lang="ts" setup>
import {ref} from 'vue'
import {
    Check as ep_check, // 按钮上的图标，如果不导入就没有图标只有按钮
} from '@element-plus/icons-vue'

import timeUtil from "@/utils/date_time_util.js";

const props = defineProps(["date", "id", "name", "content"]);
defineEmits(['onEdit']);
const refTextContent = ref(props.content);

// 能否编辑：只有今天能编辑， 之前日期的不能编辑
function isToday() {
    let midNight = props.date;
    let todayMidNight = timeUtil.nowDate().getTime() / 1000;
    return midNight >= todayMidNight;
}

function hasContent() {
    return props.content != null && props.content.trim() !== "";
}

</script>

<template>
    <el-card style="width: 280px; height: 700px">
        <template #header>
            <div>
                <span>{{ props.name }} {{ hasContent() }} {{ isToday() }}</span>
            </div>
        </template>

        <!-- 非空状态 -->
        <el-input v-if="(isToday() || (!isToday() && hasContent()))"
                  v-model="refTextContent"
                  style="width: 240px"
                  :rows="6"
                  type="textarea"
                  :placeholder="props.content"
                  clearable
                  resize="none"
                  :disabled="!isToday()"
        />

        <!-- 空状态 -->
        <el-empty v-if="(!isToday() && !hasContent())" style="width:280px; height:150px; z-index:3000;"
                  description="未填写内容"/>

        <el-button @click="$emit('onEdit', props.id, refTextContent)" v-if="isToday()" type="success"
                   circle :dark="true">提交</el-button>
    </el-card>
</template>

<style scoped>
</style>
