<script setup>
import {onMounted, onBeforeUpdate, onUnmounted} from 'vue'
import {echarts} from '@/plugins/EchartsCore.js'
import {t} from "@/framework/services/LocaleService";

const props = defineProps(["attand", "unAttand", "selected"]);
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
    grid: { // https://echarts.apache.org/zh/option.html#grid.left
        top: 50,
        left: 30,
    },
    // 饼图底部说明
    legend: { // https://echarts.apache.org/zh/option.html#legend
        orient: 'vertical',
        right: '0',
        top: "40%",
        data: [
            {
                name: t('cms.daily_paper.ATTEND'), textStyle: {
                    color: 'white',
                }
            },
            {
                name: t('cms.daily_paper.UN_ATTEND'), textStyle: {
                    color: 'white',
                }
            }],
    },
    series: [
        {
            name: '简介',
            type: 'pie',
            radius: [0, 30],
            center: ['30%', '50%'],
            data: [
                {value: props.unAttand, name: t('cms.daily_paper.UN_ATTEND'), itemStyle: 'red'},
                {value: props.attand, name: t('cms.daily_paper.ATTEND'), itemStyle: 'blue'}
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
    // 设置百分比
    option.series[0].data[0].value = props.unAttand;
    option.series[0].data[1].value = props.attand;

    // 设置legend默认选中
    option.legend.selected = props.selected;

    // 更新图表
    pieChart.setOption(option);
});

onMounted(() => {
    // 基于准备好的dom，初始化echarts实例
    pieChart = echarts.init(document.getElementById('pieChart'))

    // Echarts事件监听，监听legendselectchanged事件
    pieChart.on('legendselectchanged', onLegendSelectChanged);

    // 触发 legendAllSelect 事件
    // chart.dispatchAction({
    //     type: 'legendAllSelect'
    // });

    // 设置legend默认选中
    option.legend.selected = props.selected;
    // 更新图表
    pieChart.setOption(option);
});

onUnmounted(() => {
    pieChart.off('legendselectchanged', onLegendSelectChanged);
});

</script>

<template>
    <div class="pie-root">
        <!--必须套在一个div里面-->
        <div id="pieChart"></div>
    </div>
</template>

<style scoped>
.pie-root {
    display: flex
}

#pieChart {
    width: 300px;
    height: 150px;
}
</style>
