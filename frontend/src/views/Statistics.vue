<template>
  <div class="statistics">
    <div class="page-header">
      <h1>统计分析</h1>
      <el-button type="primary" @click="refreshStatistics">
        <el-icon><Refresh /></el-icon>
        刷新数据
      </el-button>
    </div>

    <div class="stats-cards">
      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <el-icon><DataAnalysis /></el-icon>
            <span>总案件数</span>
          </div>
        </template>
        <div class="stat-number">{{ totalCases }}</div>
      </el-card>

      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <el-icon><Clock /></el-icon>
            <span>待处理</span>
          </div>
        </template>
        <div class="stat-number pending">{{ pendingCases }}</div>
      </el-card>

      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <el-icon><Search /></el-icon>
            <span>调查中</span>
          </div>
        </template>
        <div class="stat-number investigating">{{ investigatingCases }}</div>
      </el-card>

      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <el-icon><Select /></el-icon>
            <span>已结案</span>
          </div>
        </template>
        <div class="stat-number solved">{{ solvedCases }}</div>
      </el-card>
    </div>

    <div class="charts">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>犯罪类型分布</span>
            </template>
            <v-chart class="chart" :option="crimeTypeChartOption" />
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card>
            <template #header>
              <span>案件状态分布</span>
            </template>
            <v-chart class="chart" :option="statusChartOption" />
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="24">
          <el-card>
            <template #header>
              <span>严重程度分布</span>
            </template>
            <v-chart class="chart" :option="severityChartOption" />
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div class="data-table">
      <el-card>
        <template #header>
          <span>详细统计数据</span>
        </template>
        <el-table :data="statisticsData" style="width: 100%">
          <el-table-column prop="category" label="分类" />
          <el-table-column prop="count" label="数量" />
          <el-table-column prop="percentage" label="占比">
            <template #default="scope">
              {{ scope.row.percentage }}%
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import { Refresh, DataAnalysis, Clock, Search, Select } from '@element-plus/icons-vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart, BarChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import crimeEventService from '../services/crimeEventService'

// 注册ECharts组件
use([
  CanvasRenderer,
  PieChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

export default {
  name: 'Statistics',
  components: {
    VChart,
    Refresh, DataAnalysis, Clock, Search, Select
  },
  data() {
    return {
      statistics: {},
      loading: false,
      totalCases: 0,
      pendingCases: 0,
      investigatingCases: 0,
      solvedCases: 0,
      crimeTypeChartOption: {},
      statusChartOption: {},
      severityChartOption: {},
      statisticsData: []
    }
  },
  created() {
    this.fetchStatistics()
  },
  methods: {
    async fetchStatistics() {
      this.loading = true
      try {
        const response = await crimeEventService.getCrimeStatistics()
        this.statistics = response.data
        this.processStatistics()
      } catch (error) {
        ElMessage.error('获取统计数据失败')
        console.error('Error fetching statistics:', error)
      } finally {
        this.loading = false
      }
    },

    processStatistics() {
      // 统计各类案件数量
      const crimeTypeData = []
      const statusData = []
      const severityData = []
      const allData = []

      let total = 0

      // 处理犯罪类型数据
      const crimeTypes = ['盗窃', '抢劫', '诈骗', '暴力', '其他']
      crimeTypes.forEach(type => {
        const count = this.statistics[type] || 0
        total += count
        crimeTypeData.push({
          name: type,
          value: count
        })
        allData.push({
          category: `犯罪类型-${type}`,
          count: count,
          percentage: 0
        })
      })

      // 处理状态数据
      const statuses = ['待处理', '调查中', '处理中', '已结案']
      statuses.forEach(status => {
        const count = this.statistics[status] || 0
        statusData.push({
          name: status,
          value: count
        })
        allData.push({
          category: `案件状态-${status}`,
          count: count,
          percentage: 0
        })

        // 更新统计卡片数据
        if (status === '待处理') this.pendingCases = count
        if (status === '调查中') this.investigatingCases = count
        if (status === '已结案') this.solvedCases = count
      })

      // 处理严重程度数据
      const severityLevels = ['轻微', '中等', '严重']
      severityLevels.forEach(level => {
        const count = this.statistics[level] || 0
        severityData.push({
          name: level,
          value: count
        })
        allData.push({
          category: `严重程度-${level}`,
          count: count,
          percentage: 0
        })
      })

      this.totalCases = total

      // 计算百分比
      allData.forEach(item => {
        item.percentage = total > 0 ? ((item.count / total) * 100).toFixed(1) : 0
      })

      this.statisticsData = allData

      // 设置图表选项
      this.crimeTypeChartOption = {
        title: {
          text: '犯罪类型分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '犯罪类型',
            type: 'pie',
            radius: '50%',
            data: crimeTypeData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }

      this.statusChartOption = {
        title: {
          text: '案件状态分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '案件状态',
            type: 'pie',
            radius: '50%',
            data: statusData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }

      this.severityChartOption = {
        title: {
          text: '严重程度分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: severityLevels
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '案件数量',
            type: 'bar',
            data: severityData.map(item => item.value),
            itemStyle: {
              color: function(params) {
                const colorMap = {
                  '轻微': '#67C23A',
                  '中等': '#E6A23C',
                  '严重': '#F56C6C'
                }
                return colorMap[params.name] || '#409EFF'
              }
            }
          }
        ]
      }
    },

    refreshStatistics() {
      this.fetchStatistics()
      ElMessage.success('统计数据已刷新')
    }
  }
}
</script>

<style scoped>
.statistics {
  padding: 1rem;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.page-header h1 {
  color: #2c3e50;
}

.stats-cards {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.stat-card {
  flex: 1;
  min-width: 200px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: bold;
}

.stat-number {
  font-size: 2.5rem;
  font-weight: bold;
  text-align: center;
  color: #409EFF;
}

.stat-number.pending {
  color: #E6A23C;
}

.stat-number.investigating {
  color: #F56C6C;
}

.stat-number.solved {
  color: #67C23A;
}

.charts {
  margin-bottom: 2rem;
}

.chart {
  height: 300px;
  width: 100%;
}

.data-table {
  margin-top: 2rem;
}
</style>