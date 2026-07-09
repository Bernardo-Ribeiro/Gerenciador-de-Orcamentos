@echo off
chcp 65001 >nul
echo ========================================
echo Sistema de Orcamento para Grafica
echo ========================================
echo.
echo Iniciando o sistema...
echo.

REM Verifica se o Java está instalado
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERRO] Java nao encontrado!
    echo.
    echo E necessario instalar o Java 21 JDK para executar este sistema.
    echo.
    echo Faca o download em:
    echo https://www.oracle.com/java/technologies/downloads/#jdk21-windows
    echo.
    echo Ou use o OpenJDK gratuito:
    echo https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM Executa o sistema com JavaFX (sem console)
javaw --enable-native-access=javafx.graphics --module-path "%~dp0lib\javafx" --add-modules javafx.controls,javafx.fxml -jar "%~dp0sistema-orcamento-1.0-SNAPSHOT.jar"

if %errorlevel% neq 0 (
    echo.
    echo [ERRO] Ocorreu um erro ao executar o sistema.
    echo Verifique se o Java 21 ou superior esta instalado.
    echo.
    pause
)