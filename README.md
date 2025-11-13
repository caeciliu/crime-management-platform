# 城市犯罪事件管理平台

一个基于Spring Boot + Vue + MySQL的城市犯罪事件管理系统，使用Docker进行容器化部署。

## 技术栈

### 后端
- Spring Boot 3
- Spring Data JPA
- MySQL 8.0

### 前端
- Vue 3
- Element Plus
- ECharts
- Axios

### 容器化
- Docker
- Docker Compose
- Nginx

## 快速开始

### 前提条件
- 安装 Docker 和 Docker Compose
- 确保端口 80, 3306, 8080 未被占用

### 启动项目

1. 克隆项目到本地
```bash
git clone [项目地址]
cd crime-management-platform
```

2. 启动所有服务
```bash
docker-compose up -d
```

3. 访问应用
- 前端应用: http://localhost
- 后端API: http://localhost:8080
- MySQL: localhost:3306

### 项目结构

```
crime-management-platform/
├── backend/                 # Spring Boot 后端
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/crime/management/backend/
│   │   │   │   ├── config/        # 配置类
│   │   │   │   ├── controller/    # 控制器
│   │   │   │   ├── model/         # 实体类
│   │   │   │   ├── repository/    # 数据访问层
│   │   │   │   ├── service/       # 服务层
│   │   │   │   └── CrimeManagementApplication.java
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── db/
│   │   │           └── init.sql   # 数据库初始化脚本
│   ├── Dockerfile
│   └── pom.xml
├── frontend/               # Vue 前端
│   ├── src/
│   │   ├── components/
│   │   ├── router/
│   │   ├── services/
│   │   ├── views/
│   │   ├── App.vue
│   │   └── main.js
│   ├── public/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
├── docker-compose.yml      # Docker Compose 配置
└── README.md
```

## API 文档

### 犯罪事件管理

- `GET /api/crime-events` - 获取所有犯罪事件
- `GET /api/crime-events/{id}` - 根据ID获取犯罪事件
- `POST /api/crime-events` - 创建犯罪事件
- `PUT /api/crime-events/{id}` - 更新犯罪事件
- `DELETE /api/crime-events/{id}` - 删除犯罪事件
- `GET /api/crime-events/search` - 搜索犯罪事件
- `GET /api/crime-events/statistics` - 获取统计数据

### 数据库配置

- 数据库名: crime_management_db
- 用户名: crime_user
- 密码: crime_pass

## 开发环境设置

### 后端开发

1. 进入后端目录
```bash
cd backend
```

2. 安装依赖并运行
```bash
mvn clean install
mvn spring-boot:run
```

### 前端开发

1. 进入前端目录
```bash
cd frontend
```

2. 安装依赖并运行
```bash
npm install
npm run serve
```

## 常用命令

### Docker 命令

```bash
# 启动所有服务
docker-compose up -d

# 停止所有服务
docker-compose down

# 查看日志
docker-compose logs -f [服务名]

# 重新构建并启动
docker-compose up -d --build
```

### 数据库管理

```bash
# 连接MySQL
docker exec -it crime-mysql-db mysql -u crime_user -p crime_management_db
```

## 注意事项

1. 首次启动时，MySQL会自动执行初始化脚本，创建示例数据
2. 前端API请求通过Nginx代理到后端
3. 数据库表会根据JPA实体自动创建

## 故障排除

1. 如果端口冲突，请修改docker-compose.yml中的端口映射
2. 如果数据库连接失败，请检查MySQL服务是否正常启动
3. 如果前端无法访问后端API，请检查Nginx配置和网络连接