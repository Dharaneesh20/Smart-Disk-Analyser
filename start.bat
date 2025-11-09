@echo off
echo ========================================
echo  Smart Disk Analyzer - Starting...
echo ========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven and try again
    pause
    exit /b 1
)

REM Check if Node/npm is installed
where npm >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Node.js/npm is not installed or not in PATH
    echo Please install Node.js and try again
    pause
    exit /b 1
)

echo [1/4] Checking environment...
echo Maven: 
call mvn -v | findstr "Apache Maven"
echo Node: 
call node -v
echo npm: 
call npm -v
echo.

echo [2/4] Starting Backend Server (Spring Boot on port 8080)...
start "Disk Analyzer - Backend" cmd /c "cd /d "%~dp0backend" && mvn spring-boot:run"
timeout /t 3 /nobreak >nul
echo Backend server starting in separate window...
echo.

echo [3/4] Installing Frontend Dependencies (if needed)...
cd /d "%~dp0frontend"
if not exist "node_modules" (
    echo Installing npm packages...
    call npm install
) else (
    echo Frontend dependencies already installed.
)
echo.

echo [4/4] Starting Frontend Server (React on port 3000)...
start "Disk Analyzer - Frontend" cmd /c "cd /d "%~dp0frontend" && npm start"
timeout /t 3 /nobreak >nul
echo Frontend server starting in separate window...
echo.

echo ========================================
echo  Smart Disk Analyzer Started!
echo ========================================
echo.
echo Backend API:  http://localhost:8080
echo Swagger UI:   http://localhost:8080/swagger-ui.html
echo Frontend UI:  http://localhost:3000
echo.
echo Press any key to open the frontend in your browser...
pause >nul

start http://localhost:3000

echo.
echo Both servers are running in separate windows.
echo Close those windows to stop the servers.
echo.
pause
