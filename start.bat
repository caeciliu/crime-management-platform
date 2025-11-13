@echo off
echo ======================================
echo City Crime Management Platform Startup Script
echo ======================================

echo Building backend...
cd backend
call mvn clean package -DskipTests
cd ..

echo Building frontend...
cd frontend
call npm run build
cd ..

echo Stopping and removing existing containers...
docker-compose down

echo Removing Docker build cache...
docker system prune -f

echo Removing frontend image to force rebuild...
docker rmi crime-management-platform-frontend 2>nul

echo Removing backend image to force rebuild...
docker rmi crime-management-platform-backend 2>nul

echo Building and starting services without cache...
docker-compose build --no-cache
docker-compose up -d

echo Checking service status...
docker-compose ps

echo ======================================
echo Startup completed!
echo Frontend: http://localhost
echo Backend API: http://localhost:8080
echo ======================================

echo View logs: docker-compose logs -f
echo Stop services: docker-compose down

pause