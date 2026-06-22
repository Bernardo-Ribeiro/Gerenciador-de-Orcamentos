-- Active: 1782150041296@@127.0.0.1@3306@grafica_db
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS orcamento_db;
CREATE DATABASE orcamento_db;
USE orcamento_db;

-- =====================================================
-- TABELAS BASE
-- =====================================================

CREATE TABLE categorias_lucro (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    margem_padrao DECIMAL(5,2) NOT NULL DEFAULT 0.00
);

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    status ENUM('ATIVO', 'INATIVO') DEFAULT 'ATIVO'
);

CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_razao_social VARCHAR(150) NOT NULL,
    cpf_cnpj VARCHAR(20) NOT NULL UNIQUE,
    email_contato VARCHAR(100),
    telefone_whatsapp VARCHAR(20),
    status ENUM('ATIVO', 'INATIVO') DEFAULT 'ATIVO',
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- MATERIAIS
-- =====================================================

CREATE TABLE materiais (
    id INT AUTO_INCREMENT PRIMARY KEY,

    nome VARCHAR(100) NOT NULL,

    categoria ENUM(
        'IMPRESSOS',
        'COMUNICACAO_VISUAL',
        'RIGIDOS'
    ) NOT NULL,

    id_categoria_lucro INT,

    tipo_item ENUM(
        'BASE',
        'ACABAMENTO'
    ) NOT NULL,

    tipo_cobranca ENUM(
        'AREA',
        'UNIDADE',
        'TIRAGEM',
        'FOLHA',
        'PACOTE',
        'SERVICO',
        'CHAPA'
    ) NOT NULL,

    custo_base DECIMAL(10,2) NOT NULL,
    custo_producao DECIMAL(10,2) DEFAULT 0.00,

    status ENUM(
        'ATIVO',
        'INATIVO'
    ) DEFAULT 'ATIVO',

    FOREIGN KEY (id_categoria_lucro)
        REFERENCES categorias_lucro(id)
        ON DELETE SET NULL
);

-- =====================================================
-- PRODUTOS
-- =====================================================

CREATE TABLE produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,

    nome VARCHAR(100) NOT NULL,

    categoria ENUM(
        'IMPRESSOS',
        'COMUNICACAO_VISUAL',
        'RIGIDOS'
    ) NOT NULL
);

-- =====================================================
-- LAYOUTS
-- =====================================================

CREATE TABLE layouts_produto (
    id INT AUTO_INCREMENT PRIMARY KEY,

    id_produto INT NOT NULL,

    nome_layout VARCHAR(100) NOT NULL,

    largura_mm INT NOT NULL,
    altura_mm INT NOT NULL,

    FOREIGN KEY (id_produto)
        REFERENCES produtos(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_layout_produto UNIQUE (id_produto, nome_layout)
);

-- =====================================================
-- RELACIONAMENTOS PRODUTO x MATERIAL
-- =====================================================

CREATE TABLE produto_materiais (
    id_produto INT NOT NULL,
    id_material INT NOT NULL,

    material_padrao BOOLEAN DEFAULT FALSE,

    PRIMARY KEY (id_produto, id_material),

    FOREIGN KEY (id_produto)
        REFERENCES produtos(id)
        ON DELETE CASCADE,

    FOREIGN KEY (id_material)
        REFERENCES materiais(id)
        ON DELETE CASCADE
);

CREATE TABLE produto_acabamentos (
    id_produto INT NOT NULL,
    id_material INT NOT NULL,

    obrigatorio BOOLEAN DEFAULT FALSE,

    PRIMARY KEY (id_produto, id_material),

    FOREIGN KEY (id_produto)
        REFERENCES produtos(id)
        ON DELETE CASCADE,

    FOREIGN KEY (id_material)
        REFERENCES materiais(id)
        ON DELETE CASCADE
);

-- =====================================================
-- ESCALAS PRODUTIVAS
-- =====================================================

CREATE TABLE escalas_produtivas (
    id INT AUTO_INCREMENT PRIMARY KEY,

    id_material INT NOT NULL,

    qtd_minima INT NOT NULL,
    qtd_maxima INT NOT NULL,

    desconto_percentual DECIMAL(5,2) NOT NULL,

    FOREIGN KEY (id_material)
        REFERENCES materiais(id)
        ON DELETE CASCADE
);

-- =====================================================
-- ORÇAMENTOS
-- =====================================================

CREATE TABLE orcamentos (
    id INT AUTO_INCREMENT PRIMARY KEY,

    id_cliente INT NOT NULL,
    id_usuario INT NOT NULL,

    data_emissao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_validade DATE NOT NULL,

    status ENUM(
        'PENDENTE',
        'APROVADO',
        'REPROVADO'
    ) DEFAULT 'PENDENTE',

    valor_bruto DECIMAL(10,2) NOT NULL,
    margem_lucro_percentual DECIMAL(5,2) NOT NULL,
    desconto_progressivo DECIMAL(5,2) DEFAULT 0.00,
    valor_final DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (id_cliente)
        REFERENCES clientes(id),

    FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id)
);

-- =====================================================
-- ITENS DO ORÇAMENTO
-- =====================================================

CREATE TABLE itens_orcamento (
    id INT AUTO_INCREMENT PRIMARY KEY,

    id_orcamento INT NOT NULL,

    id_produto INT NOT NULL,
    id_material INT NOT NULL,
    id_layout INT,

    largura_mm INT DEFAULT 0,
    altura_mm INT DEFAULT 0,

    quantidade INT DEFAULT 1,

    area_calculada DECIMAL(10,4),

    valor_bruto_item DECIMAL(10,2) NOT NULL,
    valor_final_item DECIMAL(10,2) NOT NULL,

    custo_unitario DECIMAL(10,2) NOT NULL,

    tipo_cobranca_aplicado ENUM(
        'AREA',
        'UNIDADE',
        'TIRAGEM',
        'FOLHA',
        'PACOTE',
        'SERVICO',
        'CHAPA'
    ) NOT NULL,

    FOREIGN KEY (id_orcamento)
        REFERENCES orcamentos(id)
        ON DELETE CASCADE,

    FOREIGN KEY (id_produto)
        REFERENCES produtos(id),

    FOREIGN KEY (id_material)
        REFERENCES materiais(id),

    FOREIGN KEY (id_layout)
        REFERENCES layouts_produto(id)
);
CREATE TABLE item_orcamento_acabamentos (
    id INT AUTO_INCREMENT PRIMARY KEY,

    id_item_orcamento INT NOT NULL,
    id_material_acabamento INT NOT NULL,

    quantidade INT DEFAULT 1,

    valor_unitario DECIMAL(10,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (id_item_orcamento)
        REFERENCES itens_orcamento(id)
        ON DELETE CASCADE,

    FOREIGN KEY (id_material_acabamento)
        REFERENCES materiais(id)
);
-- =====================================================
-- AUDITORIA
-- =====================================================

CREATE TABLE auditoria_log (
    id INT AUTO_INCREMENT PRIMARY KEY,

    tabela VARCHAR(50) NOT NULL,
    id_registro INT NOT NULL,

    campo VARCHAR(50),
    valor_antigo TEXT,
    valor_novo TEXT,

    id_usuario INT,

    FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id)
        ON DELETE SET NULL,

    data_evento TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_layouts_produto_produto
ON layouts_produto(id_produto);

CREATE INDEX idx_produto_materiais_produto
ON produto_materiais(id_produto);

CREATE INDEX idx_produto_materiais_material
ON produto_materiais(id_material);

CREATE INDEX idx_produto_acabamentos_produto
ON produto_acabamentos(id_produto);

CREATE INDEX idx_produto_acabamentos_material
ON produto_acabamentos(id_material);

CREATE INDEX idx_item_orcamento_acabamentos_item
ON item_orcamento_acabamentos(id_item_orcamento);

CREATE INDEX idx_itens_orcamento_orc
ON itens_orcamento(id_orcamento);

CREATE INDEX idx_itens_orcamento_mat
ON itens_orcamento(id_material);

CREATE INDEX idx_escalas_material
ON escalas_produtivas(id_material);

CREATE INDEX idx_orcamentos_cliente
ON orcamentos(id_cliente);


SET FOREIGN_KEY_CHECKS = 1;
-- =============================================================================
-- 2. INSERÇÃO DE DADOS (Seeds) - CORRIGIDO E ORDENADO
-- =============================================================================

-- 1. Categorias de Lucro (Tabela Pai)
INSERT INTO categorias_lucro (nome, descricao, margem_padrao) VALUES 
('Impressão Digital Solvent', 'Banners, Lonas e Adesivos em grandes formatos', 45.00),
('Acabamento e Refile', 'Serviços de corte, solda eletrônica e ilhós', 60.00),
('Letra Caixa Inox', 'Letreiros metálicos com ou sem iluminação LED', 120.00),
('Serralheria Estrutural', 'Painéis, totens e cavaletes de metal', 85.00),
('Instalação em Altura', 'Serviços externos com necessidade de balancim', 150.00),
('Design e Vetorização', 'Criação de artes e ajuste de arquivos técnicos', 200.00),
('Impressos em Geral', 'Cartões, folhetos e papéis em geral', 100.00);

-- 2. Usuários (Tabela Pai)
INSERT INTO usuarios (nome, email, senha_hash, status) VALUES
('Administrador', 'admin@grafica.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'ATIVO'),
('Operador Comercial', 'comercial@grafica.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'ATIVO'),
('Gestor Produccion', 'gestor@grafica.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'ATIVO');

-- 3. Clientes (Tabela Pai)
INSERT INTO clientes (nome_razao_social, cpf_cnpj, email_contato, telefone_whatsapp, status, data_cadastro) VALUES
('Construtora Horizonte Ltda', '12345678000101', 'contato@horizonte.com', '11999990001', 'ATIVO', NOW() - INTERVAL 20 DAY),
('Mercado Central SA', '22345678000102', 'compras@mercadocentral.com', '11999990002', 'ATIVO', NOW() - INTERVAL 10 DAY),
('Clinica Vida Plena', '32345678000103', 'financeiro@vidaplena.com', '11999990003', 'ATIVO', NOW() - INTERVAL 5 DAY);

-- 4. Produtos (Tabela Pai)
INSERT INTO produtos (nome, categoria) VALUES
('Cartão de Visita', 'IMPRESSOS'),          -- ID 1
('Flyer', 'IMPRESSOS'),                     -- ID 2
('Folder', 'IMPRESSOS'),                    -- ID 3
('Banner', 'COMUNICACAO_VISUAL'),           -- ID 4
('Faixa', 'COMUNICACAO_VISUAL'),            -- ID 5
('Adesivo', 'COMUNICACAO_VISUAL'),          -- ID 6
('Placa PVC', 'RIGIDOS');                   -- ID 7

-- 5. Materiais (Base e Acabamentos)
-- =============================================================================
-- PAPÉIS (IMPRESSOS, BASE)
-- =============================================================================
INSERT INTO materiais (nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status) VALUES
('Papel Sulfite 75g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.08, 0.02, 'ATIVO'),             -- ID 1
('Papel Sulfite 90g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.12, 0.03, 'ATIVO'),             -- ID 2
('Papel Sulfite 120g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.18, 0.04, 'ATIVO'),            -- ID 3
('Papel Sulfite 180g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.35, 0.08, 'ATIVO'),            -- ID 4
('Papel Couché 90g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.15, 0.03, 'ATIVO'),              -- ID 5
('Papel Couché 115g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.20, 0.04, 'ATIVO'),             -- ID 6
('Papel Couché 150g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.28, 0.05, 'ATIVO'),             -- ID 7
('Papel Couché 170g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.35, 0.07, 'ATIVO'),             -- ID 8
('Papel Couché 250g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.55, 0.10, 'ATIVO'),             -- ID 9
('Papel Couché 300g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.70, 0.12, 'ATIVO'),             -- ID 10
('Papel A4 75g (500 folhas)', 'IMPRESSOS', 7, 'BASE', 'PACOTE', 35.00, 0.00, 'ATIVO'),   -- ID 11
('Papel A4 90g (500 folhas)', 'IMPRESSOS', 7, 'BASE', 'PACOTE', 45.00, 0.00, 'ATIVO'),   -- ID 12
('Papel Reciclado', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.18, 0.04, 'ATIVO'),               -- ID 13
('Papel Fotográfico', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 1.20, 0.10, 'ATIVO'),             -- ID 14
('Papel Vergê', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.60, 0.08, 'ATIVO'),                   -- ID 15
('Papel Kraft', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.45, 0.06, 'ATIVO'),                   -- ID 16
('Papel Supremo / Cartão', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.85, 0.10, 'ATIVO');        -- ID 17

-- =============================================================================
-- COMUNICAÇÃO VISUAL (COMUNICACAO_VISUAL, BASE)
-- =============================================================================
INSERT INTO materiais (nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status) VALUES
('Lona Front Light 280g', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 18.00, 6.00, 'ATIVO'),    -- ID 18
('Lona Front Light 340g', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 22.00, 7.00, 'ATIVO'),    -- ID 19
('Lona Front Light 440g', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 25.00, 8.90, 'ATIVO'),    -- ID 20
('Lona Backlight', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 42.00, 12.00, 'ATIVO'),          -- ID 21
('Adesivo Vinil Branco Brilho', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 30.00, 5.00, 'ATIVO'),  -- ID 22
('Adesivo Vinil Branco Fosco', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 32.00, 5.50, 'ATIVO'),   -- ID 23
('Adesivo Transparente', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 40.00, 6.50, 'ATIVO'),      -- ID 24
('Adesivo Perfurado (One Way Vision)', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 55.00, 8.00, 'ATIVO'), -- ID 25
('Adesivo Blackout', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 45.00, 7.00, 'ATIVO'),         -- ID 26
('Plotagem', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 18.00, 4.00, 'ATIVO'),                 -- ID 27
('Envelopamento', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 55.00, 10.00, 'ATIVO');           -- ID 28

-- =============================================================================
-- RÍGIDOS (RIGIDOS, BASE)
-- =============================================================================
INSERT INTO materiais (nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status) VALUES
('PVC Expandido 2mm', 'RIGIDOS', 1, 'BASE', 'AREA', 45.00, 10.00, 'ATIVO'),      -- ID 29
('PVC Expandido 5mm', 'RIGIDOS', 1, 'BASE', 'AREA', 75.00, 15.00, 'ATIVO'),      -- ID 30
('Acrílico 2mm', 'RIGIDOS', 1, 'BASE', 'AREA', 95.00, 18.00, 'ATIVO'),           -- ID 31
('Acrílico 3mm', 'RIGIDOS', 1, 'BASE', 'AREA', 120.00, 22.00, 'ATIVO'),          -- ID 32
('ACM 3mm', 'RIGIDOS', 1, 'BASE', 'AREA', 180.00, 25.00, 'ATIVO'),               -- ID 33
('MDF 3mm', 'RIGIDOS', 1, 'BASE', 'AREA', 55.00, 10.00, 'ATIVO'),                -- ID 34
('MDF 6mm', 'RIGIDOS', 1, 'BASE', 'AREA', 85.00, 15.00, 'ATIVO'),                -- ID 35
('Polionda', 'RIGIDOS', 1, 'BASE', 'AREA', 28.00, 5.00, 'ATIVO');                -- ID 36

-- =============================================================================
-- ACABAMENTOS (IMPRESSOS, ACABAMENTO)
-- =============================================================================
INSERT INTO materiais (nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status) VALUES
('Laminação Fosca', 'IMPRESSOS', 2, 'ACABAMENTO', 'SERVICO', 2.50, 0.50, 'ATIVO'),     -- ID 37
('Laminação Brilho', 'IMPRESSOS', 2, 'ACABAMENTO', 'SERVICO', 2.50, 0.50, 'ATIVO'),    -- ID 38
('Verniz UV', 'IMPRESSOS', 2, 'ACABAMENTO', 'UNIDADE', 0.20, 0.05, 'ATIVO'),           -- ID 39
('Corte Reto', 'IMPRESSOS', 2, 'ACABAMENTO', 'UNIDADE', 0.05, 0.01, 'ATIVO'),          -- ID 40
('Corte Especial (Plotter)', 'IMPRESSOS', 2, 'ACABAMENTO', 'SERVICO', 8.00, 2.00, 'ATIVO'), -- ID 41
('Dobra', 'IMPRESSOS', 2, 'ACABAMENTO', 'UNIDADE', 0.08, 0.02, 'ATIVO'),               -- ID 42
('Furação', 'IMPRESSOS', 2, 'ACABAMENTO', 'UNIDADE', 0.30, 0.05, 'ATIVO'),             -- ID 43
('Ilhós', 'IMPRESSOS', 2, 'ACABAMENTO', 'UNIDADE', 0.50, 0.10, 'ATIVO'),               -- ID 44
('Encadernação Espiral', 'IMPRESSOS', 2, 'ACABAMENTO', 'SERVICO', 8.00, 2.00, 'ATIVO'), -- ID 45
('Refile', 'IMPRESSOS', 2, 'ACABAMENTO', 'SERVICO', 5.00, 1.00, 'ATIVO');              -- ID 46

-- 6. Layouts de Produto (Filho de Produto)
INSERT INTO layouts_produto (id_produto, nome_layout, largura_mm, altura_mm) VALUES
(1, 'Cartão de Visita Padrão', 90, 50),
(1, 'Cartão de Visita Quadrado', 65, 65),
(1, 'Cartão de Visita Slim', 90, 40),
(2, 'Flyer A4', 210, 297),
(2, 'Flyer A5', 148, 210),
(2, 'Flyer A6', 105, 148),
(2, 'Flyer DL', 99, 210),
(4, 'Banner Padrão', 600, 900),
(4, 'Banner Grande', 900, 1200),
(5, 'Faixa 2x1m', 2000, 1000);

-- 7. Relacionamento: Produto x Materiais (Filho de Produto e Material)
-- Alinhado por categoria: IMPRESSOS usa papéis, COMUNICACAO_VISUAL usa lonas/adesivos, RIGIDOS usa acrílico/PVC
INSERT INTO produto_materiais (id_produto, id_material, material_padrao) VALUES
-- Cartão de Visita (IMPRESSOS) -> Papéis
(1, 9, TRUE),    -- Cartão -> Papel Couché 250g (ID 9)
(1, 2, FALSE),   -- Cartão -> Papel Sulfite 90g (ID 2)
(1, 17, FALSE),  -- Cartão -> Papel Supremo / Cartão (ID 17)

-- Flyer (IMPRESSOS) -> Papéis
(2, 7, TRUE),    -- Flyer -> Papel Couché 150g (ID 7)
(2, 5, FALSE),   -- Flyer -> Papel Couché 90g (ID 5)

-- Folder (IMPRESSOS) -> Papéis
(3, 9, TRUE),    -- Folder -> Papel Couché 250g (ID 9)
(3, 8, FALSE),   -- Folder -> Papel Couché 170g (ID 8)

-- Banner (COMUNICACAO_VISUAL) -> Lonas
(4, 20, TRUE),   -- Banner -> Lona Front Light 440g (ID 20)
(4, 18, FALSE),  -- Banner -> Lona Front Light 280g (ID 18)
(4, 21, FALSE),  -- Banner -> Lona Backlight (ID 21)

-- Faixa (COMUNICACAO_VISUAL) -> Lonas
(5, 19, TRUE),   -- Faixa -> Lona Front Light 340g (ID 19)
(5, 20, FALSE),  -- Faixa -> Lona Front Light 440g (ID 20)

-- Adesivo (COMUNICACAO_VISUAL) -> Adesivos
(6, 22, TRUE),   -- Adesivo -> Adesivo Vinil Branco Brilho (ID 22)
(6, 23, FALSE),  -- Adesivo -> Adesivo Vinil Branco Fosco (ID 23)
(6, 24, FALSE),  -- Adesivo -> Adesivo Transparente (ID 24)

-- Placa PVC (RIGIDOS) -> Rígidos
(7, 29, TRUE),   -- Placa PVC -> PVC Expandido 2mm (ID 29)
(7, 30, FALSE);  -- Placa PVC -> PVC Expandido 5mm (ID 30)

-- 8. Relacionamento: Produto x Acabamentos (Filho de Produto e Material)
-- Acabamentos apropriados por tipo de produto
INSERT INTO produto_acabamentos (id_produto, id_material, obrigatorio) VALUES
-- Cartão de Visita -> Acabamentos de impressão
(1, 37, FALSE), -- Cartão -> Laminação Fosca (ID 37)
(1, 38, FALSE), -- Cartão -> Laminação Brilho (ID 38)
(1, 39, FALSE), -- Cartão -> Verniz UV (ID 39)
(1, 40, FALSE), -- Cartão -> Corte Reto (ID 40)

-- Flyer -> Acabamentos de impressão
(2, 37, FALSE), -- Flyer -> Laminação Fosca (ID 37)
(2, 38, FALSE), -- Flyer -> Laminação Brilho (ID 38)
(2, 46, FALSE), -- Flyer -> Refile (ID 46)

-- Folder -> Acabamentos de impressão
(3, 37, FALSE), -- Folder -> Laminação Fosca (ID 37)
(3, 38, FALSE), -- Folder -> Laminação Brilho (ID 38)
(3, 42, FALSE), -- Folder -> Dobra (ID 42)

-- Banner -> Acabamentos de comunicação visual
(4, 44, FALSE), -- Banner -> Ilhós (ID 44)
(4, 46, FALSE), -- Banner -> Refile (ID 46)
(4, 41, FALSE), -- Banner -> Corte Especial (Plotter) (ID 41)

-- Faixa -> Acabamentos de comunicação visual
(5, 44, FALSE), -- Faixa -> Ilhós (ID 44)
(5, 46, FALSE), -- Faixa -> Refile (ID 46)

-- Adesivo -> Acabamentos de impressão
(6, 37, FALSE), -- Adesivo -> Laminação Fosca (ID 37)
(6, 38, FALSE), -- Adesivo -> Laminação Brilho (ID 38)
(6, 46, FALSE), -- Adesivo -> Refile (ID 46)

-- Placa PVC -> Acabamentos de rígidos
(7, 46, FALSE), -- Placa PVC -> Refile (ID 46)
(7, 41, FALSE); -- Placa PVC -> Corte Especial (Plotter) (ID 41)

-- 9. Escalas Produtivas (Filho de Material) - Baseada na Lona Front Light 440g (ID 20)
INSERT INTO escalas_produtivas (id_material, qtd_minima, qtd_maxima, desconto_percentual) VALUES
(20, 1, 10, 0.00),
(20, 11, 50, 5.00),
(20, 51, 500, 10.00);

-- 10. Orçamentos (Filho de Cliente e Usuario)
INSERT INTO orcamentos (id_cliente, id_usuario, data_emissao, data_validade, status, valor_bruto, margem_lucro_percentual, desconto_progressivo, valor_final) VALUES
(1, 1, NOW() - INTERVAL 2 DAY, CURDATE() + INTERVAL 13 DAY, 'PENDENTE', 4200.00, 28.00, 0.00, 5376.00),
(2, 2, NOW() - INTERVAL 1 DAY, CURDATE() + INTERVAL 14 DAY, 'APROVADO', 1800.00, 25.00, 2.00, 2205.00),
(3, 3, NOW(), CURDATE() + INTERVAL 15 DAY, 'REPROVADO', 950.00, 22.00, 0.00, 1159.00);

-- 11. Itens de Orçamento (Filho de Orcamento, Produto, Material, Layout)
INSERT INTO itens_orcamento (id_orcamento, id_produto, id_material, id_layout, largura_mm, altura_mm, quantidade, area_calculada, valor_bruto_item, valor_final_item, custo_unitario, tipo_cobranca_aplicado) VALUES
(1, 4, 20, 8, 3000, 1500, 2, 9.0000, 1200.00, 1536.00, 41.40, 'AREA'),   -- Banner + Lona Front Light 440g (ID 20)
(2, 6, 22, NULL, 1500, 1000, 1, 1.5000, 550.00, 673.75, 31.10, 'AREA'),   -- Adesivo + Adesivo Vinil Branco Brilho (ID 22)
(3, 2, 7, 4, 0, 0, 2500, 0.0000, 950.00, 1159.00, 0.47, 'TIRAGEM');       -- Flyer + Papel Couché 150g (ID 7)

-- 12. Auditoria
INSERT INTO auditoria_log (tabela, id_registro, campo, valor_antigo, valor_novo, id_usuario, data_evento) VALUES
('orcamentos', 1, 'status', 'PENDENTE', 'PENDENTE', 1, NOW() - INTERVAL 2 DAY),
('orcamentos', 2, 'status', 'PENDENTE', 'APROVADO', 2, NOW() - INTERVAL 1 DAY),
('materiais', 7, 'custo_base', '0.25', '0.28', 3, NOW());

SET FOREIGN_KEY_CHECKS = 1;