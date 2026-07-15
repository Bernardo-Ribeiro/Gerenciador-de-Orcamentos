# Script para compilar o instalador do Sistema de Orçamento
# Requer: Inno Setup instalado

$issFile = Join-Path $PSScriptRoot "installer.iss"
$isccPath = "C:\Program Files (x86)\Inno Setup 6\ISCC.exe"

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Sistema de Orcamento - Compilador de Instalador" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""

# Verifica se o Inno Setup está instalado
if (Test-Path $isccPath) {
    Write-Host "[OK] Inno Setup encontrada: $isccPath" -ForegroundColor Green
} else {
    # Tenta encontrar em outros locais comuns
    $possiblePaths = @(
        "C:\Program Files\Inno Setup 7\ISCC.exe",
        "C:\Program Files\Inno Setup 6\ISCC.exe",
        "C:\Program Files (x86)\Inno Setup 5\ISCC.exe",
        "C:\Program Files\Inno Setup 5\ISCC.exe"
    )
    
    foreach ($path in $possiblePaths) {
        if (Test-Path $path) {
            $isccPath = $path
            Write-Host "[OK] Inno Setup encontrada: $isccPath" -ForegroundColor Green
            break
        }
    }
    
    if (-not (Test-Path $isccPath)) {
        Write-Host "[ERRO] Inno Setup nao encontrado!" -ForegroundColor Red
        Write-Host ""
        Write-Host "Para criar o instalador, voce precisa:" -ForegroundColor Yellow
        Write-Host "1. Baixar o Inno Setup: https://jrsoftware.org/isdl.php" -ForegroundColor Yellow
        Write-Host "2. Instalar o Inno Setup" -ForegroundColor Yellow
        Write-Host "3. Executar este script novamente" -ForegroundColor Yellow
        Write-Host ""
        exit 1
    }
}

# Verifica se o arquivo .iss existe
if (-not (Test-Path $issFile)) {
    Write-Host "[ERRO] Arquivo installer.iss nao encontrado em: $issFile" -ForegroundColor Red
    exit 1
}

# Verifica se o executavel foi gerado
$exePath = Join-Path $PSScriptRoot "..\sistema-orcamento\target\SistemaOrcamento.exe"
if (-not (Test-Path $exePath)) {
    Write-Host "[ERRO] Executavel nao encontrado. Execute primeiro:" -ForegroundColor Red
    Write-Host "  cd ..\sistema-orcamento" -ForegroundColor Yellow
    Write-Host "  mvn clean package" -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "[INFO] Compilando instalador..." -ForegroundColor Cyan
Write-Host ""

# Compila o instalador
try {
    & $isccPath $issFile
    Write-Host ""
    Write-Host "=========================================" -ForegroundColor Green
    Write-Host "Instalador compilado com sucesso!" -ForegroundColor Green
    Write-Host "=========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Local do instalador:" -ForegroundColor Cyan
    Write-Host "  ..\dist\instalador\SetupSistemaOrcamento_1.0.exe" -ForegroundColor White
    Write-Host ""
} catch {
    Write-Host ""
    Write-Host "[ERRO] Falha ao compilar o instalador:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}