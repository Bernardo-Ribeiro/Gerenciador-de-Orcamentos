# ✅ Configuração de PDF - Funcionalidade Completa

## Visão Geral
A funcionalidade de configuração de PDF está 100% implementada e funcional, permitindo personalização completa dos orçamentos gerados.

## 📋 Funcionalidades Implementadas

### 1. **Tela de Configurações de PDF** (`Ajustes → Configurações PDF`)

#### Dados da Empresa
- ✅ **Nome Comercial** - Nome da gráfica/empresa
- ✅ **CNPJ** - Documento da empresa
- ✅ **Telefone/WhatsApp** - Contato
- ✅ **E-mail** - E-mail de contato
- ✅ **Endereço Completo** - Endereço comercial

#### Identidade Visual
- ✅ **Logomarca** - Seleção de imagem (PNG, JPG, JPEG, GIF)
  - Botão "Carregar Nova Logo" abre seletor de arquivos
  - Imagem é copiada para `~/Orcamentos/logos/`
  - Suporte a imagens de até 2MB (recomendado)
  - Logomarca aparece no canto superior esquerdo do PDF

#### Layout do Documento
- ✅ **Tamanho do Papel** - Configuração do formato (A4 padrão)
- ✅ **Margens** - Superior e Lateral em mm
- ✅ **Observações do Rodapé** - Texto personalizado

#### Ações
- ✅ **Salvar Configurações** - Persiste no banco de dados
- ✅ **Restaurar Padrões** - Volta para configurações padrão

### 2. **Geração de PDF Personalizado**

#### Cabeçalho do PDF
O PDF gerado exibe:
```
[LOGO]                          NOME DA EMPRESA
                                CNPJ: XX.XXX.XXX/XXXX-XX
                                Endereço completo
                                Tel: (XX) XXXXX-XXXX | Email: contato@empresa.com
                                
                                ORÇAMENTO Nº 123
                                Data: 06/07/2026
                                Válido até: 21/07/2026
```

#### Corpo do PDF
- ✅ Tabela com todos os itens do orçamento
  - Produto
  - Material
  - Dimensões (largura x altura em mm)
  - Quantidade
  - Valor Bruto
  - Valor Final

#### Totais
- ✅ Total Bruto
- ✅ Desconto (se aplicado)
- ✅ Margem de lucro (%)
- ✅ **TOTAL FINAL** (em destaque)

#### Rodapé Personalizado
- ✅ Texto configurado na tela de configurações
- ✅ Data de validade automática
- ✅ Exemplo: "Este orçamento tem validade até 21/07/2026. Garantia de 12 meses."

### 3. **Fluxo de Geração de PDF**

```
1. Usuário clica em "Gerar PDF"
   ↓
2. Abre janela de seleção de diretório
   ↓
3. Usuário seleciona a pasta desejada
   ↓
4. Sistema busca configurações salvas:
   - Dados da empresa
   - Logomarca
   - Rodapé personalizado
   ↓
5. PDF é gerado com personalização completa
   ↓
6. Arquivo salvo em: <PastaSelecionada>/Orcamento_<ID>.pdf
   ↓
7. PDF aberto automaticamente no visualizador padrão
```

## 🗂️ Estrutura de Armazenamento

### Banco de Dados
```sql
CREATE TABLE configuracao_pdf (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_empresa VARCHAR(150),
    cnpj VARCHAR(20),
    endereco VARCHAR(255),
    telefone VARCHAR(20),
    email VARCHAR(100),
    logo_path VARCHAR(255),      -- Caminho da imagem
    rodape VARCHAR(255),          -- Texto do rodapé
    cores VARCHAR(20),
    fonte VARCHAR(50)
);
```

### Arquivos
```
~/Orcamentos/
├── Orcamento_1.pdf
├── Orcamento_2.pdf
└── logos/
    ├── logo_1720281234567_minhalogo.png
    └── logo_1720281234568_outralogo.jpg
```

## 🎨 Como Configurar

### Passo 1: Acessar Configurações
1. No menu lateral, clique em **Ajustes**
2. Selecione **Configurações PDF**

### Passo 2: Preencher Dados da Empresa
1. Preencha **Nome Comercial** (obrigatório)
2. Preencha **CNPJ** (obrigatório)
3. Preencha **Telefone** (obrigatório)
4. Preencha **E-mail** (obrigatório)
5. Preencha **Endereço** (opcional)

### Passo 3: Carregar Logomarca
1. Clique em **"Carregar Nova Logo"**
2. Selecione o arquivo de imagem (PNG, JPG, JPEG, GIF)
3. A imagem é copiada automaticamente para `~/Orcamentos/logos/`

### Passo 4: Personalizar Rodapé
1. Edite o campo **Observações do Rodapé**
2. Sugestão: "Este orçamento tem validade de X dias. Garantia de Y meses."

### Passo 5: Salvar
1. Clique em **"Salvar Configurações"**
2. Mensagem de confirmação aparecerá

### Passo 6: Gerar PDF
1. Vá para **Orçamentos**
2. Crie ou abra um orçamento
3. Clique em **"Gerar PDF"**
4. Selecione a pasta para salvar
5. PDF será gerado com todas as personalizações!

## 🔧 Arquivos Envolvidos

### Controller
- `ConfiguracoesPdfController.java` - Lógica da tela de configurações
- `OrcamentoController.java` - Geração do PDF
- `PdfService.java` - Criação do documento PDF

### Model
- `ConfiguracaoPdf.java` - Entidade de configuração

### DAO
- `ConfiguracaoPdfDAO.java` - Persistência das configurações

### View
- `configuracoes-pdf.fxml` - Interface de configurações

## ✅ Validações

### Campos Obrigatórios
- ✅ Nome Comercial
- ✅ CNPJ
- ✅ Telefone
- ✅ E-mail

### Validações de Logo
- ✅ Apenas imagens (PNG, JPG, JPEG, GIF)
- ✅ Cópia automática para diretório seguro
- ✅ Nome único com timestamp

### Validações de PDF
- ✅ Orçamento deve ter itens
- ✅ Orçamento deve estar salvo
- ✅ Data de emissão automática

## 🎯 Funcionalidades Extras

### Restaurar Padrões
- ✅ Volta todas as configurações para valores padrão
- ✅ Limpa a logomarca selecionada

### Persistência
- ✅ Configurações salvas no banco de dados
- ✅ Recuperação automática ao abrir a tela
- ✅ Uma única configuração por sistema

### Flexibilidade
- ✅ Usuário escolhe onde salvar cada PDF
- ✅ Configurações únicas para todos orçamentos
- ✅ Rodapé personalizável por empresa

## 📊 Status

| Funcionalidade | Status |
|----------------|--------|
| Configurar dados da empresa | ✅ Completo |
| Carregar logomarca | ✅ Completo |
| Salvar configurações | ✅ Completo |
| Carregar configurações salvas | ✅ Completo |
| Gerar PDF com logo | ✅ Completo |
| Gerar PDF com dados da empresa | ✅ Completo |
| Gerar PDF com rodapé personalizado | ✅ Completo |
| Selecionar diretório de salvamento | ✅ Completo |
| Abrir PDF automaticamente | ✅ Completo |

## ✨ Resultado Final

O PDF gerado terá:
- **Cabeçalho profissional** com logo e dados completos da empresa
- **Tabela organizada** com todos os itens do orçamento
- **Totais destacados** com margem e descontos
- **Rodapé personalizado** com mensagem da empresa
- **Validade clara** do orçamento

**Configuração de PDF 100% funcional e pronta para uso! 🎉**