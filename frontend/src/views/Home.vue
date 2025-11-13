<template>
  <div class="home">
    <div class="welcome">
      <h1>欢迎使用城市犯罪事件管理平台</h1>
      <p>这是一个专业的城市犯罪事件管理系统，提供全面的事件管理、分析和统计功能。</p>
    </div>

    <div class="features">
      <el-card class="feature-card">
        <template #header>
          <div class="card-header">
            <el-icon><Document /></el-icon>
            <span>事件管理</span>
          </div>
        </template>
        <ul>
          <li>犯罪事件录入</li>
          <li>事件状态跟踪</li>
          <li>事件详情查看</li>
          <li>事件更新与删除</li>
        </ul>
      </el-card>

      <el-card class="feature-card">
        <template #header>
          <div class="card-header">
            <el-icon><Search /></el-icon>
            <span>搜索查询</span>
          </div>
        </template>
        <ul>
          <li>按类型筛选</li>
          <li>按地点搜索</li>
          <li>按时间范围查询</li>
          <li>多条件组合搜索</li>
        </ul>
      </el-card>

      <el-card class="feature-card">
        <template #header>
          <div class="card-header">
            <el-icon><DataAnalysis /></el-icon>
            <span>统计分析</span>
          </div>
        </template>
        <ul>
          <li>犯罪类型统计</li>
          <li>事件状态分析</li>
          <li>时间趋势分析</li>
          <li>地点分布统计</li>
        </ul>
      </el-card>
    </div>

    <div class="tech-stack">
      <h2>技术栈</h2>
      <div class="tech-cards">
        <el-card>
          <template #header>
            <span>前端技术</span>
          </template>
          <ul>
            <li>Vue 3</li>
            <li>Element Plus</li>
            <li>ECharts</li>
            <li>Axios</li>
          </ul>
        </el-card>

        <el-card>
          <template #header>
            <span>后端技术</span>
          </template>
          <ul>
            <li>Spring Boot 3</li>
            <li>Spring Data JPA</li>
            <li>Spring Cache</li>
            <li>MySQL 8.0</li>
          </ul>
        </el-card>

        <el-card>
          <template #header>
            <span>容器化</span>
          </template>
          <ul>
            <li>Docker</li>
            <li>Docker Compose</li>
            <li>Nginx</li>
          </ul>
        </el-card>
      </div>
    </div>

    <div class="actions">
      <el-button type="primary" size="large" @click="$router.push('/crime-events')">
        <el-icon><View /></el-icon>
        查看犯罪事件
      </el-button>
      <el-button type="success" size="large" @click="$router.push('/statistics')">
        <el-icon><DataAnalysis /></el-icon>
        查看统计分析
      </el-button>
    </div>
  </div>
</template>

<script>
import { Document, Search, DataAnalysis, View } from '@element-plus/icons-vue'

export default {
  name: 'Home',
  components: {
    Document,
    Search,
    DataAnalysis,
    View
  }
}
</script>

<style scoped>
.home {
  text-align: center;
  padding: 2rem 0;
}

.welcome {
  margin-bottom: 3rem;
}

.welcome h1 {
  color: #2c3e50;
  margin-bottom: 1rem;
  font-size: 2.5rem;
}

.welcome p {
  font-size: 1.2rem;
  color: #666;
  max-width: 800px;
  margin: 0 auto;
}

.features {
  display: flex;
  justify-content: center;
  gap: 2rem;
  margin-bottom: 3rem;
  flex-wrap: wrap;
}

.feature-card {
  width: 300px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: bold;
  font-size: 1.1rem;
}

.feature-card ul {
  list-style: none;
  padding: 0;
  text-align: left;
}

.feature-card li {
  padding: 0.5rem 0;
  border-bottom: 1px solid #eee;
}

.feature-card li:last-child {
  border-bottom: none;
}

.tech-stack {
  margin-bottom: 3rem;
}

.tech-stack h2 {
  color: #2c3e50;
  margin-bottom: 2rem;
}

.tech-cards {
  display: flex;
  justify-content: center;
  gap: 2rem;
  flex-wrap: wrap;
}

.tech-cards .el-card {
  width: 300px;
}

.tech-cards ul {
  list-style: none;
  padding: 0;
  text-align: left;
}

.tech-cards li {
  padding: 0.5rem 0;
  border-bottom: 1px solid #eee;
}

.tech-cards li:last-child {
  border-bottom: none;
}

.actions {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 2rem;
}
</style>