CREATE DATABASE IF NOT EXISTS orcamento_db;
USE orcamento_db;


-- Tabela de usuários, clientes, materiais, escalas produtivas, orçamentos, itens de orçamento e auditoria
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
    
    FOREIGN KEY (id_orcamento) REFERENCES orcamentos(id) ON DELETE CASCADE,
    FOREIGN KEY (id_produto) REFERENCES produtos(id),
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

CREATE TABLE categorias_lucro (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    margem_padrao DECIMAL(5,2) NOT NULL DEFAULT 0.00
);

-- Índices para otimizar consultas
CREATE INDEX idx_itens_orcamento_orc ON itens_orcamento(id_orcamento);
CREATE INDEX idx_itens_orcamento_mat ON itens_orcamento(id_material);
CREATE INDEX idx_escalas_material ON escalas_produtivas(id_material);
CREATE INDEX idx_orcamentos_cliente ON orcamentos(id_cliente);

CREATE TABLE configuracao_pdf (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_empresa VARCHAR(150),
    cnpj VARCHAR(20),
    endereco VARCHAR(255),
    telefone VARCHAR(20),
    email VARCHAR(100),
    logo_path VARCHAR(255),
    rodape VARCHAR(255),
    cores VARCHAR(20),
    fonte VARCHAR(50)
);