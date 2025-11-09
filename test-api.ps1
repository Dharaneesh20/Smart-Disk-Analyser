# API Endpoint Testing Script
# Tests all backend endpoints to ensure functionality

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Smart Disk Analyzer - API Testing" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

$baseUrl = "http://localhost:8080/api"
$results = @()

function Test-Endpoint {
    param($name, $url, $method = "GET", $body = $null)
    
    Write-Host "Testing: $name..." -NoNewline
    
    try {
        $params = @{
            Uri = "$baseUrl$url"
            Method = $method
            TimeoutSec = 30
            ContentType = "application/json"
        }
        
        if ($body) {
            $params.Body = ($body | ConvertTo-Json)
        }
        
        $response = Invoke-RestMethod @params
        
        if ($response.success -eq $true) {
            Write-Host " ✓ PASS" -ForegroundColor Green
            return @{Name=$name; Status="PASS"; Response=$response}
        } else {
            Write-Host " ✗ FAIL (success=false)" -ForegroundColor Red
            return @{Name=$name; Status="FAIL"; Error="Response success=false"}
        }
    }
    catch {
        Write-Host " ✗ FAIL" -ForegroundColor Red
        return @{Name=$name; Status="FAIL"; Error=$_.Exception.Message}
    }
}

# Test 1: Health Check
$results += Test-Endpoint -name "Health Check" -url "/disk/health"

# Test 2: Get Statistics (empty initially)
$results += Test-Endpoint -name "Get Statistics" -url "/disk/statistics"

# Test 3: Scan Partitions
$results += Test-Endpoint -name "Scan Partitions" -url "/partition/scan"

# Test 4: Get Partition Health
$results += Test-Endpoint -name "Partition Health" -url "/partition/health"

# Test 5: List Partitions
$results += Test-Endpoint -name "List Partitions" -url "/partition/list"

# Test 6: Find Duplicates (will be empty without scan)
$results += Test-Endpoint -name "Find Duplicates" -url "/disk/duplicates"

# Test 7: Get Large Files
$results += Test-Endpoint -name "Get Large Files (>10MB)" -url "/disk/large-files?sizeThreshold=10485760"

# Test 8: Scan Directory (using temp folder)
$tempPath = $env:TEMP
$scanRequest = @{
    path = $tempPath
    includeHidden = $false
    maxDepth = 2
    fileExtensions = $null
}
$results += Test-Endpoint -name "Scan Directory ($tempPath)" -url "/disk/scan" -method "POST" -body $scanRequest

# Wait a bit for scan to complete
Write-Host "`nWaiting for scan to complete..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

# Test 9: Get Statistics (after scan)
$results += Test-Endpoint -name "Get Statistics (after scan)" -url "/disk/statistics"

# Test 10: Find Duplicates (after scan)
$results += Test-Endpoint -name "Find Duplicates (after scan)" -url "/disk/duplicates"

# Summary
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Test Results Summary" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

$passed = ($results | Where-Object {$_.Status -eq "PASS"}).Count
$failed = ($results | Where-Object {$_.Status -eq "FAIL"}).Count
$total = $results.Count

Write-Host "`nTotal Tests: $total" -ForegroundColor White
Write-Host "Passed: $passed" -ForegroundColor Green
Write-Host "Failed: $failed" -ForegroundColor $(if($failed -gt 0){"Red"}else{"Green"})

if ($failed -gt 0) {
    Write-Host "`nFailed Tests:" -ForegroundColor Red
    $results | Where-Object {$_.Status -eq "FAIL"} | ForEach-Object {
        Write-Host "  - $($_.Name): $($_.Error)" -ForegroundColor Red
    }
}

Write-Host "`n========================================`n" -ForegroundColor Cyan

# Return results
$results
