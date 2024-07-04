<script lang="ts" setup>
import {ref} from 'vue'
import {
    Check as ep_check, // 按钮上的图标，如果不导入就没有图标只有按钮
} from '@element-plus/icons-vue'

import date_time_util from "@/utils/date_time_util.js";

const props = defineProps(["id", "name", "content", "time"]);
defineEmits(['onEdit']);
const refTextContent = ref(props.content);

function canEdit() {
    let todayMidNight = date_time_util.nowDate().getTime() / 1000;
    return props.time >= todayMidNight;
}

function hasContent() {
    return props.content != null && props.content.trim() !== "";
}

</script>

<template>
    <el-card style="max-width: 280px">
        <template #header>
            <div>
                <span>{{ props.name }}</span>
            </div>
        </template>
        <div>
            <!-- 非空状态 -->
            <div v-if="hasContent()">
                <el-input
                    v-model="refTextContent"
                    style="width: 240px"
                    :rows="7"
                    type="textarea"
                    placeholder=""
                    clearable
                    resize="none"
                    :disabled="!canEdit()"
                />
            </div>

            <!-- 空状态 -->
            <el-empty v-if="!hasContent()" style="max-width:280px; height:10px; z-index:3000;"
                      description="未填写内容"/>

            <el-button @click="$emit('onEdit', props.id, refTextContent)" v-show="canEdit()" type="success"
                       :icon="ep_check" circle/>
        </div>
    </el-card>
</template>

<style scoped>
</style>
