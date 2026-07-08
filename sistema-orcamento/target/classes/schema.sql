-- SQLite Schema for Budget Management System
-- Auto-generated from MySQL schema

-- Enable foreign key support
PRAGMA foreign_keys = ON;

-- =====================================================
-- BASE TABLES
-- =====================================================

CREATE TABLE IF NOT EXISTS categorias_lucro (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    descricao TEXT,
    margem_padrao REAL NOT NULL DEFAULT 0.00
);

CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    senha_hash TEXT NOT NULL,
    status TEXT DEFAULT 'ATIVO' CHECK(status IN ('ATIVO', 'INATIVO'))
);

CREATE TABLE IF NOT EXISTS clientes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome_razao_social TEXT NOT NULL,
    cpf_cnpj TEXT NOT NULL UNIQUE,
    email_contato TEXT,
    telefone_whatsapp TEXT,
    status TEXT DEFAULT 'ATIVO' CHECK(status IN ('ATIVO', 'INATIVO')),
    data_cadastro TEXT DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- MATERIAIS
-- =====================================================

CREATE TABLE IF NOT EXISTS materiais (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    categoria TEXT NOT NULL CHECK(categoria IN ('IMPRESSOS', 'COMUNICACAO_VISUAL', 'RIGIDOS')),
    id_categoria_lucro INTEGER,
    tipo_item TEXT NOT NULL CHECK(tipo_item IN ('BASE', 'ACABAMENTO')),
    tipo_cobranca TEXT NOT NULL CHECK(tipo_cobranca IN ('AREA', 'UNIDADE', 'TIRAGEM', 'FOLHA', 'PACOTE', 'SERVICO', 'CHAPA')),
    custo_base REAL NOT NULL,
    custo_producao REAL DEFAULT 0.00,
    status TEXT DEFAULT 'ATIVO' CHECK(status IN ('ATIVO', 'INATIVO')),
    FOREIGN KEY (id_categoria_lucro) REFERENCES categorias_lucro(id) ON DELETE SET NULL
);

-- =====================================================
-- PRODUTOS
-- =====================================================

CREATE TABLE IF NOT EXISTS produtos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    categoria TEXT NOT NULL CHECK(categoria IN ('IMPRESSOS', 'COMUNICACAO_VISUAL', 'RIGIDOS'))
);

-- =====================================================
-- LAYOUTS
-- =====================================================

CREATE TABLE IF NOT EXISTS layouts_produto (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_produto INTEGER NOT NULL,
    nome_layout TEXT NOT NULL,
    largura_mm INTEGER NOT NULL,
    altura_mm INTEGER NOT NULL,
    FOREIGN KEY (id_produto) REFERENCES produtos(id) ON DELETE CASCADE,
    UNIQUE (id_produto, nome_layout)
);

-- =====================================================
-- RELACIONAMENTOS PRODUTO x MATERIAL
-- =====================================================

CREATE TABLE IF NOT EXISTS produto_materiais (
    id_produto INTEGER NOT NULL,
    id_material INTEGER NOT NULL,
    material_padrao INTEGER DEFAULT 0,
    PRIMARY KEY (id_produto, id_material),
    FOREIGN KEY (id_produto) REFERENCES produtos(id) ON DELETE CASCADE,
    FOREIGN KEY (id_material) REFERENCES materiais(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS produto_acabamentos (
    id_produto INTEGER NOT NULL,
    id_material INTEGER NOT NULL,
    obrigatorio INTEGER DEFAULT 0,
    PRIMARY KEY (id_produto, id_material),
    FOREIGN KEY (id_produto) REFERENCES produtos(id) ON DELETE CASCADE,
    FOREIGN KEY (id_material) REFERENCES materiais(id) ON DELETE CASCADE
);

-- =====================================================
-- ESCALAS PRODUTIVAS
-- =====================================================

CREATE TABLE IF NOT EXISTS escalas_produtivas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_material INTEGER NOT NULL,
    qtd_minima INTEGER NOT NULL,
    qtd_maxima INTEGER NOT NULL,
    desconto_percentual REAL NOT NULL,
    FOREIGN KEY (id_material) REFERENCES materiais(id) ON DELETE CASCADE
);

-- =====================================================
-- ORÇAMENTOS
-- =====================================================

CREATE TABLE IF NOT EXISTS orcamentos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_cliente INTEGER NOT NULL,
    id_usuario INTEGER NOT NULL,
    data_emissao TEXT DEFAULT CURRENT_TIMESTAMP,
    data_validade TEXT NOT NULL,
    status TEXT DEFAULT 'PENDENTE' CHECK(status IN ('PENDENTE', 'APROVADO', 'REPROVADO', 'EXCLUIDO')),
    valor_bruto REAL NOT NULL,
    margem_lucro_percentual REAL NOT NULL,
    desconto_progressivo REAL DEFAULT 0.00,
    valor_final REAL NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

-- =====================================================
-- ITENS DO ORÇAMENTO
-- =====================================================

CREATE TABLE IF NOT EXISTS itens_orcamento (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_orcamento INTEGER NOT NULL,
    id_produto INTEGER NOT NULL,
    id_material INTEGER NOT NULL,
    id_layout INTEGER,
    largura_mm INTEGER DEFAULT 0,
    altura_mm INTEGER DEFAULT 0,
    quantidade INTEGER DEFAULT 1,
    area_calculada REAL,
    valor_bruto_item REAL NOT NULL,
    valor_final_item REAL NOT NULL,
    custo_unitario REAL NOT NULL,
    tipo_cobranca_aplicado TEXT NOT NULL CHECK(tipo_cobranca_aplicado IN ('AREA', 'UNIDADE', 'TIRAGEM', 'FOLHA', 'PACOTE', 'SERVICO', 'CHAPA')),
    FOREIGN KEY (id_orcamento) REFERENCES orcamentos(id) ON DELETE CASCADE,
    FOREIGN KEY (id_produto) REFERENCES produtos(id),
    FOREIGN KEY (id_material) REFERENCES materiais(id),
    FOREIGN KEY (id_layout) REFERENCES layouts_produto(id)
);

CREATE TABLE IF NOT EXISTS item_orcamento_acabamentos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_item_orcamento INTEGER NOT NULL,
    id_material_acabamento INTEGER NOT NULL,
    quantidade INTEGER DEFAULT 1,
    valor_unitario REAL NOT NULL,
    valor_total REAL NOT NULL,
    FOREIGN KEY (id_item_orcamento) REFERENCES itens_orcamento(id) ON DELETE CASCADE,
    FOREIGN KEY (id_material_acabamento) REFERENCES materiais(id)
);

-- =====================================================
-- AUDITORIA
-- =====================================================

CREATE TABLE IF NOT EXISTS auditoria_log (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tabela TEXT NOT NULL,
    id_registro INTEGER NOT NULL,
    campo TEXT,
    valor_antigo TEXT,
    valor_novo TEXT,
    id_usuario INTEGER,
    data_evento TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE SET NULL
);

-- =====================================================
-- CONFIGURAÇÃO PDF
-- =====================================================

CREATE TABLE IF NOT EXISTS configuracao_pdf (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome_empresa TEXT,
    cnpj TEXT,
    endereco TEXT,
    telefone TEXT,
    email TEXT,
    logo_path TEXT,
    rodape TEXT,
    cores TEXT,
    fonte TEXT
);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX IF NOT EXISTS idx_layouts_produto_produto ON layouts_produto(id_produto);
CREATE INDEX IF NOT EXISTS idx_produto_materiais_produto ON produto_materiais(id_produto);
CREATE INDEX IF NOT EXISTS idx_produto_materiais_material ON produto_materiais(id_material);
CREATE INDEX IF NOT EXISTS idx_produto_acabamentos_produto ON produto_acabamentos(id_produto);
CREATE INDEX IF NOT EXISTS idx_produto_acabamentos_material ON produto_acabamentos(id_material);
CREATE INDEX IF NOT EXISTS idx_item_orcamento_acabamentos_item ON item_orcamento_acabamentos(id_item_orcamento);
CREATE INDEX IF NOT EXISTS idx_itens_orcamento_orc ON itens_orcamento(id_orcamento);
CREATE INDEX IF NOT EXISTS idx_itens_orcamento_mat ON itens_orcamento(id_material);
CREATE INDEX IF NOT EXISTS idx_escalas_material ON escalas_produtivas(id_material);
CREATE INDEX IF NOT EXISTS idx_orcamentos_cliente ON orcamentos(id_cliente);