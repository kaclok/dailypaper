<script setup>
import {onMounted, onBeforeUpdate, watchEffect} from 'vue'
import {echarts} from '@/plugins/echartsCore.js'
import i18n from '@/config/i18n.js'

const props = defineProps(["attand", "unAttand"]);
const emit = defineEmits(['onLegendSelectChanged']);

let pieChart = null;
let option = {
    /* 饼图标题
        title: {
            text: '日报总体出勤情况', // 主标题
            x: 'left' // x轴方向对齐方式
        },*/
    /* 饼图悬浮提示框 */
    tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} ({d}%)'
    },
    // 饼图底部说明
    legend: {
        orient: 'horizontal',
        right: '0',
        top: "50%",
        data: [
            i18n.ATTEND,
            i18n.UN_ATTEND],
    },
    series: [
        {
            name: '比例',
            type: 'pie',
            radius: '50',
            center: ['50%', '50%'],
            data: [
                {value: props.unAttand, name: i18n.UN_ATTEND, itemStyle: 'red'},
                {value: props.attand, name: i18n.ATTEND, itemStyle: 'blue'}
            ],
            itemStyle: {
                /*emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                },*/
                color: function (params) {
                    // 自定义颜色
                    let colorList = ['red', 'blue']
                    return colorList[params.dataIndex]
                }
            }
        }
    ]
};

function onLegendSelectChanged(params) {
    emit('onLegendSelectChanged', params)
}

onBeforeUpdate(() => {
    option.series[0].data[0].value = props.unAttand;
    option.series[0].data[1].value = props.attand;

    // 更新图表
    pieChart.setOption(option);
});

onMounted(() => {
    // 基于准备好的dom，初始化echarts实例
    pieChart = echarts.init(document.getElementById('pieChart'))
    
    // 事件触发
    // pieChart.on('legendselectchanged', onLegendSelectChanged);

    // 更新图表
    pieChart.setOption(option);
});

</script>

<template>
    <div>
        <!--必须套在一个div里面-->
        <div id="pieChart" style="width: 600px; height:400px;"></div>
    </div>
</template>

<style scoped>
</style>
