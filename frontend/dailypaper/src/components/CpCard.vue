<script lang="ts" setup>
import {onBeforeUpdate, ref} from 'vue'
import {
    Check as ep_check, // 按钮上的图标，如果不导入就没有图标只有按钮
} from '@element-plus/icons-vue'

import TimeUtil from "@/utils/DateTimeUtil.js";

const props = defineProps(["date", "id", "name", "time", "content"]);
defineEmits(['onEdit']);

// https://cn.vuejs.org/guide/components/props.html
// props.content只是提供一个初始值，以后refTextContent和prop的更新无关了
let refTextContent = ref(props.content);

// 能否编辑：只有今天能编辑， 之前日期的不能编辑
function isToday() {
    let midNight = props.date;
    let todayMidNight = TimeUtil.nowDate().getTime() / 1000;
    return midNight >= todayMidNight;
}

function hasContent() {
    return props.content != null && props.content.trim() !== "";
}

function hasEdited() {
    return props.time !== 0;
}

onBeforeUpdate(() => {
    // 父组件变更的时候，通知子组件的props, 但是子组件的v-model绑定的不是props, 所以需要在此更新
    refTextContent.value = props.content;
});

</script>

<template>
    <el-card style="width: 280px; height: 700px">
        <template #header>
            <span :style="{color: hasEdited() ? 'blue' : 'red'}">{{ props.name }}</span>
        </template>

        <!-- 非空状态 -->
        <el-input v-if="(isToday() || (!isToday() && hasContent()))"
                  v-model="refTextContent"
                  :rows="7"
                  type="textarea"
                  placeholder="输入日报内容"
                  clearable
                  resize="none"
                  :disabled="!isToday()"

                  style="width: 240px"
        />

        <!-- 空状态 -->
        <el-empty v-if="(!isToday() && !hasContent())" style="width:280px; height:150px; z-index:3000;"
                  description="未填写内容"/>

        <el-button @click="$emit('onEdit', props.id, refTextContent)" v-if="isToday()" type="success"
                   circle :dark="true">提交
        </el-button>
    </el-card>
</template>

<style scoped>
</style>
