import api from './api'

export default {
  // 获取所有犯罪事件
  getAllCrimeEvents() {
    return api.get('/crime-events')
  },

  // 分页获取犯罪事件
  getCrimeEventsPage(page = 0, size = 10) {
    return api.get(`/crime-events/page?page=${page}&size=${size}`)
  },

  // 根据ID获取犯罪事件
  getCrimeEventById(id) {
    return api.get(`/crime-events/${id}`)
  },

  // 创建犯罪事件
  createCrimeEvent(crimeEvent) {
    return api.post('/crime-events', crimeEvent)
  },

  // 更新犯罪事件
  updateCrimeEvent(id, crimeEvent) {
    return api.put(`/crime-events/${id}`, crimeEvent)
  },

  // 更新犯罪事件状态
  updateCrimeEventStatus(id, status) {
    return api.put(`/crime-events/${id}/status`, { status })
  },

  // 删除犯罪事件
  deleteCrimeEvent(id) {
    return api.delete(`/crime-events/${id}`)
  },

  // 搜索犯罪事件
  searchCrimeEvents(params) {
    return api.get('/crime-events/search', { params })
  },

  // 按时间范围搜索
  searchByTimeRange(startTime, endTime) {
    return api.get('/crime-events/search/time-range', {
      params: { startTime, endTime }
    })
  },

  // 获取犯罪统计数据
  getCrimeStatistics() {
    return api.get('/crime-events/statistics')
  },

  // 健康检查
  healthCheck() {
    return api.get('/crime-events/health')
  }
}