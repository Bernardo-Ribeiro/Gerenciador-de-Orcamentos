@echo off
echo ========================================
echo Sistema de Orcamentos - Execucao de Testes
echo ========================================
echo.

cd sistema-orcamento

echo [1/3] Limpando build anterior...
call mvn clean

echo.
echo [2/3] Executando testes unitarios e de integracao...
call mvn test

echo.
echo [3/3] Gerando relatorio de cobertura Jacoco...
call mvn jacoco:report

echo.
echo ========================================
echo Testes concluidos!
echo ========================================
echo.
echo Relatorio de cobertura disponivel em:
echo %CD%\target\site\jacoco\index.html
echo.

pause