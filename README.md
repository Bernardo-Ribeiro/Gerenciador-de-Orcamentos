# Sistema de Orçamento para Gráfica

Sistema de gestão de orçamentos para gráficas, desenvolvido em Java com JavaFX.

## 🚀 Instalação e Execução

### Opção 1: Versão Portátil (Recomendado para Desenvolvimento)

1. Baixe ou clone o repositório
2. Navegue até a pasta `dist/portatil/`
3. Execute `IniciarSistema.bat`

**Requisitos:**
- Java 21 ou superior instalado

### Opção 2: Instalador Windows (Recomendado para Usuários Finais)

1. Baixe o instalador em `dist/instalador/`
2. Execute `SetupSistemaOrcamento_1.0.exe`
3. Siga as instruções de instalação

**Requisitos:**
- Windows 10/11 64-bit
- Java 21 ou superior (verificado durante instalação)

## 🔧 Compilando do Código Fonte

### Pré-requisitos
- Java 21 JDK
- Maven 3.6+
- (Opcional) Inno Setup 6 para criar o instalador

### Passos

```bash
# Compilar o projeto
cd sistema-orcamento
mvn clean package

# O executável e JAR serão gerados em:
# sistema-orcamento/target/SistemaOrcamento.exe
# sistema-orcamento/target/sistema-orcamento-1.0-SNAPSHOT.jar
```

### Criar Instalador

```bash
# Requer Inno Setup 6 instalado
cd instalador
.\build-installer.ps1

# O instalador será gerado em:
# dist/instalador/SetupSistemaOrcamento_1.0.exe
```

## 📁 Estrutura do Projeto

```
Gerenciador-de-Orcamentos/
├── dist/
│   ├── README.md              # Guia de distribuição
│   ├── portatil/              # Versão portátil (JAR + scripts)
│   └── instalador/            # Instalador Windows (.exe)
├── instalador/
│   ├── installer.iss          # Script Inno Setup
│   └── build-installer.ps1    # Script de build
├── sistema-orcamento/         # Código fonte completo
│   ├── src/                   # Código Java
│   ├── database/              # Banco de dados SQLite
│   ├── target/                # Compilados (gerado pelo Maven)
│   └── pom.xml                # Configuração Maven
├── .env                       # Variáveis de ambiente
├── .gitignore
└── README.md                  # Este arquivo
```

## 📋 Notas

- O banco de dados SQLite está localizado em `database/`
- A versão portátil é ideal para testes e desenvolvimento
- O instalador é recomendado para distribuição a clientes finais
- Ambos requerem Java 21+ instalado no sistema

## 📄 Licença

Projeto desenvolvido para uso interno da gráfica.