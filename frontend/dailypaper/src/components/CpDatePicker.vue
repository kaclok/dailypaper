<script lang="ts" setup>
import {ref} from 'vue'
import zhCn from "element-plus/es/locale/lang/zh-cn";
import TimeUtil from "@/utils/DateTimeUtil";

defineEmits(['onDateChanged']);
const props = defineProps(['targetDate']);

const refTargetDate = ref(props.targetDate);

function overToday() {
    return refTargetDate.value.getTime() >= TimeUtil.nowDate().getTime() + 86400;
}

</script>

<template>
    <div class="date-picker-root">
        <el-config-provider :locale="zhCn">
            <el-date-picker @change="$emit('onDateChanged', refTargetDate)"
                            :clearable="false"
                            v-model="refTargetDate"
                            type="date"
                            placeholder="选择日期"
                            format="YYYY/MM/DD"
                            value-format="x"
                            size="small"
            />
        </el-config-provider>
    </div>
</template>

<style scoped>
.date-picker-root {
    position: absolute;
    top: 30px;
    left: 30px;
    text-align: center;
    border-right: solid 1px var(--el-border-color);
    flex: 1;
}

.date-picker-root:last-child {
    border-right: none;
}
</style>
