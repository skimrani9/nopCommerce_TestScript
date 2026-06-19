param(
    [switch]$Coverage,
    [string]$BaseUrl = "",
    [string]$Tags = "@admin and not @wip",
    [switch]$FailOnTestFailure
)

# CSM starts Nop.Web on port 5000; Docker uses port 80.
if ($Coverage -and -not $BaseUrl) {
    $BaseUrl = "http://localhost:5000"
}

$ErrorActionPreference = "Stop"

Set-Location $PSScriptRoot

$coverageSessionId = ""
if ($Coverage) {
    $repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
    $tools = Join-Path $repoRoot "tools\CoverageSessionManager\scripts"
    $ensurePg = Join-Path $tools "ensure-postgres-for-coverage.ps1"
    $syncScript = Join-Path $tools "sync-windows-appsettings.ps1"
    if (Test-Path $ensurePg) {
        & powershell -NoProfile -ExecutionPolicy Bypass -File $ensurePg
    }
    if (Test-Path $syncScript) {
        & powershell -NoProfile -ExecutionPolicy Bypass -File $syncScript -RepoRoot $repoRoot
    }

    $lockFile = Join-Path $repoRoot "coverage-sessions\.active-session.lock"
    $storeListening = Get-NetTCPConnection -LocalPort 5000 -State Listen -ErrorAction SilentlyContinue
    if ((Test-Path $lockFile) -and $storeListening) {
        $coverageSessionId = (Get-Content $lockFile -Raw).Trim()
        Write-Host "Reusing active coverage session: $coverageSessionId (Nop.Web on :5000)"
    } else {
        try {
            $health = Invoke-WebRequest http://127.0.0.1:5055/health -UseBasicParsing -TimeoutSec 3
            if ($health.StatusCode -eq 200) {
                Write-Host "CSM Host is up; tests will create or join a coverage session."
            }
        } catch {
            Write-Warning "CSM Host not running on http://127.0.0.1:5055. Start: tools\CoverageSessionManager\run-csm.ps1 host"
        }
    }
}

$mvnArgs = @("test", "-Padmin-all")
if ($Coverage) {
    $mvnArgs = @("test", "-Padmin-all-coverage")
}

$mvnArgs += "-Dcucumber.filter.tags=$Tags"

if ($BaseUrl) {
    $mvnArgs += "-Dbase.url=$BaseUrl"
}

if ($Coverage -and $coverageSessionId) {
    $mvnArgs += "-Dcoverage.session.id=$coverageSessionId"
}

if ($FailOnTestFailure) {
    $mvnArgs += "-Dmaven.test.failure.ignore=false"
}

& mvn @mvnArgs
