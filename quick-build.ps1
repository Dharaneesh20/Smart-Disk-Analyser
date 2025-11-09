# Quick Build Script - No Code Signing
# Run this with: pwsh -ExecutionPolicy Bypass .\quick-build.ps1

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Smart Disk Analyzer - Quick Build" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

$projectRoot = $PSScriptRoot

Write-Host "⚠ This build will NOT be code signed" -ForegroundColor Yellow
Write-Host "  Windows SmartScreen will show warnings" -ForegroundColor Yellow
Write-Host "  For production, run PowerShell as Administrator`n" -ForegroundColor Yellow

try {
    # Build backend
    Write-Host "[1/3] Building Backend..." -ForegroundColor Yellow
    Set-Location "$projectRoot\backend"
    if (!(Test-Path "target\disk-cleanup-partition-assistant-1.0.0.jar")) {
        mvn clean package -DskipTests -q
    }
    Write-Host "✓ Backend ready" -ForegroundColor Green

    # Build frontend
    Write-Host "`n[2/3] Building Frontend..." -ForegroundColor Yellow
    Set-Location "$projectRoot\frontend"
    if (!(Test-Path "build")) {
        npm run build
    }
    Write-Host "✓ Frontend ready" -ForegroundColor Green

    # Build Electron (directory only - no installer)
    Write-Host "`n[3/3] Building Electron App (portable)..." -ForegroundColor Yellow
    Set-Location "$projectRoot\electron"
    
    # Setup if needed
    if (!(Test-Path "node_modules")) {
        npm install
    }

    # Build directory only (no NSIS installer, no code signing needed)
    Write-Host "Creating portable app..." -ForegroundColor Cyan
    $env:CSC_IDENTITY_AUTO_DISCOVERY = "false"
    npx electron-builder --win --x64 --dir

    if ($LASTEXITCODE -eq 0) {
        Write-Host "`n========================================" -ForegroundColor Green
        Write-Host "✅ Build Complete!" -ForegroundColor Green
        Write-Host "========================================" -ForegroundColor Green

        Write-Host "`nPortable app created at:" -ForegroundColor White
        Write-Host "  $projectRoot\electron\dist\win-unpacked\`n" -ForegroundColor Cyan

        Write-Host "To run the app:" -ForegroundColor White
        Write-Host "  cd electron\dist\win-unpacked" -ForegroundColor Cyan
        Write-Host "  .\`"Smart Disk Analyzer.exe`"`n" -ForegroundColor Cyan

        Write-Host "⚠ Note: This is NOT an installer, just the app files" -ForegroundColor Yellow
        Write-Host "  To create an installer, run PowerShell as Administrator`n" -ForegroundColor Yellow
    } else {
        throw "Electron build failed!"
    }

} catch {
    Write-Host "`n❌ Build Failed!" -ForegroundColor Red
    Write-Host "Error: $_" -ForegroundColor Red
    Set-Location $projectRoot
    exit 1
}

Set-Location $projectRoot
