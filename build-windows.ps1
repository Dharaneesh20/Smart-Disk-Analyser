# Build Script for Windows Application
# This script builds the complete Windows application with embedded JRE

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Smart Disk Analyzer - Windows Build" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

$ErrorActionPreference = "Stop"
$projectRoot = $PSScriptRoot

# Step 1: Build Backend
Write-Host "[1/5] Building Backend (Maven)..." -ForegroundColor Yellow
Set-Location "$projectRoot\backend"
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Backend build failed!" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Backend built successfully" -ForegroundColor Green

# Step 2: Build Frontend
Write-Host "`n[2/5] Building Frontend (React)..." -ForegroundColor Yellow
Set-Location "$projectRoot\frontend"
npm run build
if ($LASTEXITCODE -ne 0) {
    Write-Host "Frontend build failed!" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Frontend built successfully" -ForegroundColor Green

# Step 3: Setup Electron
Write-Host "`n[3/5] Setting up Electron..." -ForegroundColor Yellow
Set-Location "$projectRoot\electron"
if (!(Test-Path "node_modules")) {
    npm install
}
Write-Host "✓ Electron setup complete" -ForegroundColor Green

# Step 4: Download JRE (if not exists)
Write-Host "`n[4/5] Checking for JRE..." -ForegroundColor Yellow
$jrePath = "$projectRoot\electron\jre"
if (!(Test-Path $jrePath)) {
    Write-Host "JRE not found. Please download JRE 17+ and extract to:" -ForegroundColor Yellow
    Write-Host "  $jrePath" -ForegroundColor White
    Write-Host "`nYou can download from:" -ForegroundColor Yellow
    Write-Host "  https://adoptium.net/temurin/releases/?version=17" -ForegroundColor Cyan
    Write-Host "`nExtract the 'jdk-17.x.x-jre' folder and rename it to 'jre'" -ForegroundColor Yellow
    
    $response = Read-Host "`nSkip JRE bundling? (Y/N)"
    if ($response -ne 'Y' -and $response -ne 'y') {
        exit 1
    }
    Write-Host "⚠ Building without embedded JRE (requires Java on target system)" -ForegroundColor Yellow
} else {
    Write-Host "✓ JRE found at $jrePath" -ForegroundColor Green
}

# Step 5: Build Electron App
Write-Host "`n[5/5] Building Electron Application..." -ForegroundColor Yellow
Set-Location "$projectRoot\electron"

# Build for Windows x86 (32-bit)
Write-Host "Building for Windows x86 (32-bit)..." -ForegroundColor Cyan
npm run build:win32
if ($LASTEXITCODE -ne 0) {
    Write-Host "Windows x86 build failed!" -ForegroundColor Red
    exit 1
}

# Build for Windows x64 (64-bit)
Write-Host "Building for Windows x64 (64-bit)..." -ForegroundColor Cyan
npm run build:win
if ($LASTEXITCODE -ne 0) {
    Write-Host "Windows x64 build failed!" -ForegroundColor Red
    exit 1
}

Write-Host "`n========================================" -ForegroundColor Green
Write-Host "Build Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

Write-Host "`nBuilt applications can be found in:" -ForegroundColor White
Write-Host "  $projectRoot\electron\dist" -ForegroundColor Cyan

Write-Host "`nAvailable installers:" -ForegroundColor White
Get-ChildItem "$projectRoot\electron\dist" -Filter "*.exe" | ForEach-Object {
    $size = [math]::Round($_.Length / 1MB, 2)
    Write-Host "  - $($_.Name) ($size MB)" -ForegroundColor Green
}

Write-Host "`n" -ForegroundColor White
