<script lang="ts" setup>
import {ref} from 'vue'
import zhCn from "element-plus/es/locale/lang/zh-cn";
import {DateTimeUtil} from '@/framework/utils/DateTimeUtil';

defineEmits(['onDateRangeChanged']);
const props = defineProps(['beginDate', 'endDate']);

const refValue = ref('');

const shortcuts = [
    {
        text: '上周',
        value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            return [start, end]
        },
    },
    {
        text: '上月',
        value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            return [start, end]
        },
    },
    {
        text: '上季',
        value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            return [start, end]
        },
    },
    {
        text: '上年',
        value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 365)
            return [start, end]
        },
    },
]

function lastMonthDate() {
    let d = DateTimeUtil.nowDate();
    d.setTime(d.getTime() - 3600 * 1000 * 24 * 30);
    return d;
}

</script>

<template>
    <div class="date-picker-root">
        <el-config-provider :locale="zhCn">
            <el-date-picker @change="$emit('onDateRangeChanged', refValue)"
                            :clearable="false"
                            v-model="refValue"
                            type="daterange"
                            range-separator="至"
                            start-placeholder="导出开始日期"
                            end-placeholder="导出结束日期"
                            format="YYYY/MM/DD"
                            value-format="x"
                            size="small"
                            unlink-panels
                            :shortcuts="shortcuts"
                            :default-value="[lastMonthDate(), DateTimeUtil.nowDate()]"
            />
        </el-config-provider>
    </div>
</template>

<style scoped>
.date-picker-root {
    position: absolute;
    top: 30px;
    right: 75px;
    text-align: center;
    border-right: solid 1px var(--el-border-color);
    flex: 1;
}

.date-picker-root:last-child {
    border-right: none;
}
</style>
