<template>
  <div class="crime-events">
    <!-- Header -->
    <header class="page-header">
      <div class="header-content">
        <div class="header-title">
          <div class="logo">
            <i class="shield-icon">🛡️</i>
            <h1>城市犯罪管理平台</h1>
          </div>
        </div>
        <div class="header-actions">
          <el-button type="primary" class="btn-primary" @click="showAddDialog">
            <el-icon><Plus /></el-icon>
            添加事件
          </el-button>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="main-content">
      <!-- Search Section -->
      <div class="search-card">
        <div class="search-header">
          <h2>搜索与筛选</h2>
          <div class="advanced-filter">
            <el-button type="text" @click="showAdvancedFilter = !showAdvancedFilter">
              <el-icon><Setting /></el-icon>
              高级筛选
            </el-button>
          </div>
        </div>
        <el-form :model="searchForm" class="search-form">
          <div class="search-grid">
            <div class="form-item">
              <label>犯罪类型</label>
              <el-select v-model="searchForm.crimeType" placeholder="全部类型" clearable>
                <el-option label="盗窃" value="盗窃" />
                <el-option label="抢劫" value="抢劫" />
                <el-option label="诈骗" value="诈骗" />
                <el-option label="暴力" value="暴力" />
                <el-option label="毒品犯罪" value="毒品犯罪" />
                <el-option label="故意破坏" value="故意破坏" />
                <el-option label="其他" value="其他" />
              </el-select>
            </div>
            <div class="form-item">
              <label>严重程度</label>
              <el-select v-model="searchForm.severityLevel" placeholder="全部级别" clearable>
                <el-option label="轻微" value="轻微" />
                <el-option label="中等" value="中等" />
                <el-option label="严重" value="严重" />
              </el-select>
            </div>
          </div>
          <div class="search-actions">
            <div class="left-actions">
              <el-button type="primary" class="btn-primary" @click="searchEvents">
                <el-icon><Search /></el-icon>
                搜索
              </el-button>
              <el-button class="btn-secondary" @click="resetSearch">
                <el-icon><Refresh /></el-icon>
                重置
              </el-button>
            </div>
          </div>
        </el-form>
      </div>

      <!-- Stats Overview -->
      <div class="stats-grid">
        <div class="stat-card card-pop-in">
          <div class="stat-content">
            <div class="stat-info">
              <p class="stat-label">总案件数</p>
              <p class="stat-value">{{ total }}</p>
              <p class="stat-description">系统中所有案件</p>
            </div>
            <div class="stat-icon primary">
              <i class="icon">⚠️</i>
            </div>
          </div>
        </div>

        <div class="stat-card card-pop-in" style="animation-delay: 0.1s">
          <div class="stat-content">
            <div class="stat-info">
              <p class="stat-label">正在调查</p>
              <p class="stat-value">{{ investigatingCount }}</p>
              <p class="stat-description">调查中的案件</p>
            </div>
            <div class="stat-icon warning">
              <i class="icon">🔄</i>
            </div>
          </div>
        </div>

        <div class="stat-card card-pop-in" style="animation-delay: 0.2s">
          <div class="stat-content">
            <div class="stat-info">
              <p class="stat-label">已结案件</p>
              <p class="stat-value">{{ solvedCount }}</p>
              <p class="stat-description">已处理的案件</p>
            </div>
            <div class="stat-icon success">
              <i class="icon">✅</i>
            </div>
          </div>
        </div>

        <div class="stat-card card-pop-in" style="animation-delay: 0.3s">
          <div class="stat-content">
            <div class="stat-info">
              <p class="stat-label">处理进度</p>
              <p class="stat-value">{{ solveRate }}%</p>
              <p class="stat-description">案件处理率</p>
            </div>
            <div class="stat-icon info">
              <i class="icon">📊</i>
            </div>
          </div>
        </div>
      </div>

      <!-- Data Table -->
      <div class="table-card">
        <div class="table-header">
          <h3>犯罪案件列表</h3>
          <div class="table-actions">
            <el-button class="table-btn">
              <el-icon><Download /></el-icon>
              导出数据
            </el-button>
            <el-button class="table-btn">
              <el-icon><Map /></el-icon>
              地图视图
            </el-button>
          </div>
        </div>

        <div class="table-container">
          <el-table :data="events" v-loading="loading" class="data-table">
            <el-table-column prop="id" label="案件编号" width="140" />
            <el-table-column prop="crimeType" label="犯罪类型" width="120">
              <template #default="scope">
                <span class="crime-type-tag" :class="getCrimeTypeClass(scope.row.crimeType)">
                  {{ scope.row.crimeType }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="incidentTime" label="时间" width="160">
              <template #default="scope">
                {{ formatDateTime(scope.row.incidentTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="location" label="地点" width="180" />
            <el-table-column prop="severityLevel" label="严重程度" width="100">
              <template #default="scope">
                <span class="severity-badge" :class="getSeverityClass(scope.row.severityLevel)">
                  {{ scope.row.severityLevel }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <span class="status-badge" :class="getStatusClass(scope.row.status)">
                  {{ scope.row.status }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="scope">
                <el-button size="small" class="action-btn" @click="viewEvent(scope.row)">
                  详情
                </el-button>
                <el-button size="small" class="action-btn" @click="editEvent(scope.row)">
                  编辑
                </el-button>
                <el-button size="small" type="danger" class="action-btn" @click="confirmDelete(scope.row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- Pagination -->
        <div class="pagination-container">
          <div class="pagination-info">
            显示 1 到 {{ Math.min(pageSize, total) }} 条，共 {{ total }} 条记录
          </div>
          <div class="pagination-controls">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[10, 20, 50, 100]"
              :total="total"
              layout="prev, pager, next"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </div>
    </main>

    <!-- 添加/编辑事件对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑犯罪事件' : '添加犯罪事件'"
      width="50%"
      :close-on-click-modal="false"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="案件标题" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="犯罪类型" prop="crimeType">
          <el-select v-model="form.crimeType" placeholder="选择犯罪类型" style="width: 100%">
            <el-option label="盗窃" value="盗窃" />
            <el-option label="抢劫" value="抢劫" />
            <el-option label="诈骗" value="诈骗" />
            <el-option label="暴力" value="暴力" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="案发地点" prop="location">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="案发时间" prop="incidentTime">
          <el-date-picker
            v-model="form.incidentTime"
            type="datetime"
            placeholder="选择案发时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="严重程度" prop="severityLevel">
          <el-select v-model="form.severityLevel" placeholder="选择严重程度" style="width: 100%">
            <el-option label="轻微" value="轻微" />
            <el-option label="中等" value="中等" />
            <el-option label="严重" value="严重" />
          </el-select>
        </el-form-item>
        <el-form-item label="事件状态" prop="status">
          <el-select v-model="form.status" placeholder="选择状态" style="width: 100%">
            <el-option label="待处理" value="待处理" />
            <el-option label="调查中" value="调查中" />
            <el-option label="处理中" value="处理中" />
            <el-option label="已结案" value="已结案" />
          </el-select>
        </el-form-item>
        <el-form-item label="事件描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入事件详细描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveEvent">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看事件详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="事件详情"
      width="50%"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="案件ID">{{ currentEvent.id }}</el-descriptions-item>
        <el-descriptions-item label="案件标题">{{ currentEvent.title }}</el-descriptions-item>
        <el-descriptions-item label="犯罪类型">
          <el-tag :type="getCrimeTypeTagType(currentEvent.crimeType)">
            {{ currentEvent.crimeType }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="严重程度">
          <el-tag :type="getSeverityTagType(currentEvent.severityLevel)">
            {{ currentEvent.severityLevel }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="案发地点">{{ currentEvent.location }}</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <el-tag :type="getStatusTagType(currentEvent.status)">
            {{ currentEvent.status }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="案发时间">
          {{ formatDateTime(currentEvent.incidentTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="上报时间">
          {{ formatDateTime(currentEvent.reportTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="事件描述" :span="2">
          {{ currentEvent.description || '无描述' }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, View, Edit, Delete, Setting, Download, Map } from '@element-plus/icons-vue'
import crimeEventService from '../services/crimeEventService'

export default {
  name: 'CrimeEvents',
  components: {
    Plus, Search, Refresh, View, Edit, Delete, Setting, Download, Map
  },
  data() {
    return {
      events: [],
      loading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      viewDialogVisible: false,
      isEdit: false,
      currentEvent: {},
      showAdvancedFilter: false,
      investigatingCount: 0,
      solvedCount: 0,
      searchForm: {
        crimeType: '',
        status: '',
        severityLevel: '',
        location: ''
      },
      form: {
        id: null,
        title: '',
        crimeType: '',
        location: '',
        description: '',
        incidentTime: '',
        status: '待处理',
        severityLevel: ''
      },
      rules: {
        title: [
          { required: true, message: '请输入案件标题', trigger: 'blur' },
          { min: 5, max: 200, message: '标题长度在 5 到 200 个字符', trigger: 'blur' }
        ],
        crimeType: [
          { required: true, message: '请选择犯罪类型', trigger: 'change' }
        ],
        location: [
          { required: true, message: '请输入案发地点', trigger: 'blur' }
        ],
        incidentTime: [
          { required: true, message: '请选择案发时间', trigger: 'change' }
        ],
        severityLevel: [
          { required: true, message: '请选择严重程度', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择事件状态', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    solveRate() {
      return this.total > 0 ? Math.round((this.solvedCount / this.total) * 100 * 10) / 10 : 0
    }
  },
  created() {
    this.fetchEvents()
  },
  methods: {
    async fetchEvents() {
      this.loading = true
      try {
        const response = await crimeEventService.getCrimeEventsPage(this.currentPage - 1, this.pageSize)
        this.events = response.data.content
        this.total = response.data.totalElements

        // Calculate statistics based on actual data
        this.calculateStatistics()
      } catch (error) {
        ElMessage.error('获取犯罪事件列表失败')
        console.error('Error fetching crime events:', error)
      } finally {
        this.loading = false
      }
    },

    async searchEvents() {
      this.loading = true
      try {
        const searchParams = {
          crimeType: this.searchForm.crimeType || null,
          status: this.searchForm.status || null,
          severityLevel: this.searchForm.severityLevel || null,
          location: this.searchForm.location || null
        }
        const response = await crimeEventService.searchCrimeEvents(searchParams)
        this.events = response.data
        this.total = response.data.length

        // Calculate statistics for search results
        this.calculateStatistics()
      } catch (error) {
        ElMessage.error('搜索失败')
        console.error('Error searching events:', error)
      } finally {
        this.loading = false
      }
    },

    resetSearch() {
      this.searchForm = {
        crimeType: '',
        status: '',
        severityLevel: '',
        location: ''
      }
      this.fetchEvents()
    },

    calculateStatistics() {
      // Calculate statistics based on current events data
      this.investigatingCount = this.events.filter(event =>
        event.status === '调查中' || event.status === '处理中'
      ).length

      this.solvedCount = this.events.filter(event =>
        event.status === '已结案'
      ).length
    },

    showAddDialog() {
      this.isEdit = false
      this.form = {
        id: null,
        title: '',
        crimeType: '',
        location: '',
        description: '',
        incidentTime: '',
        status: '待处理',
        severityLevel: ''
      }
      this.dialogVisible = true
    },

    viewEvent(event) {
      this.currentEvent = { ...event }
      this.viewDialogVisible = true
    },

    editEvent(event) {
      this.isEdit = true
      this.form = {
        ...event,
        incidentTime: this.formatDateTimeForInput(event.incidentTime)
      }
      this.dialogVisible = true
    },

    async saveEvent() {
      try {
        await this.$refs.formRef.validate()

        if (this.isEdit) {
          await crimeEventService.updateCrimeEvent(this.form.id, this.form)
          ElMessage.success('犯罪事件更新成功')
        } else {
          await crimeEventService.createCrimeEvent(this.form)
          ElMessage.success('犯罪事件创建成功')
        }

        this.dialogVisible = false
        this.fetchEvents()
      } catch (error) {
        if (error.response && error.response.data) {
          ElMessage.error(error.response.data.message || '操作失败')
        } else {
          ElMessage.error('操作失败')
        }
        console.error('Error saving event:', error)
      }
    },

    confirmDelete(event) {
      ElMessageBox.confirm(
        `确定要删除案件 "${event.title}" 吗？`,
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(async () => {
        try {
          await crimeEventService.deleteCrimeEvent(event.id)
          ElMessage.success('删除成功')
          this.fetchEvents()
        } catch (error) {
          ElMessage.error('删除失败')
          console.error('Error deleting event:', error)
        }
      }).catch(() => {
        // 用户取消删除
      })
    },

    handleSizeChange(newSize) {
      this.pageSize = newSize
      this.fetchEvents()
    },

    handleCurrentChange(newPage) {
      this.currentPage = newPage
      this.fetchEvents()
    },

    getCrimeTypeTagType(crimeType) {
      const typeMap = {
        '盗窃': 'warning',
        '抢劫': 'danger',
        '诈骗': 'info',
        '暴力': 'danger',
        '其他': ''
      }
      return typeMap[crimeType] || ''
    },

    getSeverityTagType(severity) {
      const severityMap = {
        '轻微': 'success',
        '中等': 'warning',
        '严重': 'danger'
      }
      return severityMap[severity] || ''
    },

    getStatusTagType(status) {
      const statusMap = {
        '待处理': 'info',
        '调查中': 'warning',
        '处理中': 'warning',
        '已结案': 'success'
      }
      return statusMap[status] || ''
    },

    formatDateTime(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleString('zh-CN')
    },

    formatDateTimeForInput(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toISOString().slice(0, 19).replace('T', ' ')
    },

    // New styling methods for custom tags and badges
    getCrimeTypeClass(crimeType) {
      const classMap = {
        '盗窃': 'theft',
        '抢劫': 'robbery',
        '诈骗': 'fraud',
        '暴力': 'assault',
        '毒品犯罪': 'drug',
        '故意破坏': 'vandalism',
        '其他': 'other'
      }
      return classMap[crimeType] || 'other'
    },

    getSeverityClass(severity) {
      const classMap = {
        '轻微': 'low',
        '中等': 'medium',
        '严重': 'high'
      }
      return classMap[severity] || 'medium'
    },

    getStatusClass(status) {
      const classMap = {
        '待处理': 'pending',
        '调查中': 'investigating',
        '处理中': 'processing',
        '已结案': 'closed',
        '已立案': 'active'
      }
      return classMap[status] || 'pending'
    }
  }
}
</script>

<style scoped>
/* Modern crime platform styling based on the design reference */
.crime-events {
  min-height: 100vh;
  background: #fdfdfd;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  color: #1f2937;
}

/* Header Styles */
.page-header {
  background: #ffffff;
  border-bottom: 1px solid #e5e7eb;
  box-shadow: 0 1px 3px 0px hsl(0 0% 0% / 0.1);
  margin-bottom: 2rem;
}

.header-content {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
}

.logo {
  display: flex;
  align-items: center;
  space-x: 0.5rem;
}

.shield-icon {
  width: 32px;
  height: 32px;
  background: #4f46e5;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  margin-right: 0.5rem;
}

.logo h1 {
  font-size: 1.25rem;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

/* Main Content */
.main-content {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 2rem;
}

/* Search Section */
.search-card {
  background: #ffffff;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px 0px hsl(0 0% 0% / 0.1);
  border: 1px solid #e5e7eb;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
}

.search-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.search-header h2 {
  font-size: 1.125rem;
  font-weight: 600;
  margin: 0;
  color: #1f2937;
}

.advanced-filter button {
  color: #4f46e5;
}

.search-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.search-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
  margin-bottom: 1rem;
}

.form-item {
  display: flex;
  flex-direction: column;
}

.form-item label {
  display: block;
  font-size: 0.875rem;
  font-weight: 500;
  color: #6b7280;
  margin-bottom: 0.5rem;
}

.form-item :deep(.el-select),
.form-item :deep(.el-input) {
  width: 100%;
}

.search-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.btn-primary {
  background-color: #4f46e5 !important;
  color: #ffffff !important;
  border-radius: 0.375rem !important;
  transition: all 200ms ease !important;
  border: none !important;
}

.btn-primary:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 4px 6px -1px hsl(0 0% 0% / 0.1) !important;
}

.btn-secondary {
  background: transparent !important;
  color: #6b7280 !important;
  border: 1px solid #e5e7eb !important;
  border-radius: 0.375rem !important;
}

.btn-secondary:hover {
  background: #f9fafb !important;
  color: #1f2937 !important;
}

/* Stats Grid */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}

.stat-card {
  background: #ffffff;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px 0px hsl(0 0% 0% / 0.1);
  border: 1px solid #e5e7eb;
  padding: 1.5rem;
  transition: transform 200ms ease, box-shadow 200ms ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 6px -1px hsl(0 0% 0% / 0.1);
}

.stat-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 0.875rem;
  font-weight: 500;
  color: #6b7280;
  margin: 0 0 0.25rem 0;
}

.stat-value {
  font-size: 2rem;
  font-weight: 700;
  color: #1f2937;
  margin: 0.25rem 0;
}

.stat-description {
  font-size: 0.75rem;
  color: #6b7280;
  margin: 0.25rem 0 0 0;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-icon.primary {
  background: rgba(79, 70, 229, 0.1);
  color: #4f46e5;
}

.stat-icon.warning {
  background: rgba(245, 158, 11, 0.1);
  color: #f59e0b;
}

.stat-icon.success {
  background: rgba(34, 197, 94, 0.1);
  color: #22c55e;
}

.stat-icon.danger {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
}

.stat-icon.info {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
}

/* Table Section */
.table-card {
  background: #ffffff;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px 0px hsl(0 0% 0% / 0.1);
  border: 1px solid #e5e7eb;
}

.table-header {
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-header h3 {
  font-size: 1.125rem;
  font-weight: 600;
  margin: 0;
  color: #1f2937;
}

.table-actions {
  display: flex;
  gap: 0.5rem;
}

.table-btn {
  background: #f3f4f6 !important;
  color: #6b7280 !important;
  border: none !important;
  border-radius: 0.5rem !important;
  padding: 0.5rem 0.75rem !important;
  font-size: 0.875rem !important;
}

.table-btn:hover {
  background: #e5e7eb !important;
  color: #1f2937 !important;
}

.table-container {
  overflow-x: auto;
}

.data-table :deep(.el-table__header) {
  background: #f3f4f6;
}

.data-table :deep(.el-table__header th) {
  background: #f3f4f6;
  color: #374151;
  font-weight: 500;
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  border-bottom: 1px solid #e5e7eb;
}

.data-table :deep(.el-table__row:hover) {
  background: #f9fafb;
}

.data-table :deep(.el-table__row) {
  animation: slideUp 400ms ease-out;
}

.data-table :deep(.el-table__row:nth-child(1)) { animation-delay: 0s; }
.data-table :deep(.el-table__row:nth-child(2)) { animation-delay: 0.1s; }
.data-table :deep(.el-table__row:nth-child(3)) { animation-delay: 0.2s; }
.data-table :deep(.el-table__row:nth-child(4)) { animation-delay: 0.3s; }
.data-table :deep(.el-table__row:nth-child(5)) { animation-delay: 0.4s; }

/* Custom Tags and Badges */
.crime-type-tag {
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  font-size: 0.875rem;
  font-weight: 500;
}

.severity-badge, .status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  font-size: 0.875rem;
  font-weight: 500;
}

/* Crime Type Colors */
.crime-type-tag.theft {
  background: #fef3c7;
  color: #92400e;
}

.crime-type-tag.robbery {
  background: #fee2e2;
  color: #dc2626;
}

.crime-type-tag.fraud {
  background: #dbeafe;
  color: #2563eb;
}

.crime-type-tag.assault {
  background: #fee2e2;
  color: #dc2626;
}

.crime-type-tag.drug {
  background: #f3e8ff;
  color: #9333ea;
}

.crime-type-tag.vandalism {
  background: #ecfdf5;
  color: #059669;
}

.crime-type-tag.other {
  background: #f3f4f6;
  color: #4b5563;
}

/* Severity Colors */
.severity-badge.low {
  background: #eff6ff;
  color: #2563eb;
}

.severity-badge.medium {
  background: #fffbeb;
  color: #d97706;
}

.severity-badge.high {
  background: #fef2f2;
  color: #dc2626;
}

/* Status Colors */
.status-badge.pending {
  background: #fef3c7;
  color: #92400e;
}

.status-badge.investigating, .status-badge.processing {
  background: #fef3c7;
  color: #92400e;
}

.status-badge.closed {
  background: #f3f4f6;
  color: #4b5563;
}

.status-badge.active {
  background: #dcfce7;
  color: #166534;
}

/* Action Buttons */
.action-btn {
  background: transparent !important;
  color: #4f46e5 !important;
  border: 1px solid #e5e7eb !important;
  border-radius: 0.375rem !important;
  font-size: 0.875rem !important;
  padding: 0.25rem 0.5rem !important;
  margin-right: 0.5rem !important;
}

.action-btn:hover {
  background: #4f46e5 !important;
  color: #ffffff !important;
}

.action-btn[type="danger"] {
  color: #ef4444 !important;
  border-color: #fecaca !important;
}

.action-btn[type="danger"]:hover {
  background: #ef4444 !important;
  color: #ffffff !important;
}

/* Pagination */
.pagination-container {
  padding: 1rem 1.5rem;
  border-top: 1px solid #e5e7eb;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-info {
  font-size: 0.875rem;
  color: #6b7280;
}

.pagination-controls :deep(.el-pagination) {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.pagination-controls :deep(.el-pagination button) {
  border-radius: 0.5rem;
  border: 1px solid #e5e7eb;
  background: #ffffff;
  color: #6b7280;
}

.pagination-controls :deep(.el-pagination button:hover) {
  background: #f3f4f6;
  color: #1f2937;
}

.pagination-controls :deep(.el-pagination .is-active) {
  background: #4f46e5 !important;
  color: #ffffff !important;
  border-color: #4f46e5 !important;
}

/* Animations */
@keyframes popIn {
  0% { transform: scale(0.9); opacity: 0; }
  100% { transform: scale(1); opacity: 1; }
}

@keyframes slideUp {
  0% { transform: translateY(30px); opacity: 0; }
  100% { transform: translateY(0); opacity: 1; }
}

.card-pop-in {
  animation: popIn 400ms ease-out;
}

/* Element Plus overrides */
:deep(.el-dialog) {
  border-radius: 0.5rem;
}

:deep(.el-dialog__header) {
  border-bottom: 1px solid #e5e7eb;
  padding: 1.5rem;
}

:deep(.el-dialog__body) {
  padding: 1.5rem;
}

:deep(.el-dialog__footer) {
  border-top: 1px solid #e5e7eb;
  padding: 1rem 1.5rem;
}

:deep(.el-form-item__label) {
  color: #374151;
  font-weight: 500;
}

:deep(.el-input__wrapper) {
  border-radius: 0.375rem;
  border: 1px solid #d1d5db;
}

:deep(.el-select .el-input__wrapper) {
  border-radius: 0.375rem;
}

:deep(.el-textarea__inner) {
  border-radius: 0.375rem;
  border: 1px solid #d1d5db;
}

:deep(.el-loading-mask) {
  border-radius: 0.5rem;
}
</style>
