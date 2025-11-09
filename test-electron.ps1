# Test Electron App - Development Mode
# Run this with: pwsh -ExecutionPolicy Bypass .\test-electron.ps1

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Smart Disk Analyzer - Test Mode" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

$projectRoot = $PSScriptRoot

Write-Host "[INFO] This will launch the app in development mode" -ForegroundColor Cyan
Write-Host "  No installer will be created" -ForegroundColor Cyan
Write-Host "  Press Ctrl+C to stop`n" -ForegroundColor Cyan

try {
    # Check backend JAR
    Write-Host "[1/3] Checking Backend..." -ForegroundColor Yellow
    $backendJar = "$projectRoot\backend\target\disk-cleanup-partition-assistant-1.0.0.jar"
    if (!(Test-Path $backendJar)) {
        Write-Host "  Backend JAR not found, building..." -ForegroundColor Yellow
        Set-Location "$projectRoot\backend"
        mvn clean package -DskipTests -q
    }
    Write-Host "[OK] Backend ready" -ForegroundColor Green

    # Check frontend build
    Write-Host "`n[2/3] Checking Frontend..." -ForegroundColor Yellow
    $frontendBuild = "$projectRoot\frontend\build"
    if (!(Test-Path $frontendBuild)) {
        Write-Host "  Frontend build not found, building..." -ForegroundColor Yellow
        Set-Location "$projectRoot\frontend"
        npm run build
    }
    Write-Host "[OK] Frontend ready" -ForegroundColor Green

    # Launch Electron
    Write-Host "`n[3/3] Launching Electron..." -ForegroundColor Yellow
    Set-Location "$projectRoot\electron"
    
    if (!(Test-Path "node_modules")) {
        Write-Host "  Installing dependencies..." -ForegroundColor Yellow
        npm install
    }

    Write-Host "`n========================================" -ForegroundColor Green
    Write-Host "Starting Smart Disk Analyzer..." -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "`n[INFO] Press Ctrl+C to stop the app`n" -ForegroundColor Cyan

    # Start in development mode
    npm start

} catch {
    Write-Host "`n[ERROR] Test Failed!" -ForegroundColor Red
    Write-Host "Error: $_" -ForegroundColor Red
    Set-Location $projectRoot
    exit 1
}

Set-Location $projectRoot
