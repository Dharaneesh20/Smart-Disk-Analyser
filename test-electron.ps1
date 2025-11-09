# Test Script - Run Electron App in Development Mode
# This tests the app without building installers

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Smart Disk Analyzer - Development Test" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

$ErrorActionPreference = "Stop"
$projectRoot = $PSScriptRoot

try {
    # Step 1: Check if backend JAR exists
    Write-Host "[1/4] Checking backend..." -ForegroundColor Yellow
    $jarPath = "$projectRoot\backend\target\disk-cleanup-partition-assistant-1.0.0.jar"
    
    if (!(Test-Path $jarPath)) {
        Write-Host "Backend JAR not found. Building..." -ForegroundColor Yellow
        Set-Location "$projectRoot\backend"
        mvn clean package -DskipTests
        if ($LASTEXITCODE -ne 0) {
            throw "Backend build failed!"
        }
    }
    Write-Host "✓ Backend JAR ready" -ForegroundColor Green

    # Step 2: Check if frontend build exists
    Write-Host "`n[2/4] Checking frontend..." -ForegroundColor Yellow
    $frontendBuild = "$projectRoot\frontend\build"
    
    if (!(Test-Path $frontendBuild)) {
        Write-Host "Frontend build not found. Building..." -ForegroundColor Yellow
        Set-Location "$projectRoot\frontend"
        npm run build
        if ($LASTEXITCODE -ne 0) {
            throw "Frontend build failed!"
        }
    }
    Write-Host "✓ Frontend build ready" -ForegroundColor Green

    # Step 3: Setup Electron
    Write-Host "`n[3/4] Checking Electron..." -ForegroundColor Yellow
    Set-Location "$projectRoot\electron"
    if (!(Test-Path "node_modules")) {
        Write-Host "Installing Electron dependencies..." -ForegroundColor Yellow
        npm install
        if ($LASTEXITCODE -ne 0) {
            throw "Electron npm install failed!"
        }
    }
    Write-Host "✓ Electron ready" -ForegroundColor Green

    # Step 4: Start Electron App
    Write-Host "`n[4/4] Starting Electron App..." -ForegroundColor Yellow
    Write-Host "`nℹ The app will start in development mode" -ForegroundColor Cyan
    Write-Host "ℹ Backend will start automatically on port 8080" -ForegroundColor Cyan
    Write-Host "ℹ Close the app window when done testing" -ForegroundColor Cyan
    Write-Host "`nStarting...`n" -ForegroundColor Green
    
    Set-Location "$projectRoot\electron"
    npm start

} catch {
    Write-Host "`n❌ Test Failed!" -ForegroundColor Red
    Write-Host "Error: $_" -ForegroundColor Red
    Set-Location $projectRoot
    exit 1
}

Set-Location $projectRoot
