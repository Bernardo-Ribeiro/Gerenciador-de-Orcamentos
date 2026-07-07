# Graph Report - .  (2026-07-07)

## Corpus Check
- 76 files · ~169,024 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 906 nodes · 2257 edges · 30 communities (24 shown, 6 thin omitted)
- Extraction: 80% EXTRACTED · 20% INFERRED · 0% AMBIGUOUS · INFERRED: 455 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Material Costs Controller|Material Costs Controller]]
- [[_COMMUNITY_Client Management Controller|Client Management Controller]]
- [[_COMMUNITY_Login & Authentication|Login & Authentication]]
- [[_COMMUNITY_Dashboard UI|Dashboard UI]]
- [[_COMMUNITY_Material Costs Controller|Material Costs Controller]]
- [[_COMMUNITY_Client Management Controller|Client Management Controller]]
- [[_COMMUNITY_Client Management Controller|Client Management Controller]]
- [[_COMMUNITY_Budget Controller|Budget Controller]]
- [[_COMMUNITY_Module 8|Module 8]]
- [[_COMMUNITY_Budget Controller|Budget Controller]]
- [[_COMMUNITY_Budget Controller|Budget Controller]]
- [[_COMMUNITY_Module 11|Module 11]]
- [[_COMMUNITY_Client Management Controller|Client Management Controller]]
- [[_COMMUNITY_Dashboard UI|Dashboard UI]]
- [[_COMMUNITY_Material Costs Controller|Material Costs Controller]]
- [[_COMMUNITY_Client Management Controller|Client Management Controller]]
- [[_COMMUNITY_Material Costs Controller|Material Costs Controller]]
- [[_COMMUNITY_Material Costs Controller|Material Costs Controller]]
- [[_COMMUNITY_Module 18|Module 18]]
- [[_COMMUNITY_Dashboard UI|Dashboard UI]]
- [[_COMMUNITY_Client Management Controller|Client Management Controller]]
- [[_COMMUNITY_Budget Controller|Budget Controller]]
- [[_COMMUNITY_Material Costs Controller|Material Costs Controller]]
- [[_COMMUNITY_Dashboard UI|Dashboard UI]]
- [[_COMMUNITY_Module 24|Module 24]]
- [[_COMMUNITY_Module 25|Module 25]]
- [[_COMMUNITY_Module 26|Module 26]]
- [[_COMMUNITY_Module 29|Module 29]]

## God Nodes (most connected - your core abstractions)
1. `OrcamentoController` - 60 edges
2. `Orcamento` - 51 edges
3. `ItemOrcamento` - 46 edges
4. `Material` - 43 edges
5. `RelatoriosController` - 38 edges
6. `DashboardController` - 35 edges
7. `ProdutoController` - 35 edges
8. `Usuario` - 35 edges
9. `ConfiguracaoPdf` - 33 edges
10. `MainController` - 30 edges

## Surprising Connections (you probably didn't know these)
- `CustosInsumosController` --references--> `CategoriaLucroDAO`  [EXTRACTED]
  sistema-orcamento/src/main/java/com/grafica/controller/CustosInsumosController.java → sistema-orcamento/src/main/java/com/grafica/dao/CategoriaLucroDAO.java
- `CustosInsumosController` --references--> `CategoriaLucro`  [EXTRACTED]
  sistema-orcamento/src/main/java/com/grafica/controller/CustosInsumosController.java → sistema-orcamento/src/main/java/com/grafica/model/CategoriaLucro.java
- `DashboardController` --references--> `ClienteDAO`  [EXTRACTED]
  sistema-orcamento/src/main/java/com/grafica/controller/DashboardController.java → sistema-orcamento/src/main/java/com/grafica/dao/ClienteDAO.java
- `DashboardController` --references--> `OrcamentoDAO`  [EXTRACTED]
  sistema-orcamento/src/main/java/com/grafica/controller/DashboardController.java → sistema-orcamento/src/main/java/com/grafica/dao/OrcamentoDAO.java
- `DashboardController` --references--> `Usuario`  [EXTRACTED]
  sistema-orcamento/src/main/java/com/grafica/controller/DashboardController.java → sistema-orcamento/src/main/java/com/grafica/model/Usuario.java

## Import Cycles
- None detected.

## Communities (30 total, 6 thin omitted)

### Community 0 - "Material Costs Controller"
Cohesion: 0.06
Nodes (20): CustosInsumosController, CheckBox, ComboBox, FXML, HBox, ObservableList, TableColumn, TableView (+12 more)

### Community 1 - "Client Management Controller"
Cohesion: 0.09
Nodes (12): Document, Font, PdfPCell, ConfiguracoesPdfController, FXML, TextField, ConfiguracaoPdfDAO, ResultSet (+4 more)

### Community 2 - "Login & Authentication"
Cohesion: 0.06
Nodes (23): Application, ImageView, Node, Parent, PasswordField, Rectangle2D, Scene, FXML (+15 more)

### Community 3 - "Dashboard UI"
Cohesion: 0.07
Nodes (15): BorderPane, DashboardController, Button, FXML, Label, NumberFormat, TableColumn, TableView (+7 more)

### Community 4 - "Material Costs Controller"
Cohesion: 0.06
Nodes (5): ItemOrcamentoView, ItemOrcamentoDAO, ResultSet, ItemOrcamento, Override

### Community 5 - "Client Management Controller"
Cohesion: 0.07
Nodes (15): ClienteController, ActionEvent, Button, ComboBox, FXML, HBox, Label, ObservableList (+7 more)

### Community 6 - "Client Management Controller"
Cohesion: 0.09
Nodes (13): SearchableComboBox, CheckBox, FXML, Label, ObservableList, TableCell, TableColumn, TableView (+5 more)

### Community 7 - "Budget Controller"
Cohesion: 0.08
Nodes (11): GestaoUsuariosController, ComboBox, FXML, ObservableList, TableColumn, TableView, TextField, ResultSet (+3 more)

### Community 8 - "Module 8"
Cohesion: 0.06
Nodes (7): EscalaProdutivaDAO, ResultSet, ItemOrcamentoAcabamentoDAO, ResultSet, EscalaProdutiva, ItemOrcamentoAcabamento, Override

### Community 9 - "Budget Controller"
Cohesion: 0.10
Nodes (12): Initializable, ResourceBundle, FXML, HBox, Label, Override, TextField, VBox (+4 more)

### Community 10 - "Budget Controller"
Cohesion: 0.09
Nodes (10): GestaoLayoutsController, ComboBox, FXML, ObservableList, TableColumn, TableView, TextField, LayoutProdutoDAO (+2 more)

### Community 11 - "Module 11"
Cohesion: 0.10
Nodes (6): FormUtils, ComboBox, TextField, NumberFormat, Window, UiUtils

### Community 12 - "Client Management Controller"
Cohesion: 0.14
Nodes (4): ResultSet, OrcamentoDAO, Override, Orcamento

### Community 13 - "Dashboard UI"
Cohesion: 0.16
Nodes (8): EscalaProdutivaController, Intervalo, IntervaloRow, FXML, HBox, Label, TextField, VBox

### Community 14 - "Material Costs Controller"
Cohesion: 0.12
Nodes (5): RadioButton, ResultSet, ProdutoMaterialDAO, Override, ProdutoMaterial

### Community 15 - "Client Management Controller"
Cohesion: 0.17
Nodes (11): Region, ActionEvent, FXML, Label, NumberFormat, ObservableList, TableColumn, TableView (+3 more)

### Community 16 - "Material Costs Controller"
Cohesion: 0.13
Nodes (5): ObservableList, ResultSet, ProdutoAcabamentoDAO, Override, ProdutoAcabamento

### Community 17 - "Material Costs Controller"
Cohesion: 0.19
Nodes (7): ComboBox, FXML, TableCell, TableColumn, TableView, TextField, ProdutoController

### Community 18 - "Module 18"
Cohesion: 0.15
Nodes (4): ResultSet, ProdutoDAO, Override, Produto

### Community 20 - "Client Management Controller"
Cohesion: 0.20
Nodes (7): Callback, Button, Label, TableCell, TableColumn, TableView, TableUtils

### Community 21 - "Budget Controller"
Cohesion: 0.15
Nodes (6): Button, FXML, Label, TableColumn, TableView, VisualizarOrcamentoController

### Community 22 - "Material Costs Controller"
Cohesion: 0.35
Nodes (6): AjustesController, ActionEvent, Button, FXML, Label, StackPane

## Knowledge Gaps
- **3 isolated node(s):** `recordToolUse.sh script`, `recordToolUse.sh script`, `com.grafica:sistema-orcamento`
  These have ≤1 connection - possible missing edges or undocumented components.
- **6 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `OrcamentoController` connect `Client Management Controller` to `Material Costs Controller`, `Dashboard UI`, `Material Costs Controller`, `Client Management Controller`, `Budget Controller`, `Budget Controller`, `Client Management Controller`, `Material Costs Controller`, `Material Costs Controller`, `Module 18`?**
  _High betweenness centrality (0.161) - this node is a cross-community bridge._
- **Why does `Orcamento` connect `Client Management Controller` to `Client Management Controller`, `Login & Authentication`, `Dashboard UI`, `Client Management Controller`, `Client Management Controller`, `Dashboard UI`, `Budget Controller`, `Dashboard UI`?**
  _High betweenness centrality (0.084) - this node is a cross-community bridge._
- **Why does `Material` connect `Material Costs Controller` to `Material Costs Controller`, `Material Costs Controller`, `Material Costs Controller`, `Client Management Controller`?**
  _High betweenness centrality (0.075) - this node is a cross-community bridge._
- **What connects `recordToolUse.sh script`, `recordToolUse.sh script`, `com.grafica:sistema-orcamento` to the rest of the system?**
  _3 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Material Costs Controller` be split into smaller, more focused modules?**
  _Cohesion score 0.05809979494190021 - nodes in this community are weakly interconnected._
- **Should `Client Management Controller` be split into smaller, more focused modules?**
  _Cohesion score 0.08602150537634409 - nodes in this community are weakly interconnected._
- **Should `Login & Authentication` be split into smaller, more focused modules?**
  _Cohesion score 0.05901639344262295 - nodes in this community are weakly interconnected._