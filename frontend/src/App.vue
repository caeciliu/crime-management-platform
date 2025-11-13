<template>
  <div id="app">
    <header>
      <nav>
        <div class="logo">
          <h1>城市犯罪事件管理平台</h1>
        </div>
        <ul class="nav-links">
          <li><router-link to="/">首页</router-link></li>
          <li><router-link to="/crime-events">犯罪事件</router-link></li>
          <li><router-link to="/statistics">统计分析</router-link></li>
        </ul>
      </nav>
    </header>

    <main>
      <router-view />
    </main>

    <footer>
      <p>&copy; 2023 城市犯罪事件管理平台. 所有权利保留.</p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

header {
  background-color: #2c3e50;
  color: white;
  padding: 1rem 2rem;
}

nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
}

.logo h1 {
  font-size: 1.5rem;
}

.nav-links {
  display: flex;
  list-style: none;
}

.nav-links li {
  margin-left: 2rem;
}

.nav-links a {
  color: white;
  text-decoration: none;
  font-weight: bold;
  transition: color 0.3s;
}

.nav-links a:hover,
.nav-links a.router-link-active {
  color: #42b983;
}

main {
  flex: 1;
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

footer {
  background-color: #f8f9fa;
  text-align: center;
  padding: 1rem;
  border-top: 1px solid #e9ecef;
}
</style>