<script lang="ts" setup>
import {onBeforeUpdate, ref} from 'vue'
import {DateTimeUtil} from '@/framework/utils/DateTimeUtil'
import {t} from "@/framework/services/LocaleService";

const props = defineProps(["date", "curAccount", "id", "name", "account", "time", "content"]);
defineEmits(['onEdit', 'onExportOne']);

// https://cn.vuejs.org/guide/components/props.html
// props.content只是提供一个初始值，以后refTextContent和prop的更新无关了
let refTextContent = ref(props.content);

function isSelf() {
    if (!props.curAccount) {
        return true;
    }

    return props.curAccount === props.account;
}

// 能否编辑：只有今天能编辑， 之前日期的不能编辑
function isToday() {
    let midNight = props.date;
    let todayMidNight = DateTimeUtil.nowDate().getTime() / 1000;
    return midNight >= todayMidNight;
}

function hasContent() {
    return props.content != null && props.content.trim() !== "";
}

function getEditText() {
    return hasContent() ? t('cms.daily_paper.ATTEND') : t('cms.daily_paper.UN_ATTEND');
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
    <el-card class="card_root">
        <template #header>
            <span :style="{color: hasEdited() ? 'blue' : 'red'}">{{ props.name }}</span>
            <span class="flag">{{ getEditText() }}</span>
            <el-button @click="$emit('onEdit', props.id, props.account, props.content, refTextContent)" v-if="isSelf() && isToday()"
                       type="success"
                       v-cd-s="3"
                       circle :dark="true" style="position: relative; left: 120px; top: 30px">{{t('cms.daily_paper.SUBMIT')}}
            </el-button>
        </template>

        <!-- 非空状态 -->
        <el-input v-if="(isToday() || (!isToday() && hasContent()))"
                  v-model="refTextContent"
                  :rows="13"
                  type="textarea"
                  :placeholder="t('cms.daily_paper.INPUT_DAILY_CONTENT')"
                  clearable
                  :show-word-limit="true"
                  resize="none"
                  :disabled="!isSelf() || !isToday()"

                  style="width: 240px; font-size: 12px;"
        />

        <!-- 空状态 -->
        <el-empty v-if="(!isToday() && !hasContent())" description="未填写内容"
                  style="z-index: 2000; --el-empty-fill-color-0: red; --el-empty-fill-color-1: blue;"/>
    </el-card>
</template>

<style scoped>
.card_root {
    height: 350px;

    margin: 10px;
}

.flag {
    position: relative;
    left: 10px;

    font-weight: 900;
}
</style>
