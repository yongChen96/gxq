<!-- 应用预警状态柱状图对比分析 -->
<template>
  <div id="appWarnState"></div>
</template>

<script>
export default {
  data () {
    return {
      appWarnState: '',
      echartData: []
    }
  },
  methods: {
    initAppWarnState () {
      const echartData = this.echartData
      // 基于准备好的dom，初始化echarts实例
      if (!this.appWarnState) {
        this.appWarnState = this.$echarts.init(document.getElementById('appWarnState'))
      }
      const barWidth = 5
      const option = {
        backgroundColor: '#fff',
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['已处理预警', '未处理预警', '本年新增预警'],
          align: 'right',
          right: 10,
          textStyle: {
            color: "#333"
          },
          itemWidth: 10,
          itemHeight: 10,
          itemGap: 35
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [{
            type: 'category',
            data: this.echartData.names,
            axisLabel : {
                show: true,
                textStyle: {
                    color: "#00c7ff",
                },
                formatter : function (value){
                    let valueTxt = '';
                    if (value.length > 6) {
                    valueTxt = value.substring(0, 4) + '...';
                    }
                    else {
                        valueTxt = value;
                    }
                    return valueTxt ;
                },
                interval:0,
                rotate:40
            },
            axisLine: {
                show: true,
                lineStyle: {
                    color: "#A6A6A6",
                    width: 1,
                    type: "solid"
                },
            },
            axisTick: {
                show: false
            }
        }],
        yAxis: [{
            type: 'value',
            axisTick: {
                show: false,
            },
            axisLine: {
                show: true,
                lineStyle: {
                    color: "#A6A6A6",
                    width: 1,
                    type: "solid"
                },
            },
            splitLine: {
                show: false
            }
        }],
        series: [{
            name: '已处理预警',
            type: 'bar',
            data: this.echartData.processeds,
            barWidth: barWidth,
            barGap: 1,
            itemStyle: {
                normal: {
                    color: '#9FDFA1'
                }
            }
        }, {
            name: '未处理预警',
            type: 'bar',
            data: this.echartData.untreateds,
            barWidth: barWidth,
            barGap: 1,
            itemStyle: {
                normal: {
                    color: '#F19A9A'
                }
            }
        }, {
            name: '新增预警',
            type: 'bar',
            data: this.echartData.newThisYears,
            barWidth: barWidth, //柱子宽度
            barGap: 1, //柱子之间间距
            itemStyle: {
                normal: {
                    color: '#85C8F6'
                }
            }
        }]
      };
      // 使用刚指定的配置项和数据显示图表。
      this.appWarnState.setOption(option);
    },
    refresh (data) {
      this.echartData = data
      this.initAppWarnState()
    }
  }
}
</script>

<style lang="less" scoped>
#appWarnState{
  width: 100%;
  height: 300px;
  // overflow: auto;
}
</style>
