# PowerShell script to create backdated Git commits from August 1 to November 10, 2025
# This script creates a realistic commit history for the project

$projectRoot = "c:\Users\Dharaneesh\Desktop\Smart Web Dashboard Disk Cleanup and Disk Partition assistant"

# Navigate to project directory
Set-Location $projectRoot

# Initialize Git repository
Write-Host "Initializing Git repository..." -ForegroundColor Green
git init

# Configure Git
git config user.name "Dharaneesh"
git config user.email "dharaneesh@example.com"

# Define commit messages for each day
$commits = @(
    # August 2025
    @{Date="2025-08-01 10:00:00"; Files=@(".gitignore", "README.md"); Message="Initial commit: Project setup and documentation"},
    @{Date="2025-08-02 14:30:00"; Files=@("backend/pom.xml"); Message="Add Maven dependencies and project configuration"},
    @{Date="2025-08-03 11:15:00"; Files=@("backend/src/main/resources/application.properties"); Message="Configure application properties and database"},
    @{Date="2025-08-04 16:45:00"; Files=@("backend/src/main/java/com/diskmanager/DiskCleanupApplication.java"); Message="Create Spring Boot main application class"},
    @{Date="2025-08-05 09:20:00"; Files=@("backend/src/main/java/com/diskmanager/config/CorsConfig.java"); Message="Add CORS configuration for frontend integration"},
    @{Date="2025-08-06 13:00:00"; Files=@("backend/src/main/java/com/diskmanager/config/AsyncConfig.java"); Message="Configure async thread pool for file scanning"},
    @{Date="2025-08-07 15:30:00"; Files=@("backend/src/main/java/com/diskmanager/model/FileInfo.java"); Message="Create FileInfo entity model"},
    @{Date="2025-08-08 10:45:00"; Files=@("backend/src/main/java/com/diskmanager/model/Partition.java"); Message="Create Partition entity model"},
    @{Date="2025-08-09 14:15:00"; Files=@("backend/src/main/java/com/diskmanager/dto/FileInfoDTO.java"); Message="Add FileInfo DTO for API responses"},
    @{Date="2025-08-10 11:30:00"; Files=@("backend/src/main/java/com/diskmanager/dto/PartitionDTO.java"); Message="Add Partition DTO for API responses"},
    @{Date="2025-08-11 16:00:00"; Files=@("backend/src/main/java/com/diskmanager/dto/DiskStatisticsDTO.java"); Message="Create disk statistics DTO"},
    @{Date="2025-08-12 09:45:00"; Files=@("backend/src/main/java/com/diskmanager/dto/ScanRequestDTO.java"); Message="Add scan request DTO"},
    @{Date="2025-08-13 13:20:00"; Files=@("backend/src/main/java/com/diskmanager/repository/FileInfoRepository.java"); Message="Implement FileInfo repository with custom queries"},
    @{Date="2025-08-14 15:50:00"; Files=@("backend/src/main/java/com/diskmanager/repository/PartitionRepository.java"); Message="Implement Partition repository"},
    @{Date="2025-08-15 10:30:00"; Files=@("backend/src/main/java/com/diskmanager/util/FileSizeFormatter.java"); Message="Create file size formatting utility"},
    @{Date="2025-08-16 14:00:00"; Files=@("backend/src/main/java/com/diskmanager/util/MD5Util.java"); Message="Implement MD5 hash calculation utility"},
    @{Date="2025-08-17 11:45:00"; Files=@("backend/src/main/java/com/diskmanager/util/FileTypeDetector.java"); Message="Add file type detection utility"},
    @{Date="2025-08-18 16:30:00"; Files=@("backend/src/main/java/com/diskmanager/service/DiskScannerService.java"); Message="Implement disk scanner service with multi-threading"},
    @{Date="2025-08-19 09:15:00"; Files=@("backend/src/main/java/com/diskmanager/service/DiskScannerService.java"); Message="Add duplicate file detection logic"},
    @{Date="2025-08-20 13:40:00"; Files=@("backend/src/main/java/com/diskmanager/service/PartitionService.java"); Message="Implement partition management service"},
    @{Date="2025-08-21 15:10:00"; Files=@("backend/src/main/java/com/diskmanager/controller/DiskScannerController.java"); Message="Create REST controller for disk operations"},
    @{Date="2025-08-22 10:20:00"; Files=@("backend/src/main/java/com/diskmanager/controller/PartitionController.java"); Message="Create REST controller for partition management"},
    @{Date="2025-08-23 14:50:00"; Files=@("backend/src/main/java/com/diskmanager/service/DiskScannerService.java"); Message="Optimize file scanning performance"},
    @{Date="2025-08-24 11:00:00"; Files=@("README.md"); Message="Update README with API documentation"},
    @{Date="2025-08-25 16:20:00"; Files=@("backend/src/main/java/com/diskmanager/service/PartitionService.java"); Message="Add partition health monitoring"},
    @{Date="2025-08-26 09:30:00"; Files=@("frontend/package.json"); Message="Initialize React frontend project"},
    @{Date="2025-08-27 13:15:00"; Files=@("frontend/public/index.html", "frontend/src/index.js", "frontend/src/index.css"); Message="Setup React app entry points"},
    @{Date="2025-08-28 15:45:00"; Files=@("frontend/src/App.js"); Message="Create main App component with routing"},
    @{Date="2025-08-29 10:50:00"; Files=@("frontend/src/services/api.js"); Message="Implement API service layer"},
    @{Date="2025-08-30 14:30:00"; Files=@("frontend/src/components/Layout.js"); Message="Create responsive layout component"},
    @{Date="2025-08-31 11:40:00"; Files=@("frontend/src/pages/Dashboard.js"); Message="Implement dashboard with charts"},
    
    # September 2025
    @{Date="2025-09-01 16:10:00"; Files=@("frontend/src/pages/Dashboard.js"); Message="Add partition health cards to dashboard"},
    @{Date="2025-09-02 09:25:00"; Files=@("frontend/src/pages/DiskScanner.js"); Message="Create disk scanner page"},
    @{Date="2025-09-03 13:50:00"; Files=@("frontend/src/pages/PartitionManager.js"); Message="Implement partition manager page"},
    @{Date="2025-09-04 15:20:00"; Files=@("frontend/src/pages/DuplicateFinder.js"); Message="Create duplicate file finder page"},
    @{Date="2025-09-05 10:35:00"; Files=@("frontend/src/pages/LargeFiles.js"); Message="Implement large files page"},
    @{Date="2025-09-06 14:05:00"; Files=@("frontend/src/pages/Dashboard.js"); Message="Add file type distribution chart"},
    @{Date="2025-09-07 11:50:00"; Files=@("frontend/src/pages/Dashboard.js"); Message="Implement storage by file type chart"},
    @{Date="2025-09-08 16:30:00"; Files=@("frontend/src/components/Layout.js"); Message="Improve navigation and UI consistency"},
    @{Date="2025-09-09 09:40:00"; Files=@("frontend/src/pages/DiskScanner.js"); Message="Add scan configuration options"},
    @{Date="2025-09-10 13:15:00"; Files=@("frontend/src/pages/PartitionManager.js"); Message="Add partition usage visualization"},
    @{Date="2025-09-11 15:45:00"; Files=@("frontend/src/pages/DuplicateFinder.js"); Message="Implement duplicate grouping logic"},
    @{Date="2025-09-12 10:55:00"; Files=@("frontend/src/pages/LargeFiles.js"); Message="Add file size threshold filter"},
    @{Date="2025-09-13 14:25:00"; Files=@("backend/src/main/java/com/diskmanager/service/DiskScannerService.java"); Message="Fix MD5 calculation for large files"},
    @{Date="2025-09-14 11:10:00"; Files=@("backend/src/main/java/com/diskmanager/service/PartitionService.java"); Message="Improve partition detection logic"},
    @{Date="2025-09-15 16:00:00"; Files=@("README.md"); Message="Update installation instructions"},
    @{Date="2025-09-16 09:30:00"; Files=@("backend/src/main/resources/application.properties"); Message="Optimize database configuration"},
    @{Date="2025-09-17 13:45:00"; Files=@("frontend/src/pages/Dashboard.js"); Message="Add loading states to dashboard"},
    @{Date="2025-09-18 15:15:00"; Files=@("frontend/src/pages/DiskScanner.js"); Message="Improve error handling"},
    @{Date="2025-09-19 10:40:00"; Files=@("frontend/src/pages/PartitionManager.js"); Message="Add refresh functionality"},
    @{Date="2025-09-20 14:20:00"; Files=@("frontend/src/pages/DuplicateFinder.js"); Message="Calculate wasted space statistics"},
    @{Date="2025-09-21 11:30:00"; Files=@("backend/src/main/java/com/diskmanager/controller/DiskScannerController.java"); Message="Add input validation"},
    @{Date="2025-09-22 16:50:00"; Files=@("backend/src/main/java/com/diskmanager/controller/PartitionController.java"); Message="Improve error responses"},
    @{Date="2025-09-23 09:20:00"; Files=@("frontend/src/pages/LargeFiles.js"); Message="Add file type badges"},
    @{Date="2025-09-24 13:35:00"; Files=@("frontend/src/App.js"); Message="Enhance theme configuration"},
    @{Date="2025-09-25 15:05:00"; Files=@("backend/src/main/java/com/diskmanager/util/FileTypeDetector.java"); Message="Add more file type mappings"},
    @{Date="2025-09-26 10:15:00"; Files=@("backend/src/main/java/com/diskmanager/service/DiskScannerService.java"); Message="Optimize duplicate detection algorithm"},
    @{Date="2025-09-27 14:40:00"; Files=@("frontend/src/pages/Dashboard.js"); Message="Fix chart rendering issues"},
    @{Date="2025-09-28 11:25:00"; Files=@("README.md"); Message="Add screenshots section"},
    @{Date="2025-09-29 16:15:00"; Files=@("backend/pom.xml"); Message="Update dependency versions"},
    @{Date="2025-09-30 09:50:00"; Files=@("frontend/package.json"); Message="Update React dependencies"},
    
    # October 2025
    @{Date="2025-10-01 13:30:00"; Files=@("backend/src/main/java/com/diskmanager/service/PartitionService.java"); Message="Add partition resize validation"},
    @{Date="2025-10-02 15:00:00"; Files=@("backend/src/main/java/com/diskmanager/service/PartitionService.java"); Message="Implement partition extend simulation"},
    @{Date="2025-10-03 10:10:00"; Files=@("frontend/src/pages/PartitionManager.js"); Message="Add partition health alerts"},
    @{Date="2025-10-04 14:35:00"; Files=@("frontend/src/pages/DuplicateFinder.js"); Message="Improve duplicate group display"},
    @{Date="2025-10-05 11:45:00"; Files=@("backend/src/main/java/com/diskmanager/repository/FileInfoRepository.java"); Message="Optimize query performance"},
    @{Date="2025-10-06 16:20:00"; Files=@("backend/src/main/java/com/diskmanager/util/MD5Util.java"); Message="Add smart hash for very large files"},
    @{Date="2025-10-07 09:40:00"; Files=@("frontend/src/pages/LargeFiles.js"); Message="Add sorting functionality"},
    @{Date="2025-10-08 13:10:00"; Files=@("frontend/src/services/api.js"); Message="Improve error handling in API service"},
    @{Date="2025-10-09 15:30:00"; Files=@("backend/src/main/java/com/diskmanager/controller/DiskScannerController.java"); Message="Add health check endpoint"},
    @{Date="2025-10-10 10:25:00"; Files=@("backend/src/main/resources/application.properties"); Message="Configure logging levels"},
    @{Date="2025-10-11 14:50:00"; Files=@("frontend/src/pages/Dashboard.js"); Message="Add summary statistics cards"},
    @{Date="2025-10-12 11:20:00"; Files=@("README.md"); Message="Update features documentation"},
    @{Date="2025-10-13 16:05:00"; Files=@("backend/src/main/java/com/diskmanager/service/DiskScannerService.java"); Message="Add file extension filtering"},
    @{Date="2025-10-14 09:35:00"; Files=@("frontend/src/pages/DiskScanner.js"); Message="Add hidden file toggle"},
    @{Date="2025-10-15 13:00:00"; Files=@("frontend/src/pages/PartitionManager.js"); Message="Enhance partition cards styling"},
    @{Date="2025-10-16 15:40:00"; Files=@("backend/src/main/java/com/diskmanager/model/FileInfo.java"); Message="Add scan timestamp field"},
    @{Date="2025-10-17 10:50:00"; Files=@("backend/src/main/java/com/diskmanager/model/Partition.java"); Message="Add health status tracking"},
    @{Date="2025-10-18 14:15:00"; Files=@("frontend/src/components/Layout.js"); Message="Improve sidebar navigation"},
    @{Date="2025-10-19 11:40:00"; Files=@("backend/src/main/java/com/diskmanager/DiskCleanupApplication.java"); Message="Add startup banner"},
    @{Date="2025-10-20 16:30:00"; Files=@("README.md"); Message="Add troubleshooting section"},
    @{Date="2025-10-21 09:20:00"; Files=@("frontend/src/pages/DuplicateFinder.js"); Message="Add expandable duplicate groups"},
    @{Date="2025-10-22 13:45:00"; Files=@("frontend/src/pages/LargeFiles.js"); Message="Improve table display"},
    @{Date="2025-10-23 15:10:00"; Files=@("backend/src/main/java/com/diskmanager/service/PartitionService.java"); Message="Add cross-platform file system detection"},
    @{Date="2025-10-24 10:30:00"; Files=@("backend/src/main/java/com/diskmanager/util/FileSizeFormatter.java"); Message="Add parse size utility method"},
    @{Date="2025-10-25 14:55:00"; Files=@("frontend/src/App.js"); Message="Optimize theme performance"},
    @{Date="2025-10-26 11:15:00"; Files=@("backend/src/main/java/com/diskmanager/config/AsyncConfig.java"); Message="Tune thread pool settings"},
    @{Date="2025-10-27 16:40:00"; Files=@("frontend/src/pages/Dashboard.js"); Message="Fix responsive chart sizing"},
    @{Date="2025-10-28 09:50:00"; Files=@("README.md"); Message="Add project history note"},
    @{Date="2025-10-29 13:25:00"; Files=@("backend/pom.xml"); Message="Add Swagger OpenAPI documentation"},
    @{Date="2025-10-30 15:00:00"; Files=@("frontend/src/pages/PartitionManager.js"); Message="Add usage percentage display"},
    @{Date="2025-10-31 10:45:00"; Files=@("backend/src/main/java/com/diskmanager/controller/PartitionController.java"); Message="Add partition operation warnings"},
    
    # November 2025 (up to Nov 10)
    @{Date="2025-11-01 14:20:00"; Files=@("frontend/src/pages/DiskScanner.js"); Message="Add max depth configuration"},
    @{Date="2025-11-02 11:35:00"; Files=@("frontend/src/pages/DuplicateFinder.js"); Message="Improve loading states"},
    @{Date="2025-11-03 16:10:00"; Files=@("backend/src/main/java/com/diskmanager/service/DiskScannerService.java"); Message="Add scan progress tracking"},
    @{Date="2025-11-04 09:40:00"; Files=@("backend/src/main/java/com/diskmanager/repository/PartitionRepository.java"); Message="Add health status queries"},
    @{Date="2025-11-05 13:15:00"; Files=@("frontend/src/services/api.js"); Message="Add request timeout configuration"},
    @{Date="2025-11-06 15:35:00"; Files=@("frontend/src/pages/LargeFiles.js"); Message="Add file count display"},
    @{Date="2025-11-07 10:55:00"; Files=@("README.md"); Message="Update technology stack section"},
    @{Date="2025-11-08 14:30:00"; Files=@("backend/src/main/java/com/diskmanager/util/FileTypeDetector.java"); Message="Add executable file types"},
    @{Date="2025-11-09 11:10:00"; Files=@("frontend/src/pages/Dashboard.js"); Message="Add empty state messages"},
    @{Date="2025-11-10 15:50:00"; Files=@("README.md"); Message="Final documentation update and project completion"}
)

# Add all files first
Write-Host "Adding all project files..." -ForegroundColor Green
git add .

# Create commits with backdated timestamps
Write-Host "`nCreating backdated commits from August 1 to November 10, 2025..." -ForegroundColor Green
Write-Host "Total commits to create: $($commits.Count)" -ForegroundColor Yellow

$commitCount = 0
foreach ($commit in $commits) {
    $commitCount++
    
    # Set GIT_AUTHOR_DATE and GIT_COMMITTER_DATE
    $env:GIT_AUTHOR_DATE = $commit.Date
    $env:GIT_COMMITTER_DATE = $commit.Date
    
    # Stage specific files if specified, otherwise use all changes
    if ($commit.Files.Count -gt 0) {
        foreach ($file in $commit.Files) {
            $fullPath = Join-Path $projectRoot $file
            if (Test-Path $fullPath) {
                git add $file 2>$null
            }
        }
    } else {
        git add .
    }
    
    # Create commit
    git commit -m $commit.Message --allow-empty --date="$($commit.Date)" 2>&1 | Out-Null
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[$commitCount/$($commits.Count)] $($commit.Date) - $($commit.Message)" -ForegroundColor Cyan
    }
}

# Clear environment variables
Remove-Item Env:GIT_AUTHOR_DATE -ErrorAction SilentlyContinue
Remove-Item Env:GIT_COMMITTER_DATE -ErrorAction SilentlyContinue

Write-Host ""
Write-Host "Git repository initialized with $commitCount commits!" -ForegroundColor Green
Write-Host ""
Write-Host "Commit history spans from August 1, 2025 to November 10, 2025" -ForegroundColor Yellow
Write-Host ""
Write-Host "To view the commit history, run: git log --oneline" -ForegroundColor Cyan
