<#
.SYNOPSIS
  Run admin UI scenarios with coverage (CSM Host + Nop.Web on port 5000).

.DESCRIPTION
  1. Stops Docker nopcommerce (so CSM can start Windows Nop.Web under dotnet-coverage)
  2. Starts Coverage Session Manager Host on http://127.0.0.1:5055
  3. Runs admin scenarios from src/test/resources/features/admin (default: first 5 @TC-NC-T* cases)
  4. Optional: Roslyn map (Phase 3) after tests complete

  UI tests use http://localhost:5000 — not Docker port 80.

.EXAMPLE
  .\run-admin-coverage.ps1
  .\run-admin-coverage.ps1 -TestCaseLimit 10
  .\run-admin-coverage.ps1 -AllTests
  .\run-admin-coverage.ps1 -Tags "@TC-NC-T32001 or @TC-NC-T32002"
#>
param(
    [string]$Tags = "",
    [int]$TestCaseLimit = 5,
    [switch]$AllTests,
    [string]$BaseUrl = "http://localhost:5000",
    [switch]$SkipDockerStop,
    [switch]$SkipDockerStart,
    [switch]$SkipPhase3,
    [switch]$FailOnTestFailure,
    [switch]$CleanMaven
)

$ErrorActionPreference = "Stop"

$UiRoot = $PSScriptRoot
$RepoRoot = Resolve-Path (Join-Path $UiRoot "..")
$ToolsRoot = Join-Path $RepoRoot "tools\CoverageSessionManager"
. (Join-Path $ToolsRoot "scripts\use-dotnet-for-csm.ps1")
$Dotnet = Join-Path $env:USERPROFILE ".dotnet\dotnet.exe"
if (-not (Test-Path $Dotnet)) { $Dotnet = "dotnet" }

$hostExe = Join-Path $ToolsRoot "src\CoverageSessionManager.Host\bin\Release\net10.0\CoverageSessionManager.Host.exe"
$hostProj = Join-Path $ToolsRoot "src\CoverageSessionManager.Host\CoverageSessionManager.Host.csproj"
$cliProj = Join-Path $ToolsRoot "src\CoverageSessionManager.Cli\CoverageSessionManager.Cli.csproj"

function Stop-CsmHost {
    Get-Process -Name "CoverageSessionManager.Host" -ErrorAction SilentlyContinue | Stop-Process -Force
    Get-CimInstance Win32_Process -Filter "Name='dotnet.exe'" -ErrorAction SilentlyContinue |
        Where-Object { $_.CommandLine -match "CoverageSessionManager.Host" } |
        ForEach-Object { Stop-Process -Id $_.ProcessId -Force -ErrorAction SilentlyContinue }
}

function Wait-Health($Url, $Seconds = 60) {
    $deadline = (Get-Date).AddSeconds($Seconds)
    while ((Get-Date) -lt $deadline) {
        try {
            $r = Invoke-WebRequest -Uri $Url -UseBasicParsing -TimeoutSec 3
            if ($r.StatusCode -eq 200) { return $true }
        } catch { Start-Sleep -Seconds 1 }
    }
    return $false
}

function Get-LimitedTestCaseTags {
    param([int]$Limit)

    $featureDir = Join-Path $UiRoot "src\test\resources\features\admin"
    $selected = [System.Collections.Generic.List[string]]::new()
    $seen = @{}

    # Prefer one testcase per feature file for breadth across admin modules.
    Get-ChildItem $featureDir -Filter *.feature | Sort-Object Name | ForEach-Object {
        $match = Select-String -Path $_.FullName -Pattern '@(TC-NC-T\d+)' | Select-Object -First 1
        if ($match) {
            $id = $match.Matches[0].Groups[1].Value
            if (-not $seen[$id]) {
                $seen[$id] = $true
                [void]$selected.Add($id)
            }
        }
    }

    if ($selected.Count -lt $Limit) {
        Get-ChildItem $featureDir -Filter *.feature | ForEach-Object {
            Select-String -Path $_.FullName -Pattern '@(TC-NC-T\d+)' -AllMatches |
                ForEach-Object { $_.Matches } |
                ForEach-Object { $_.Groups[1].Value }
        } | Sort-Object -Unique | ForEach-Object {
            if ($selected.Count -ge $Limit) { return }
            if (-not $seen[$_]) {
                $seen[$_] = $true
                [void]$selected.Add($_)
            }
        }
    }

    $ids = @($selected | Select-Object -First $Limit)
    if ($ids.Count -eq 0) {
        throw "No @TC-NC-T* tags found in $featureDir"
    }

    return (($ids | ForEach-Object { "@$_" }) -join " or ")
}

if ($AllTests) {
    if (-not $Tags) {
        $Tags = "@admin and not @wip"
    }
} elseif (-not $Tags) {
    $Tags = Get-LimitedTestCaseTags -Limit $TestCaseLimit
}

Write-Host "=== Docker Postgres on localhost:5433 ===" -ForegroundColor Cyan
& powershell -NoProfile -ExecutionPolicy Bypass -File (Join-Path $ToolsRoot "scripts\ensure-postgres-for-coverage.ps1")
if ($LASTEXITCODE -ne 0) {
    throw "ensure-postgres-for-coverage.ps1 failed (exit $LASTEXITCODE)."
}

# Postgres needs a few seconds after container recreate before Nop.Web can connect.
$pgReady = $false
$pgDeadline = (Get-Date).AddSeconds(45)
while ((Get-Date) -lt $pgDeadline) {
    $tcp = New-Object System.Net.Sockets.TcpClient
    try {
        $tcp.Connect("127.0.0.1", 5433)
        $pgReady = $true
        break
    } catch {
        Start-Sleep -Seconds 2
    } finally {
        $tcp.Close()
    }
}
if (-not $pgReady) {
    throw "PostgreSQL is not reachable on localhost:5433. Fix Docker/WSL Postgres before running coverage."
}

Write-Host "=== Sync Windows Nop.Web DB config ===" -ForegroundColor Cyan
wsl docker start nopcommerce 2>&1 | Out-Null
Start-Sleep -Seconds 3
& powershell -NoProfile -ExecutionPolicy Bypass -File (Join-Path $ToolsRoot "scripts\sync-windows-appsettings.ps1") -RepoRoot $RepoRoot

Write-Host "=== Build Coverage Session Manager ===" -ForegroundColor Cyan
& $Dotnet build $hostProj -c Release -v q
& $Dotnet build $cliProj -c Release -v q

if (-not $SkipDockerStop) {
    Write-Host "=== Stopping Docker nopcommerce (CSM will start Nop.Web on $BaseUrl) ===" -ForegroundColor Cyan
    wsl docker stop nopcommerce 2>&1 | Out-Host
}

Stop-CsmHost
& $Dotnet run --project $cliProj --no-build -c Release -- cleanup --force | Out-Host

Write-Host "=== Starting CSM Host on http://127.0.0.1:5055 ===" -ForegroundColor Cyan
$hostProcess = Start-Process -FilePath $Dotnet -ArgumentList @(
    "run", "--project", $hostProj, "--no-build", "-c", "Release", "--urls", "http://127.0.0.1:5055"
) -WorkingDirectory $ToolsRoot -PassThru -WindowStyle Hidden

if (-not (Wait-Health "http://127.0.0.1:5055/health" 45)) {
    Stop-CsmHost
    throw "CSM Host did not become healthy on http://127.0.0.1:5055"
}

$sessionId = $null
$mvnExit = 0
try {
    Write-Host "=== Creating coverage session ===" -ForegroundColor Cyan
    try {
        $create = Invoke-RestMethod -Method Post -Uri "http://127.0.0.1:5055/sessions" -TimeoutSec 60
    } catch {
        throw "Failed to create coverage session via CSM Host: $($_.Exception.Message)"
    }
    $sessionId = $create.sessionId
    if (-not $sessionId) {
        throw "CSM Host did not return a session id."
    }
    Write-Host "  session:  $sessionId" -ForegroundColor Green

    Write-Host "=== Starting Nop.Web under dotnet-coverage on $BaseUrl (build may take several minutes) ===" -ForegroundColor Cyan
    $pgRecheck = New-Object System.Net.Sockets.TcpClient
    try {
        $pgRecheck.Connect("127.0.0.1", 5433)
    } catch {
        throw "PostgreSQL on localhost:5433 is not reachable before Nop.Web start. Run ensure-postgres-for-coverage.ps1."
    } finally {
        $pgRecheck.Close()
    }
    try {
        Invoke-WebRequest -Method Post -Uri "http://127.0.0.1:5055/sessions/$sessionId/start" -UseBasicParsing -TimeoutSec 600 | Out-Null
    } catch {
        throw "Failed to start coverage session ${sessionId}: $($_.Exception.Message)"
    }
    Write-Host "  Nop.Web is ready on $BaseUrl" -ForegroundColor Green

    Write-Host "=== Running admin UI tests (continue on failure) ===" -ForegroundColor Cyan
    Write-Host "  features: src/test/resources/features/admin"
    if ($AllTests) {
        Write-Host "  scope:    all @admin scenarios"
    } elseif ($Tags -match '@TC-NC-T') {
        $tcCount = ([regex]::Matches($Tags, '@TC-NC-T\d+')).Count
        Write-Host "  scope:    $tcCount testcase(s)"
    }
    Write-Host "  tags:     $Tags"
    Write-Host "  base.url: $BaseUrl"

    Push-Location $UiRoot
    $mvnArgs = @(
        "test", "-Padmin-all-coverage",
        "-Dcucumber.tags=$Tags",
        "-Dbase.url=$BaseUrl",
        "-Dcoverage.session.id=$sessionId"
    )
    if ($CleanMaven) { $mvnArgs = @("clean") + $mvnArgs }
    if ($FailOnTestFailure) { $mvnArgs += "-Dmaven.test.failure.ignore=false" }

    $logFile = Join-Path $UiRoot "admin-coverage-run.log"
    $prevEap = $ErrorActionPreference
    $ErrorActionPreference = "Continue"
    try {
        & mvn @mvnArgs 2>&1 | Tee-Object -FilePath $logFile
        $mvnExit = $LASTEXITCODE
    } finally {
        $ErrorActionPreference = $prevEap
    }
    Pop-Location

    Write-Host ""
    Write-Host "Coverage session: $sessionId" -ForegroundColor Green
    Write-Host "Artifacts: $RepoRoot\coverage-sessions\$sessionId" -ForegroundColor Green

    Write-Host "=== Stopping coverage session and collecting artifacts ===" -ForegroundColor Cyan
    try {
        Invoke-RestMethod -Method Post -Uri "http://127.0.0.1:5055/sessions/$sessionId/stop" -TimeoutSec 300 | Out-Null
    } catch {
        Write-Warning "Coverage stop failed for session ${sessionId}: $($_.Exception.Message)"
    }

    if (-not $SkipPhase3) {
        Write-Host "=== Phase 3: Roslyn coverage map ===" -ForegroundColor Cyan
        $mapScript = Join-Path $RepoRoot "tools\RoslynSymbolIndex\scripts\coverage-intelligence.ps1"
        & powershell -NoProfile -ExecutionPolicy Bypass -File $mapScript -SessionId $sessionId
    }

    if ($mvnExit -ne 0 -and $FailOnTestFailure) {
        throw "Maven test failed (exit $mvnExit). See $logFile"
    }
}
finally {
    if ($sessionId) {
        try {
            Invoke-RestMethod -Method Post -Uri "http://127.0.0.1:5055/sessions/$sessionId/stop" -TimeoutSec 120 -ErrorAction SilentlyContinue | Out-Null
        } catch { }
        & $Dotnet run --project $cliProj --no-build -c Release -- cleanup --force 2>&1 | Out-Null
    }

    if ($hostProcess -and -not $hostProcess.HasExited) {
        Stop-Process -Id $hostProcess.Id -Force -ErrorAction SilentlyContinue
    }
    Stop-CsmHost

    if (-not $SkipDockerStart -and -not $SkipDockerStop) {
        Write-Host "=== Starting Docker nopcommerce ===" -ForegroundColor Cyan
        wsl docker start nopcommerce 2>&1 | Out-Host
    }
}

Write-Host "=== Done ===" -ForegroundColor Cyan
