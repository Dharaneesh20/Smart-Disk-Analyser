# PowerShell script to rewrite Git history with correct author information
# This will change all commits from "Dharaneesh" to "Dharaneesh20"

$projectRoot = "c:\Users\Dharaneesh\Desktop\Smart Web Dashboard Disk Cleanup and Disk Partition assistant"
Set-Location $projectRoot

Write-Host "Rewriting Git history to use GitHub username 'Dharaneesh20'..." -ForegroundColor Green
Write-Host "This will update all 103 commits with the correct author information." -ForegroundColor Yellow
Write-Host ""

# Get GitHub email (you should replace this with your actual GitHub email)
$gitHubEmail = Read-Host "Enter your GitHub email address (the one associated with Dharaneesh20)"

if ([string]::IsNullOrWhiteSpace($gitHubEmail)) {
    Write-Host "Error: Email is required!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Updating commits with:" -ForegroundColor Cyan
Write-Host "  Username: Dharaneesh20" -ForegroundColor Cyan
Write-Host "  Email: $gitHubEmail" -ForegroundColor Cyan
Write-Host ""

# Backup current branch
$currentBranch = git branch --show-current
Write-Host "Current branch: $currentBranch" -ForegroundColor Yellow

# Rewrite history
Write-Host "Rewriting commit history..." -ForegroundColor Green

git filter-branch --env-filter "
    export GIT_AUTHOR_NAME='Dharaneesh20'
    export GIT_AUTHOR_EMAIL='$gitHubEmail'
    export GIT_COMMITTER_NAME='Dharaneesh20'
    export GIT_COMMITTER_EMAIL='$gitHubEmail'
" --tag-name-filter cat -- --branches --tags

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "Successfully rewritten Git history!" -ForegroundColor Green
    Write-Host ""
    Write-Host "To push the updated history to GitHub, run:" -ForegroundColor Yellow
    Write-Host "  git push --force origin main" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "WARNING: This will overwrite the history on GitHub!" -ForegroundColor Red
} else {
    Write-Host ""
    Write-Host "Error rewriting history. Please check the output above." -ForegroundColor Red
}
