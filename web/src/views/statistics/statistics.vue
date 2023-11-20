<template>
  <div class="app-container">
    <div class="h1" :style="styleOfH1">
      <h1>系统使用数据统计</h1>
    </div>
    <br />
    <div class="choose" :style="styleOfChoose">
      系统使用模型
      <el-select v-model="selectedModelId" placeholder="请选择" @change="handleModelChange">
        <el-option
          v-for="model in models"
          :key="model.model_id"
          :label="model.model_name"
          :value="model.model_id"
        ></el-option>
      </el-select>
    </div>
    <div v-if="selectedModelId>=0">
      <br />
      <div class="chart">
        <el-card>
          <div ref="lineChart" style="height: 400px"></div>
        </el-card>
      </div>
      <br />
      <div>
        <h2>模型使用统计表</h2>
      </div>
    </div>
    <el-table
        v-loading="listLoading"
        :data="usageList.usages"
        element-loading-text="Loading"
        border
        fit
        highlight-current-row
      >

        <el-table-column align="center" label="日期"  >
          <template v-slot="scope">
            {{ formatDate(scope.row.use_date) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="用户使用次数" >
          <template v-slot="scope">
            {{ scope.row.use_count }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="反馈错误次数" >
          <template v-slot="scope">
            {{ scope.row.wrong_feedback_count }}
          </template>
        </el-table-column>
        <el-table-column label="反馈正确次数" align="center" >
          <template v-slot="scope">
            <span>{{ scope.row.right_feedback_count }}</span>
          </template>
        </el-table-column>
        <el-table-column label="错误率"  align="center" >
          <template v-slot="scope">
            {{ (scope.row.wrong_feedback_count*100/scope.row.use_count).toFixed(2).toString()+"%"}}
          </template>
        </el-table-column>
      </el-table>
  </div>
</template>

<script>
import axios from 'axios'
import * as echarts from 'echarts'

export default {
  data() {
    return {
      styleOfH1: {
        color: 'blue',
        marginLeft: '2%'
      },
      styleOfChoose: {
        marginLeft: '2%'
      },
      models: [],
      currentModel: null,
      selectedModelId: 0,
      listLoading: true,
      modelUsageData: {},
      modelUsageStats: {},
      usageList: [],
      dates: [],
      useCounts: [],
      wrongFeedbackCounts: [],
      rightFeedbackCounts: [],
      lineChart: null
    }
  },
  methods: {
    async fetchModels() {
      try {
        const response = await axios.post('http://192.168.43.254:8080/request/model')
        this.models = response.data.models
        this.currentModel = response.data.current_model
        this.selectedModelId = response.data.current_model.model_id
        this.fetchModelUsageStats()
      } catch (error) {
        console.error('Error fetching models:', error)
      }
    },
    async fetchModelUsageStats() {
      try {
        const formData = new FormData()
        formData.append('model_id', this.selectedModelId.toString())
        // console.log(this.selectedModelId.toString())
        const response = await axios.post('http://192.168.43.254:8080/request/usage', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        this.listLoading = false
        this.usageList = response.data
        this.dates = []
        this.useCounts = []
        this.wrongFeedbackCounts = []
        this.rightFeedbackCounts = []
        for (const usage of this.usageList.usages) {
          this.dates.push(usage.use_date)
          this.useCounts.push(usage.use_count)
          this.wrongFeedbackCounts.push(usage.wrong_feedback_count)
          this.rightFeedbackCounts.push(usage.right_feedback_count)
        }

        this.updateLineChart()
      } catch (error) {
        console.error('Error fetching model usage stats:', error)
      }
    },
    async handleModelChange() {
      if (this.selectedModelId>=0) {
        await this.fetchModelUsageStats()
      }
    },
    formatDate(date) {
      const options = { year: 'numeric', month: '2-digit', day: '2-digit' }
      return new Date(date).toLocaleDateString('zh-CN', options)
    },
    updateLineChart() {
      if (!this.lineChart) {
        this.lineChart = echarts.init(this.$refs.lineChart)
      }

      const option = {
        title: {
          text: '模型使用统计',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            animation: false
          }
        },
        legend: {
          data: ['用户使用次数', '反馈正确次数', '反馈错误次数'],
          top: 30
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.dates,
          axisLabel: {
            formatter: function(value) {
            // 在这里使用更明确的日期格式化方法
              return echarts.format.formatTime('yyyy-MM-dd', new Date(value))
            }
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '用户使用次数',
            type: 'line',
            data: this.useCounts
          },
          {
            name: '反馈正确次数',
            type: 'line',
            data: this.rightFeedbackCounts
          },
          {
            name: '反馈错误次数',
            type: 'line',
            data: this.wrongFeedbackCounts
          }

        ]
      }
      this.lineChart.setOption(option)
    }
  },
  mounted() {
    this.fetchModels()
  }
}
</script>

<style>
/* 在这里添加你的样式 */
</style>
