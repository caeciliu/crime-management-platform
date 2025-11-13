# 犯罪管理平台部署指南

## 项目概述

本项目是一个城市犯罪事件管理平台，采用前后端分离架构：
- **前端**: Vue 3 + Element Plus + ECharts
- **后端**: Spring Boot 3 + JPA + MySQL
- **数据库**: MySQL 8.0
- **容器化**: Docker + Docker Compose

## 系统要求

### 开发环境
- **操作系统**: Windows/Linux/macOS
- **Java**: JDK 21+
- **Node.js**: 16.0+
- **Maven**: 3.6+
- **MySQL**: 8.0+ (如果不使用Docker)
- **Docker**: 20.0+ (可选，用于容器化部署)

### 生产环境
- **服务器**: Linux (推荐 Ubuntu 20.04+)
- **内存**: 最低 2GB，推荐 4GB+
- **存储**: 最低 20GB
- **Docker**: 20.0+ (推荐)
- **Docker Compose**: 1.29+

## 部署方式

### 方式一：一键部署 (推荐)

使用项目提供的批处理脚本快速部署：

#### Windows 环境
```bash
# 生产环境部署
start.bat

# 开发环境启动
start-dev.bat

# 停止所有服务
stop.bat
```

#### Linux/macOS 环境
```bash
# 生产环境部署
docker-compose up -d --build

# 开发环境启动
./start-dev.sh

# 停止所有服务
docker-compose down
```

### 方式二：手动部署

#### 1. 克隆项目
```bash
git clone <repository-url>
cd crime-management-platform
```

#### 2. 配置环境变量
创建 `.env` 文件（可选）：
```env
# 数据库配置
MYSQL_ROOT_PASSWORD=root123
MYSQL_DATABASE=crime_management_db
MYSQL_USER=crime_user
MYSQL_PASSWORD=crime_pass

# 应用配置
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
```

#### 3. 启动数据库服务
```bash
# 使用 Docker 启动 MySQL
docker-compose up -d db

# 或者使用本地 MySQL
# 确保MySQL服务正在运行，并导入数据库脚本
mysql -u root -p < backend/src/main/resources/db/init.sql
```

#### 4. 构建后端服务
```bash
cd backend
./mvnw clean package -DskipTests

# 或者使用 Docker
docker build -t crime-backend .
```

#### 5. 构建前端服务
```bash
cd frontend
npm install
npm run build

# 或者使用 Docker
docker build -t crime-frontend .
```

#### 6. 启动所有服务
```bash
# 使用 Docker Compose
docker-compose up -d

# 或者手动启动
# 后端
cd backend
java -jar target/crime-management-backend-1.0.0.jar

# 前端 (需要nginx配置)
# 将 frontend/dist 目录部署到nginx
```

## 配置说明

### 数据库配置

**Docker 环境** (自动配置):
- 主机: localhost:3306
- 数据库: crime_management_db
- 用户名: crime_user
- 密码: crime_pass

**手动配置**:
```sql
-- 创建数据库
CREATE DATABASE crime_management_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户
CREATE USER 'crime_user'@'%' IDENTIFIED BY 'crime_pass';
GRANT ALL PRIVILEGES ON crime_management_db.* TO 'crime_user'@'%';
FLUSH PRIVILEGES;

-- 导入表结构
USE crime_management_db;
SOURCE backend/src/main/resources/db/init.sql;
```

### 后端配置

主要配置文件位置：`backend/src/main/resources/application.yml`

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/crime_management_db?useUnicode=true&characterEncoding=UTF-8&connectionCollation=utf8mb4_unicode_ci&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: crime_user
    password: crime_pass

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
```

### 前端配置

**开发环境配置** (`frontend/vue.config.js`):
```javascript
module.exports = {
  devServer: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {'^/api': ''}
      }
    }
  }
}
```

**生产环境配置** (`frontend/nginx.conf`):
```nginx
server {
    listen 80;
    server_name localhost;

    root /usr/share/nginx/html;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://backend:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

## 服务访问

部署成功后，可通过以下地址访问服务：

- **前端应用**: http://localhost (或 http://localhost:80)
- **后端API**: http://localhost:8080
- **API文档**: http://localhost:8080/swagger-ui.html (如果集成Swagger)
- **数据库**: localhost:3306

## 常用命令

### Docker Compose 命令
```bash
# 启动所有服务
docker-compose up -d

# 重新构建并启动
docker-compose up -d --build

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f [service-name]

# 停止所有服务
docker-compose down

# 停止并删除数据卷（谨慎使用）
docker-compose down -v
```

### 开发调试
```bash
# 后端开发模式
cd backend
./mvnw spring-boot:run

# 前端开发模式
cd frontend
npm run serve
```

## 故障排查

### 常见问题

1. **端口占用**
   ```bash
   # Windows
   netstat -ano | findstr :8080

   # Linux/macOS
   lsof -i :8080

   # 解决方案：修改端口配置或停止占用进程
   ```

2. **数据库连接失败**
   - 检查数据库服务是否运行
   - 验证连接参数（主机、端口、用户名、密码）
   - 确认网络连通性

3. **前端构建失败**
   ```bash
   # 清理依赖
   cd frontend
   rm -rf node_modules package-lock.json
   npm install

   # 重新构建
   npm run build
   ```

4. **后端启动失败**
   ```bash
   # 检查Java版本
   java -version

   # 清理Maven缓存
   cd backend
   ./mvnw clean

   # 重新构建
   ./mvnw package -DskipTests
   ```

### 日志查看

**Docker 环境**:
```bash
# 查看所有服务日志
docker-compose logs

# 查看特定服务日志
docker-compose logs backend
docker-compose logs frontend
docker-compose logs db

# 实时跟踪日志
docker-compose logs -f
```

**本地环境**:
- 后端日志：控制台输出或 `logs/application.log`
- 前端日志：浏览器开发者工具控制台
- 数据库日志：MySQL错误日志

## 性能优化建议

### 后端优化
1. 配置数据库连接池
2. 启用JPA查询缓存
3. 配置适当的JVM内存参数
4. 使用反向代理（Nginx）

### 前端优化
1. 启用Gzip压缩
2. 配置静态资源缓存
3. 使用CDN加速静态资源
4. 代码分割和懒加载

### 数据库优化
1. 创建适当的索引
2. 定期清理日志数据
3. 配置查询缓存
4. 优化查询语句

## 安全配置

### 生产环境安全建议
1. 修改默认数据库密码
2. 启用HTTPS
3. 配置防火墙规则
4. 定期更新依赖包
5. 启用访问日志
6. 配置CORS策略

### 环境变量安全
```bash
# 使用环境变量存储敏感信息
export MYSQL_ROOT_PASSWORD=your_secure_password
export SPRING_DATASOURCE_PASSWORD=your_db_password
```

## 备份与恢复

### 数据库备份
```bash
# Docker 环境
docker exec crime-mysql-db mysqldump -u root -proot123 crime_management_db > backup.sql

# 本地环境
mysqldump -u root -p crime_management_db > backup.sql
```

### 数据库恢复
```bash
# Docker 环境
docker exec -i crime-mysql-db mysql -u root -proot123 crime_management_db < backup.sql

# 本地环境
mysql -u root -p crime_management_db < backup.sql
```

## 监控与维护

### 健康检查
```bash
# 检查服务状态
curl http://localhost:8080/actuator/health
curl http://localhost:80
```

### 定期维护
1. 监控磁盘空间使用
2. 检查服务资源占用
3. 定期备份数据
4. 更新安全补丁
5. 清理日志文件

## 联系支持

如遇到部署问题，请提供以下信息：
1. 操作系统版本
2. 错误日志
3. 部署方式（Docker/手动）
4. 配置文件（敏感信息可隐藏）

---

**注意**: 本指南基于当前项目结构编写，实际部署时请根据具体环境和需求进行调整。