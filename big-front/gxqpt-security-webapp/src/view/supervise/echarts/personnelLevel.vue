<!-- 各人员预警级别情况 -->
<template>
  <div id="personnelLevel"></div>
</template>

<script>
export default {
  data(){
    return{
      legendmap:{'1':'特别重大(Ⅰ级)','2':'重大(Ⅱ级)','3':'较大(Ⅲ级)','4':'一般(Ⅳ级)'},
      legendData:['特别重大(Ⅰ级)', '重大(Ⅱ级)','较大(Ⅲ级)','一般(Ⅳ级)'],
      yAxisData:[],
      seriesData:[],
      echartData: [],
      lineBar: ''
    }
  },
  methods: {
    initUnitLevel () {
      // 基于准备好的dom，初始化echarts实例
      if (!this.lineBar) {
        this.lineBar = this.$echarts.init(document.getElementById('personnelLevel'))
      }
      const option = {
        tooltip : {
          trigger: 'axis',
          axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        legend: {
          data: this.legendData
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        color:['#EF0202','#EF8D02','#EFCB02','#0299EF'],//在这里设置colorList，是一个数组，图片颜色会按顺序选取
        xAxis:  {
          type: 'value',
          axisLine: {
              show: true,
              lineStyle: {
                  color: "#A6A6A6",
                  width: 1,
                  type: "solid"
              }
          },
          axisTick: {
              show: false,
          },
          axisLabel: {
              show: true,
              textStyle: {
                  color: "#00c7ff",
              }
          }
        },
        yAxis: {
          type: 'category',
          data: this.yAxisData,
          axisLine: {
              show: true,
              lineStyle: {
                  color: "#A6A6A6",
                  width: 1,
                  type: "solid"
              }
          },
          axisTick: {
              show: false,
          },
          axisLabel: {
              show: true,
              textStyle: {
                  color: "#00c7ff",
              }
          },
        },
        series: this.seriesData
      }
      // 使用刚指定的配置项和数据显示图表。
      this.lineBar.setOption(option);
    },
    count(){
      let arr = [];
      for(var i of this.legendData){
        this.seriesData.push({
          'name':i,
          type: 'bar',
          stack: '总量',
          label: {
            normal: {
              show: true,
              position: 'insideRight'
            }
          },
          data:[]
        })
      }
      for(var i = 0;i < this.echartData.length;i++){
        this.yAxisData.push(this.echartData[i].name);
        for(var j in this.legendmap){
          (this.echartData[i].count[j]) ? this.seriesData[j-1].data.push(this.echartData[i].count[j]) : this.seriesData[j-1].data.push(0);
        }
      }
    },
    refresh (data) {
      this.echartData = data
      this.count()
      this.initUnitLevel()
    }
  }
}
</script>

<style lang="less" scoped>
#personnelLevel{
  width: 100%;
  height: 300px;
}
</style>
