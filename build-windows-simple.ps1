# Simplified Build Script for Windows Application
# Builds without requiring JRE to be bundled (will use system Java)

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Smart Disk Analyzer - Windows Build" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

$ErrorActionPreference = "Stop"
$projectRoot = $PSScriptRoot

try {
    # Step 1: Build Backend
    Write-Host "[1/5] Building Backend (Maven)..." -ForegroundColor Yellow
    Set-Location "$projectRoot\backend"
    mvn clean package -DskipTests
    if ($LASTEXITCODE -ne 0) {
        throw "Backend build failed!"
    }
    Write-Host "✓ Backend built successfully" -ForegroundColor Green

    # Step 2: Build Frontend
    Write-Host "`n[2/5] Building Frontend (React)..." -ForegroundColor Yellow
    Set-Location "$projectRoot\frontend"
    npm run build
    if ($LASTEXITCODE -ne 0) {
        throw "Frontend build failed!"
    }
    Write-Host "✓ Frontend built successfully" -ForegroundColor Green

    # Step 3: Setup Electron
    Write-Host "`n[3/5] Setting up Electron..." -ForegroundColor Yellow
    Set-Location "$projectRoot\electron"
    if (!(Test-Path "node_modules")) {
        npm install
        if ($LASTEXITCODE -ne 0) {
            throw "Electron npm install failed!"
        }
    }
    Write-Host "✓ Electron setup complete" -ForegroundColor Green

    # Step 4: Handle JRE (Optional)
    Write-Host "`n[4/5] Checking for JRE..." -ForegroundColor Yellow
    $jrePath = "$projectRoot\electron\jre"
    
    if (Test-Path $jrePath) {
        Write-Host "✓ JRE found at $jrePath - will be bundled" -ForegroundColor Green
        $bundleJRE = $true
    } else {
        Write-Host "⚠ JRE not found - building without embedded JRE" -ForegroundColor Yellow
        Write-Host "  Application will require Java 17+ installed on target system" -ForegroundColor Yellow
        Write-Host "`n  To bundle JRE, download from:" -ForegroundColor Cyan
        Write-Host "  https://adoptium.net/temurin/releases/?version=17" -ForegroundColor Cyan
        Write-Host "  Extract to: $jrePath`n" -ForegroundColor Cyan
        
        # Remove JRE from extraResources if not present
        $packageJsonPath = "$projectRoot\electron\package.json"
        $packageJson = Get-Content $packageJsonPath -Raw | ConvertFrom-Json
        
        # Filter out JRE resource
        $packageJson.build.extraResources = $packageJson.build.extraResources | Where-Object {
            $_.from -ne "jre"
        }
        
        $packageJson | ConvertTo-Json -Depth 10 | Set-Content $packageJsonPath
        Write-Host "✓ Configured build without JRE" -ForegroundColor Green
        $bundleJRE = $false
    }

    # Step 5: Build Electron App
    Write-Host "`n[5/5] Building Electron Application..." -ForegroundColor Yellow
    Set-Location "$projectRoot\electron"

    # Build for Windows x64 (64-bit) - Most common
    Write-Host "Building for Windows x64 (64-bit)..." -ForegroundColor Cyan
    npm run build:win
    if ($LASTEXITCODE -ne 0) {
        throw "Windows x64 build failed!"
    }
    Write-Host "✓ Windows x64 build complete" -ForegroundColor Green

    Write-Host "`n========================================" -ForegroundColor Green
    Write-Host "✅ Build Complete!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green

    Write-Host "`nBuilt applications can be found in:" -ForegroundColor White
    Write-Host "  $projectRoot\electron\dist`n" -ForegroundColor Cyan

    if (Test-Path "$projectRoot\electron\dist") {
        Write-Host "Available installers:" -ForegroundColor White
        Get-ChildItem "$projectRoot\electron\dist" -Filter "*.exe" | ForEach-Object {
            $size = [math]::Round($_.Length / 1MB, 2)
            Write-Host "  ✓ $($_.Name) ($size MB)" -ForegroundColor Green
        }
    }

    if (!$bundleJRE) {
        Write-Host "`n⚠ IMPORTANT: This build requires Java 17+ on target systems!" -ForegroundColor Yellow
        Write-Host "  Users must have Java installed to run the application." -ForegroundColor Yellow
    }

    Write-Host "`n" -ForegroundColor White

} catch {
    Write-Host "`n❌ Build Failed!" -ForegroundColor Red
    Write-Host "Error: $_" -ForegroundColor Red
    Set-Location $projectRoot
    exit 1
}

Set-Location $projectRoot
