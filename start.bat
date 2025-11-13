@echo off
echo ======================================
echo City Crime Management Platform Startup Script
echo ======================================

echo Stopping and removing existing containers...
docker-compose down

echo Building and starting services...
docker-compose up -d --build


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