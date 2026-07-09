# Sistema de Orçamento para Gráfica

## 📦 Conteúdo da Pasta `dist/`

Esta pasta contém as versões compiladas e prontas para distribuição do sistema.

### 📁 `portatil/` - Versão Portátil

Para usuários que já possuem Java 21+ instalado.

**Conteúdo:**
- `sistema-orcamento-1.0-SNAPSHOT.jar` - Aplicação Java compilada
- `IniciarSistema.bat` - Script para iniciar o sistema
- `database/` - Banco de dados SQLite

**Como usar:**
1. Extraia a pasta `portatil` em qualquer local
2. Execute `IniciarSistema.bat`
3. O sistema será iniciado automaticamente

**Requisitos:**
- Java 21 ou superior instalado
- 4GB RAM mínimo recomendado

### 📁 `instalador/` - Instalador Windows

Para instalação completa no Windows (recomendado para usuários finais).

**Conteúdo:**
- `SetupSistemaOrcamento_1.0.exe` - Instalador automático

**Como usar:**
1. Execute o instalador
2. Siga as instruções na tela
3. O sistema será instalado em `C:\Program Files\Sistema de Orçamento`

**Requisitos:**
- Windows 10/11 64-bit
- Java 21 ou superior será verificado durante instalação
- 500MB de espaço em disco

---

## 🔧 Para Desenvolvedores

O código fonte completo está na pasta `sistema-orcamento/` na raiz do repositório.

### Compilar o sistema:
```bash
cd sistema-orcamento
mvn clean package
```

### Gerar instalador:
```bash
# Requer Inno Setup 6
.\build-installer.ps1
```

---

## 📋 Notas

- A versão portátil é ideal para testes e desenvolvimento
- O instalador é recomendado para distribuição a clientes
- Ambos requerem Java 21+ instalado no sistema