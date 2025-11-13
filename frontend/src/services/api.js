import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: '/api', // 使用nginx代理
  timeout: 10000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 在发送请求之前做些什么
    return config
  },
  error => {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    // 对响应数据做点什么
    return response
  },
  error => {
    // 对响应错误做点什么
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

export default api
