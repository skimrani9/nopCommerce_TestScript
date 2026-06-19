$ErrorActionPreference = "Stop"

Set-Location $PSScriptRoot

mvn test "-Dcucumber.filter.tags=@admin-products and not @wip"
