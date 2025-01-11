<script lang="ts" setup>
import {ref} from 'vue'

defineEmits(['onDateChanged']);
const props = defineProps(['targetDate']);

const refTargetDate = ref(props.targetDate);
</script>

<template>
    <div class="date-picker-root">
        <el-date-picker @change="$emit('onDateChanged', refTargetDate)"
                        :clearable="false"
                        v-model="refTargetDate"
                        type="date"
                        :editable="false"
                        placeholder="选择日期"
                        format="YYYY/MM/DD"
                        value-format="x"
                        size="small"
        />
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
