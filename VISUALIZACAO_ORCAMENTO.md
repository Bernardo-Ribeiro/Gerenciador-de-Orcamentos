# ✅ Visualização de Orçamentos no Menu Relatórios

## Funcionalidade Implementada

Agora o menu **Relatórios** possui uma tela completa de visualização de orçamentos salvos, permitindo consultar todos os detalhes sem precisar gerar PDF.

## 🎯 Fluxo de Uso

### 1. Acessar Relatórios
- Menu lateral → **Relatórios**
- Tabela com todos os orçamentos salvos é exibida

### 2. Visualizar Orçamento
- Clique no botão **"Ver"** na linha do orçamento desejado
- Nova tela é aberta mostrando:
  - ✅ Número do orçamento
  - ✅ Nome do cliente
  - ✅ Data de emissão e validade
  - ✅ Status (Pendente/Aprovado/Reprovado)
  - ✅ Todos os itens com produto, material, dimensões, quantidade e valores
  - ✅ Resumo financeiro (total bruto, desconto, margem, total final)

### 3. Ações na Tela de Visualização

#### Gerar PDF
- Botão **"Gerar PDF"** no topo da tela
- Abre `DirectoryChooser` para selecionar onde salvar
- Gera PDF com todas as configurações da empresa (logo, cabeçalho, rodapé)
- Abre automaticamente o PDF após gerar

#### Criar Cópia (Duplicar)
- Botão **"Criar Cópia"** no topo da tela
- Cria um **novo orçamento** baseado no visualizado
- Copia todos os dados:
  - Mesmo cliente
  - Mesmos itens (produto, material, dimensões, quantidades)
  - Mesmos valores e margens
- Novo orçamento recebe:
  - Status: **PENDENTE**
  - Data de emissão: **Hoje**
  - Data de validade: **Hoje + 15 dias**
  - Novo ID único
- Após duplicar, você pode:
  - Ir em **Orçamentos** → Editar o novo rascunho
  - Modificar itens, valores, cliente, etc.
  - Salvar como um novo orçamento

#### Fechar
- Botão **"Fechar"** fecha a tela de visualização

## 📁 Arquivos Criados/Modificados

### Novos Arquivos
1. **`visualizar-orcamento.fxml`** - Interface da tela de visualização
2. **`VisualizarOrcamentoController.java`** - Lógica da tela de visualização

### Arquivos Modificados
1. **`RelatoriosController.java`**
   - Método `abrirVisualizacaoOrcamento()` para abrir a tela
   - Botão "Ver" agora abre a tela de visualização
   - Importações adicionadas (FXMLLoader, Stage, Scene, etc.)

2. **`OrcamentoDAO.java`**
   - Método `buscarPorId(Integer id)` para buscar orçamento completo

3. **`PdfService.java`**
   - Método `gerarOrcamentoPdfPorId(Integer id, String diretorio)` para gerar PDF a partir do ID

## 🔧 Como Funciona

### Botão "Ver"
```java
ver.setOnAction(event -> {
    RelatorioOrcamentoView view = getTableView().getItems().get(getIndex());
    if (view != null) {
        abrirVisualizacaoOrcamento(view.getOrcamento().getId());
    }
});
```

### Método `abrirVisualizacaoOrcamento()`
```java
private void abrirVisualizacaoOrcamento(Integer idOrcamento) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grafica/view/visualizar-orcamento.fxml"));
    Parent root = loader.load();
    
    VisualizarOrcamentoController controller = loader.getController();
    controller.carregarOrcamento(idOrcamento);
    
    Stage stage = new Stage();
    stage.setTitle("Visualizar Orçamento #" + String.format("%04d", idOrcamento));
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setScene(new Scene(root, 1000, 700));
    stage.showAndWait();
}
```

### Método `carregarOrcamento()` no `VisualizarOrcamentoController`
```java
public void carregarOrcamento(Integer idOrcamento) {
    orcamento = orcamentoDAO.buscarPorId(idOrcamento);
    itens = itemOrcamentoDAO.listarPorOrcamento(idOrcamento);
    preencherDados();
}
```

### Método `duplicarOrcamento()`
```java
@FXML
private void duplicarOrcamento() {
    // Cria novo orçamento com mesmos dados
    Orcamento novoOrcamento = new Orcamento();
    novoOrcamento.setIdCliente(orcamento.getIdCliente());
    novoOrcamento.setDataEmissao(LocalDate.now());
    novoOrcamento.setDataValidade(LocalDate.now().plusDays(15));
    novoOrcamento.setStatus("PENDENTE");
    // ... copia valores ...
    
    orcamentoDAO.criar(novoOrcamento);
    
    // Copia todos os itens
    for (ItemOrcamento item : itens) {
        ItemOrcamento novoItem = new ItemOrcamento();
        novoItem.setIdOrcamento(novoOrcamento.getId());
        // ... copia dados do item ...
        itemOrcamentoDAO.criar(novoItem);
    }
}
```

## 🎨 Interface

### Tela de Visualização
```
┌─────────────────────────────────────────────────────────────┐
│  Relatórios > Visualizar Orçamento                          │
│                                                              │
│  Visualização do Orçamento                                  │
│  Orçamento #0123                             [Gerar PDF]    │
│                                              [Criar Cópia]  │
│                                              [Fechar]       │
├─────────────────────────────────────────────────────────────┤
│  Dados do Orçamento                                         │
│  Cliente: João da Silva                                     │
│  Data de Emissão: 06/07/2026                                │
│  Validade: 21/07/2026                                       │
│  Status: [PENDENTE]                                         │
├─────────────────────────────────────────────────────────────┤
│  Itens do Orçamento                                         │
│  ┌──────────┬──────────┬────────────┬─────┬────────┬───────┐│
│  │Produto   │Material  │Dimensões   │Qtd  │Vl.Bruno│Vl.Fin.││
│  ├──────────┼──────────┼────────────┼─────┼────────┼───────┤│
│  │Cartão    │Papel     │100 x 50 mm │1000 │R$ 500  │R$ 600 ││
│  │Visita    │Vergê     │            │     │        │       ││
│  └──────────┴──────────┴────────────┴─────┴────────┴───────┘│
├─────────────────────────────────────────────────────────────┤
│  Resumo Financeiro                                          │
│  Total Bruto: R$ 500,00                                     │
│  Desconto: 0%                                               │
│  Margem de Lucro: 20%                                       │
│  ─────────────────────────────────────                      │
│  TOTAL FINAL: R$ 600,00                                     │
└─────────────────────────────────────────────────────────────┘
```

## ✅ Vantagens

1. **Visualização rápida** - Sem precisar gerar PDF
2. **Somente leitura** - Usuário não edita orçamento aprovado
3. **Duplicação fácil** - Cria novo orçamento baseado em existente
4. **PDF sob demanda** - Gera quando precisar
5. **Todos os dados** - Cliente, itens, valores, status

## 🧪 Testes

### Testar Visualização
1. Abra **Relatórios**
2. Clique em **"Ver"** em qualquer orçamento
3. Confira se todos os dados aparecem corretamente

### Testar Geração de PDF
1. Na tela de visualização, clique em **"Gerar PDF"**
2. Selecione uma pasta
3. PDF deve ser gerado e aberto automaticamente

### Testar Duplicação
1. Na tela de visualização, clique em **"Criar Cópia"**
2. Mensagem de sucesso deve aparecer com novo ID
3. Vá em **Orçamentos**
4. Encontre o novo orçamento (último da lista)
5. Edite e salve conforme necessário

## 📝 Próximos Passos (Sugestões)

- [ ] Adicionar botão "Editar" na visualização (se status for Pendente)
- [ ] Adicionar filtro de período na tela de visualização
- [ ] Exportar PDF diretamente sem abrir DirectoryChooser (usar pasta padrão)
- [ ] Adicionar impressão direta (sem salvar arquivo)

**Funcionalidade 100% implementada e funcional! 🎉**