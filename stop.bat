@echo off
echo ========================================
echo  Smart Disk Analyzer - Stopping...
echo ========================================
echo.

echo Stopping Node.js processes (Frontend)...
taskkill /F /IM node.exe >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Frontend server stopped.
) else (
    echo No Node.js processes found.
)
echo.

echo Stopping Java processes (Backend)...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":8080"') do (
    taskkill /F /PID %%a >nul 2>nul
    if %ERRORLEVEL% EQU 0 (
        echo Backend server stopped.
    )
)
echo.

echo ========================================
echo  All servers stopped.
echo ========================================
pause
