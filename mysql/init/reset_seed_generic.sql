-- Script para Resetar e Inicializar o Banco de Dados
-- Remove o banco existente e recria do zero com todos os dados iniciais

SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS orcamento_db;
CREATE DATABASE orcamento_db;
USE orcamento_db;

-- =============================================================================
-- 1. CRIAÇÃO DAS TABELAS (Ordem corrigida por dependências)
-- =============================================================================

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

CREATE TABLE materiais (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria ENUM('COMUNICACAO_VISUAL', 'IMPRESSOS') NOT NULL,
    id_categoria_lucro INT,
    tipo_item ENUM('BASE', 'ACABAMENTO') NOT NULL,
    tipo_cobranca ENUM('AREA', 'UNIDADE', 'TIRAGEM', 'FOLHA', 'PACOTE', 'SERVICO', 'CHAPA') NOT NULL,
    custo_base DECIMAL(10,2) NOT NULL,
    custo_producao DECIMAL(10,2) DEFAULT 0.00,
    status ENUM('ATIVO', 'INATIVO') DEFAULT 'ATIVO',
    FOREIGN KEY (id_categoria_lucro) REFERENCES categorias_lucro(id) ON DELETE SET NULL
);

CREATE TABLE layouts_produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_material INT NOT NULL,
    nome_layout VARCHAR(100) NOT NULL,
    largura_mm INT NOT NULL,
    altura_mm INT NOT NULL,
    FOREIGN KEY (id_material) REFERENCES materiais(id) ON DELETE CASCADE
);

CREATE TABLE escalas_produtivas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_material INT NOT NULL,
    qtd_minima INT NOT NULL,
    qtd_maxima INT NOT NULL,
    desconto_percentual DECIMAL(5,2) NOT NULL,
    FOREIGN KEY (id_material) REFERENCES materiais(id) ON DELETE CASCADE
);

CREATE TABLE orcamentos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_usuario INT NOT NULL,
    data_emissao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_validade DATE NOT NULL,
    status ENUM('PENDENTE', 'APROVADO', 'REPROVADO') DEFAULT 'PENDENTE',
    valor_bruto DECIMAL(10,2) NOT NULL,
    margem_lucro_percentual DECIMAL(5,2) NOT NULL,
    desconto_progressivo DECIMAL(5,2) DEFAULT 0.00,
    valor_final DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE itens_orcamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_orcamento INT NOT NULL,
    id_material INT NOT NULL,
    largura_mm INT DEFAULT 0,
    altura_mm INT DEFAULT 0,
    quantidade INT DEFAULT 1,
    area_calculada DECIMAL(10,4),
    valor_bruto_item DECIMAL(10,2) NOT NULL,
    valor_final_item DECIMAL(10,2) NOT NULL,
    custo_unitario DECIMAL(10,2) NOT NULL,
    tipo_cobranca_aplicado ENUM('AREA', 'UNIDADE', 'TIRAGEM', 'FOLHA', 'PACOTE', 'SERVICO', 'CHAPA') NOT NULL,
    FOREIGN KEY (id_orcamento) REFERENCES orcamentos(id) ON DELETE CASCADE,
    FOREIGN KEY (id_material) REFERENCES materiais(id)
);

CREATE TABLE auditoria_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tabela VARCHAR(50) NOT NULL,
    id_registro INT NOT NULL,
    campo VARCHAR(50) NOT NULL,
    valor_antigo VARCHAR(255),
    valor_novo VARCHAR(255),
    usuario VARCHAR(100),
    data_evento TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_itens_orcamento_orc ON itens_orcamento(id_orcamento);
CREATE INDEX idx_itens_orcamento_mat ON itens_orcamento(id_material);
CREATE INDEX idx_escalas_material ON escalas_produtivas(id_material);
CREATE INDEX idx_orcamentos_cliente ON orcamentos(id_cliente);

-- =============================================================================
-- 2. INSERÇÃO DE DADOS (Seeds)
-- =============================================================================

-- Categorias de Lucro
INSERT INTO categorias_lucro (nome, descricao, margem_padrao) VALUES 
('Impressão Digital Solvent', 'Banners, Lonas e Adesivos em grandes formatos', 45.00),
('Acabamento e Refile', 'Serviços de corte, solda eletrônica e ilhós', 60.00),
('Letra Caixa Inox', 'Letreiros metálicos com ou sem iluminação LED', 120.00),
('Serralheria Estrutural', 'Painéis, totens e cavaletes de metal', 85.00),
('Instalação em Altura', 'Serviços externos com necessidade de balancim', 150.00),
('Design e Vetorização', 'Criação de artes e ajuste de arquivos técnicos', 200.00),
('Impressos em Geral', 'Cartões, folhetos e papéis em geral', 100.00);

--Usuarios
INSERT INTO usuarios (nome, email, senha_hash, status) VALUES
('Administrador', 'admin@grafica.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'ATIVO'),
('Operador Comercial', 'comercial@grafica.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'ATIVO'),
('Gestor Produccion', 'gestor@grafica.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'ATIVO');

-- Clientes
INSERT INTO clientes (nome_razao_social, cpf_cnpj, email_contato, telefone_whatsapp, status, data_cadastro) VALUES
('Construtora Horizonte Ltda', '12345678000101', 'contato@horizonte.com', '11999990001', 'ATIVO', NOW() - INTERVAL 20 DAY),
('Mercado Central SA', '22345678000102', 'compras@mercadocentral.com', '11999990002', 'ATIVO', NOW() - INTERVAL 10 DAY),
('Clinica Vida Plena', '32345678000103', 'financeiro@vidaplena.com', '11999990003', 'ATIVO', NOW() - INTERVAL 5 DAY);

-- Materiais
INSERT INTO materiais (nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status) VALUES 
('Lona Front Light 440g', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 25.00, 8.90, 'ATIVO'),
('Adesivo Vinil Brilho', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 35.00, 6.20, 'ATIVO'),
('Papel Couché 250g', 'IMPRESSOS', 7, 'BASE', 'UNIDADE', 0.15, 0.05, 'ATIVO'),
('Papel Offset 90g', 'IMPRESSOS', 7, 'BASE', 'UNIDADE', 0.05, 0.02, 'ATIVO'),
('Flyer Couche 150g', 'IMPRESSOS', 7, 'BASE', 'TIRAGEM', 0.38, 0.09, 'ATIVO');

-- Layouts de Produto
INSERT INTO layouts_produto (id_material, nome_layout, largura_mm, altura_mm) VALUES 
(3, 'Cartão de Visita Padrão', 90, 50),
(3, 'Cartão de Visita Quadrado', 65, 65),
(3, 'Cartão de Visita Slim', 90, 40),
(4, 'Flyer A4', 210, 297),
(4, 'Flyer A5', 148, 210),
(4, 'Flyer A6', 105, 148),
(4, 'Flyer DL (Envelope)', 99, 210),
(1, 'Banner Padrão', 600, 900),
(1, 'Banner Grande', 900, 1200),
(1, 'Faixa 2x1m', 2000, 1000);

-- Escalas Produtivas
INSERT INTO escalas_produtivas (id_material, qtd_minima, qtd_maxima, desconto_percentual) VALUES
(1, 1, 10, 0.00),
(1, 11, 50, 5.00),
(1, 51, 500, 10.00);

-- Orçamentos
INSERT INTO orcamentos (id_cliente, id_usuario, data_emissao, data_validade, status, valor_bruto, margem_lucro_percentual, desconto_progressivo, valor_final) VALUES
(1, 1, NOW() - INTERVAL 2 DAY, CURDATE() + INTERVAL 13 DAY, 'PENDENTE', 4200.00, 28.00, 0.00, 5376.00),
(2, 2, NOW() - INTERVAL 1 DAY, CURDATE() + INTERVAL 14 DAY, 'APROVADO', 1800.00, 25.00, 2.00, 2205.00),
(3, 3, NOW(), CURDATE() + INTERVAL 15 DAY, 'REPROVADO', 950.00, 22.00, 0.00, 1159.00);

-- Itens de Orçamento
INSERT INTO itens_orcamento (id_orcamento, id_material, largura_mm, altura_mm, quantidade, area_calculada, valor_bruto_item, valor_final_item, custo_unitario, tipo_cobranca_aplicado) VALUES
(1, 1, 3000, 1500, 2, 9.0000, 1200.00, 1536.00, 41.40, 'AREA'),
(2, 2, 1500, 1000, 1, 1.5000, 550.00, 673.75, 31.10, 'AREA'),
(3, 5, 0, 0, 2500, 0.0000, 950.00, 1159.00, 0.47, 'TIRAGEM');

-- Auditoria
INSERT INTO auditoria_log (tabela, id_registro, campo, valor_antigo, valor_novo, usuario, data_evento) VALUES
('orcamentos', 1, 'status', 'PENDENTE', 'PENDENTE', 'admin@grafica.com', NOW() - INTERVAL 2 DAY),
('orcamentos', 2, 'status', 'PENDENTE', 'APROVADO', 'comercial@grafica.com', NOW() - INTERVAL 1 DAY),
('materiais', 5, 'custo_base', '0.35', '0.38', 'gestor@grafica.com', NOW());

SET FOREIGN_KEY_CHECKS = 1;
