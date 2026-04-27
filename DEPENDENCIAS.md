# Documento de Dependências

## Sistema de Orçamentação para Gráfica

Este documento descreve a stack tecnológica definida para o projeto e o papel de cada tecnologia na arquitetura do sistema.

## 1. Stack do Projeto

| Camada | Tecnologia | Versão | Finalidade |
| --- | --- | --- | --- |
| Linguagem | Java | 21 | Base de implementação da aplicação desktop. |
| Interface Gráfica | JavaFX + FXML | 21 | Construção das telas, navegação e componentes visuais. |
| Banco de Dados | MySQL | 9.7.0 | Persistência de clientes, materiais, orçamentos e histórico. |
| Geração de PDF | iText ou OpenPDF | Definida no projeto | Criação dos orçamentos em PDF. |
| Arquitetura | MVC + Service | Definida no projeto | Organização do código em camadas separadas. |

## 2. Justificativa das Dependências

### Java 24

O Java 21 será utilizado como linguagem principal por oferecer suporte moderno, boa manutenção e compatibilidade com aplicações desktop estruturadas.

### JavaFX 24 + FXML

O JavaFX 21 será a base da interface gráfica. O uso de FXML permite separar a definição visual da lógica da aplicação, favorecendo manutenção e organização.

### MySQL 9.7.0

O MySQL será o SGBD responsável pelo armazenamento dos dados do sistema, incluindo cadastros, históricos, parâmetros de cálculo e registros de orçamento.

### iText / OpenPDF

Uma das bibliotecas será responsável pela geração dos orçamentos em PDF. A escolha final deve considerar licenciamento, facilidade de integração e compatibilidade com a versão do Java adotada.

### MVC + Service

A arquitetura será organizada em camadas para manter separação de responsabilidades:

- Model: entidades de domínio, como Cliente, Orçamento e Material.
- DAO: acesso ao banco de dados e operações de persistência.
- Service: regras de negócio e cálculos de orçamento.
- Controller: integração entre a interface e a lógica da aplicação.
- View: telas FXML da aplicação.

## 3. Estrutura de Pastas Relacionada

| Pasta | Responsabilidade |
| --- | --- |
| model | Entidades do domínio. |
| dao | Acesso e manipulação dos dados no banco. |
| service | Regras de negócio, cálculos e validações. |
| controller | Lógica de interação das telas. |
| view | Arquivos FXML das interfaces. |

## 4. Dependências Funcionais da Arquitetura

### 4.1 Interface

- JavaFX 24 para componentes visuais.
- FXML para definição declarativa das telas.

### 4.2 Persistência

- Conexão com MySQL 9.7.0.
- Repositórios e consultas concentrados na camada DAO.

### 4.3 Negócio

- Cálculos de orçamento concentrados na camada Service.
- Regras comerciais isoladas da interface.

### 4.4 Saída em PDF

- Geração do documento final de orçamento em PDF.
- Integração com a camada Service para receber dados consolidados.

## 5. Observações de Projeto

- A interface não deve conter regras de negócio.
- O acesso ao banco deve ser centralizado na camada DAO.
- A geração de PDF deve receber dados já validados pela camada Service.
- A escolha entre iText e OpenPDF deve ser registrada no repositório quando a implementação começar.
