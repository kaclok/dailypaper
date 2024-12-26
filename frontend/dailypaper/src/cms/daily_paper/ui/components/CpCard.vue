<script lang="ts" setup>
import {onBeforeUpdate, ref} from 'vue'
import {DateTimeUtil} from '@/framework/utils/DateTimeUtil'
import {t} from "@/framework/services/LocaleService";
import {Singleton} from "@/framework/services/Singleton.js";
import {SysDaily} from '@/cms/daily_paper/system/SysDaily.js'

const props = defineProps(["date", "curAccount", "id", "name", "account", "time", "content", "tomorrowPlan", "tomorrowArrangement", "isLeader"]);
defineEmits(['onEdit', 'onExportOne']);

// https://cn.vuejs.org/guide/components/props.html
// props.content只是提供一个初始值，以后refTextContent和prop的更新无关了
let refTextContent = ref(props.content);
let refTextTomorrowPlan = ref(props.tomorrowPlan);
let refTextTomorrowArrangement = ref(props.tomorrowArrangement);

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
    return midNight >= todayMidNight - 86400 * 1;
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
    refTextTomorrowPlan.value = props.tomorrowPlan;
    refTextTomorrowArrangement = ref(props.tomorrowArrangement);
});

</script>

<template>
    <el-card class="card_root">
        <template #header>
            <div style="height: 16px">
                <span :style="{color: hasEdited() ? 'blue' : 'red'}">{{ props.name }}({{ props.account }})</span>
                <span class="flag">{{ getEditText() }}</span>
                <el-button @click="$emit('onEdit', props.id, props.account, props.content, refTextContent, props.tomorrowPlan,
                refTextTomorrowPlan, props.tomorrowArrangement, refTextTomorrowArrangement)" v-if="(isSelf() ||
                Singleton.getInstance(SysDaily)._curUserIsLeader) && isToday()"
                           type="success"
                           v-cd="3"
                           circle :dark="true" style="position: relative; left: 40px; top: 0px">{{ t('cms.daily_paper.SUBMIT') }}
                </el-button>
            </div>
        </template>

        <div v-if="(isToday() || (!isToday() && hasContent()))"
             style="position: relative; top: -16px;">
            <!-- 今日工作内容 -->
            <span style="font-size: 12px; color: #000000; position: relative; left: -10px;">今日工作内容:
            </span>
            <el-input
                v-model="refTextContent"
                :rows="7"
                type="textarea"
                :placeholder="t('cms.daily_paper.INPUT_DAILY_CONTENT')"
                clearable
                :show-word-limit="true"
                resize="none"
                :disabled="!isSelf() || !isToday()"

                style="left: -16px; width: 340px; font-size: 14px;"
            />

            <!--  明日工作计划 -->
            <span style="font-size: 12px; color: #000000; position: relative; left: -10px;">明日工作计划:
            </span>
            <el-input
                v-model="refTextTomorrowPlan"
                :rows="4"
                type="textarea"
                placeholder="请输入明日工作计划"
                resize="none"
                clearable
                :show-word-limit="true"
                :disabled="!isSelf() || !isToday()"

                style="left: -16px; width: 340px; font-size: 14px;"
            />

            <!--  明日工作安排 -->
            <span style="font-size: 12px; color: #000000; position: relative; left: -10px;">明日工作安排:
            </span>
            <el-input
                v-model="refTextTomorrowArrangement"
                :rows="5"
                type="textarea"
                placeholder="请输入明日工作安排"
                resize="none"
                clearable
                :show-word-limit="true"
                :disabled="!isToday() || !Singleton.getInstance(SysDaily)._curUserIsLeader"

                style="left: -16px; width: 340px; font-size: 14px;"
            />
        </div>

        <!-- 空状态 -->
        <el-empty v-if="(!isToday() && !hasContent())" description="未填写内容"
                  style="z-index: 2000; --el-empty-fill-color-0: red; --el-empty-fill-color-1: blue;"/>
    </el-card>
</template>

<style scoped>
.card_root {
    height: 500px;

    margin: 5px;
}

.flag {
    position: relative;
    left: 10px;

    font-weight: 900;
}
</style>
