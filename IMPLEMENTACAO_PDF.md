# Funcionalidade de Exportação de PDF

## Visão Geral
Implementada a funcionalidade completa de geração de orçamentos em PDF para o Sistema de Orçamentação para Gráfica.

## Arquivos Criados/Modificados

### Novos Arquivos
1. **`PdfService.java`** (`sistema-orcamento/src/main/java/com/grafica/service/`)
   - Serviço responsável pela geração dos PDFs
   - Métodos principais:
     - `gerarOrcamentoPdf()`: Gera o PDF completo do orçamento
     - `adicionarCabecalho()`: Adiciona logo e dados do orçamento
     - `adicionarDadosCliente()`: Informações do cliente
     - `adicionarTabelaItens()`: Tabela com todos os itens
     - `adicionarTotais()`: Totais com margem e desconto
     - `adicionarRodape()`: Rodapé com validade

2. **`ConfiguracaoPdf.java`** (`sistema-orcamento/src/main/java/com/grafica/model/`)
   - Model para configurações do PDF
   - Campos: nomeEmpresa, cnpj, endereco, telefone, email, logoPath, rodape, cores, fonte

3. **`ConfiguracaoPdfDAO.java`** (`sistema-orcamento/src/main/java/com/grafica/dao/`)
   - DAO para persistência das configurações de PDF
   - Métodos: salvar(), obter(), atualizar()

4. **`update_pdf_support.sql`** (`mysql/init/`)
   - Script para atualizar banco de dados existente
   - Adiciona colunas e tabela necessárias

### Arquivos Modificados
1. **`pom.xml`**
   - Adicionada dependência OpenPDF 2.0.2

2. **`module-info.java`** (removido)
   - Removido para permitir uso de bibliotecas não-modularizadas

3. **`schema.sql`**
   - Adicionada tabela `configuracao_pdf`
   - Adicionada coluna `id_produto` em `itens_orcamento`

4. **`ItemOrcamento.java`**
   - Adicionados campos `produto` e `material`
   - Adicionados getters/setters correspondentes

5. **`ItemOrcamentoDAO.java`**
   - Modificado `listarPorOrcamento()` para incluir dados do produto e material
   - Atualizado `mapearItem()` para popular produto e material

6. **`OrcamentoController.java`**
   - Implementado método `gerarPropostaPdf()`
   - Abre automaticamente o PDF após geração

## Funcionalidades do PDF

### Cabeçalho
- Logo da empresa (se configurado)
- Título "ORÇAMENTO"
- Número do orçamento
- Data de emissão
- Data de validade (15 dias)

### Dados do Cliente
- ID do cliente
- Nome/razão social

### Tabela de Itens
- Produto
- Material
- Dimensões (largura x altura em mm)
- Quantidade
- Valor Bruto
- Valor Final

### Totais
- Total Bruto
- Desconto (se aplicado)
- Margem de lucro
- **TOTAL FINAL** (em destaque)

### Rodapé
- Mensagem de validade do orçamento

## Como Usar

1. **Criar/Editar Orçamento**
   - Acesse a tela de Orçamentos
   - Selecione cliente e adicione itens
   - Salve o orçamento

2. **Gerar PDF**
   - Com o orçamento salvo, clique em "Gerar PDF"
   - O PDF será gerado em `~/Orcamentos/Orcamento_<ID>.pdf`
   - O arquivo será aberto automaticamente

3. **Configurar PDF (Opcional)**
   - Acesse Ajustes → Configurações PDF
   - Configure:
     - Nome da empresa
     - CNPJ
     - Endereço
     - Telefone
     - Email
     - Caminho do logo
     - Rodapé personalizado

## Estrutura de Pastas do PDF
```
~/Orcamentos/
└── Orcamento_1.pdf
└── Orcamento_2.pdf
└── ...
```

## Dependências
- **OpenPDF 2.0.2**: Biblioteca para geração de PDFs
  - Fork do iText de código aberto
  - Licença LGPL/MPL
  - Compatível com Java 21

## Próximos Passos (Sugestões)
1. Implementar tela de configurações de PDF na interface
2. Adicionar opção de escolher local de salvamento
3. Implementar envio de PDF por email
4. Adicionar numeração personalizada de orçamentos
5. Implementar modelos/templates de PDF personalizados