@echo off
echo ===============================================
echo Crime Management Platform - Development Startup
echo ===============================================

echo.
echo Checking backend service status...
netstat -an | findstr :8080 >nul
if %errorlevel% == 0 (
    echo [OK] Backend service is already running (port 8080)
    goto :start_frontend
)

echo.
echo Starting backend service...
cd /d "%~dp0backend"
if not exist pom.xml (
    echo [ERROR] pom.xml not found in backend directory!
    pause
    exit /b 1
)
start "Backend Service" cmd /k "mvn spring-boot:run"
echo [INFO] Backend service is starting, please wait...
timeout /t 10 /nobreak >nul

:start_frontend
echo.
echo Starting frontend development server...
cd /d "%~dp0frontend"
if not exist package.json (
    echo [ERROR] package.json not found in frontend directory!
    pause
    exit /b 1
)
echo [INFO] Frontend server will start at http://localhost:3000
echo [INFO] Hot reload enabled - Vue files will auto-refresh
echo [INFO] Press Ctrl+C to stop the service
echo.
npm run serve

pause