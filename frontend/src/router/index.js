import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import CrimeEvents from '../views/CrimeEvents.vue'
import Statistics from '../views/Statistics.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/crime-events',
    name: 'CrimeEvents',
    component: CrimeEvents
  },
  {
    path: '/statistics',
    name: 'Statistics',
    component: Statistics
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router