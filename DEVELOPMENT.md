# 开发环境说明

## 本地开发设置

### 后端开发环境

1. **环境要求**
   - JDK 21+
   - Maven 3.6+
   - MySQL 8.0+ (可选，可使用Docker中的MySQL)

2. **本地启动步骤**
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

3. **配置文件**
   - 开发环境配置: `application.properties`
   - 数据库连接已配置为本地MySQL

### 前端开发环境

1. **环境要求**
   - Node.js 16+
   - npm 7+

2. **本地启动步骤**
   ```bash
   cd frontend
   npm install
   npm run serve
   ```

3. **开发服务器**
   - 访问地址: http://localhost:3000
   - API代理已配置到本地后端(8080端口)

## 数据库结构

### 犯罪事件表 (crime_events)

```sql
CREATE TABLE crime_events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT '案件标题',
    crime_type VARCHAR(100) NOT NULL COMMENT '犯罪类型',
    location VARCHAR(255) NOT NULL COMMENT '案发地点',
    description TEXT COMMENT '事件描述',
    incident_time DATETIME NOT NULL COMMENT '案发时间',
    report_time DATETIME COMMENT '上报时间',
    status VARCHAR(50) DEFAULT '待处理' COMMENT '案件状态',
    severity_level VARCHAR(20) COMMENT '严重程度',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```


## API接口说明

### 基础路径
- 生产环境: http://localhost/api
- 开发环境: http://localhost:8080/api

### 主要接口

1. **犯罪事件管理**
   - `GET /crime-events` - 获取事件列表
   - `POST /crime-events` - 创建事件
   - `PUT /crime-events/{id}` - 更新事件
   - `DELETE /crime-events/{id}` - 删除事件

2. **搜索功能**
   - `GET /crime-events/search` - 多条件搜索
   - `GET /crime-events/search/time-range` - 时间范围搜索

3. **统计功能**
   - `GET /crime-events/statistics` - 获取统计数据

## 部署说明

### Docker部署

1. **构建镜像**
   ```bash
   docker-compose build
   ```

2. **启动服务**
   ```bash
   docker-compose up -d
   ```

3. **查看日志**
   ```bash
   docker-compose logs -f [service_name]
   ```

### 端口映射

- 前端(Nginx): 80 → 80
- 后端(Spring Boot): 8080 → 8080
- 数据库(MySQL): 3306 → 3306

## 常见问题

### 1. 数据库连接失败
- 检查MySQL服务状态
- 确认数据库连接配置
- 验证用户名密码

### 2. 前端无法访问后端
- 检查跨域配置
- 验证Nginx代理设置
- 确认后端服务正常启动

### 4. 构建失败
- 清理Maven缓存: `mvn clean`
- 重新安装依赖: `npm install`
- 检查Docker服务状态

## 日志查看

### 后端日志
```bash
docker-compose logs -f backend
```

### 前端日志
```bash
docker-compose logs -f frontend
```

### 数据库日志
```bash
docker-compose logs -f db
```

