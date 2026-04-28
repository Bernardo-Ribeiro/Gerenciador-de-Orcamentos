CREATE DATABASE IF NOT EXISTS grafica_db;
USE grafica_db;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'Ativo'
);

-- 2. Tabla de Clientes (Gestión de Cadastro - RF002)
CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_razao_social VARCHAR(150) NOT NULL,
    cpf_cnpj VARCHAR(20) NOT NULL UNIQUE,
    email_contato VARCHAR(100),
    telefone_whatsapp VARCHAR(20),
    status VARCHAR(20) DEFAULT 'Ativo',
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabla de Materiales/Servicios (Gestión de Insumos - RF003 / RN001)
CREATE TABLE materiais (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,   -- 'Comunicação Visual' o 'Materiais Impressos'
    tipo_cobranca VARCHAR(20) NOT NULL, -- 'AREA' (m2) o 'UNIDADE' (tiragem)
    custo_base DECIMAL(10,2) NOT NULL,  -- Custo do insumo (fornecedor)
    custo_producao DECIMAL(10,2) DEFAULT 0.00, -- Custo de tempo/máquina
    status VARCHAR(20) DEFAULT 'Ativo'
);

-- 4. Tabla de Escala Productiva (Regla de Descuentos por Cantidad - RN003)
CREATE TABLE escalas_produtivas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_material INT NOT NULL,
    qtd_minima INT NOT NULL,
    qtd_maxima INT NOT NULL,
    desconto_percentual DECIMAL(5,2) NOT NULL,
    FOREIGN KEY (id_material) REFERENCES materiais(id) ON DELETE CASCADE
);

-- =================================================================
-- SPRINT 2 y 3: Presupuestos (Orçamentos) e Historial
-- =================================================================

-- 5. Tabla de Presupuestos (Elaborar Orçamento - RF004, RF005, RN006)
CREATE TABLE orcamentos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_usuario INT NOT NULL,
    data_emissao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_validade DATE NOT NULL, -- Se calcula a 15 días según RN006
    status VARCHAR(30) DEFAULT 'Pendente', -- 'Pendente', 'Aprovado (Venda)', 'Cancelado/Reprovado'
    valor_bruto DECIMAL(10,2) NOT NULL,
    margem_lucro_percentual DECIMAL(5,2) NOT NULL,
    desconto_progressivo DECIMAL(5,2) DEFAULT 0.00,
    valor_final DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

-- 6. Tabla de Detalles del Presupuesto (Los ítems dentro del presupuesto)
CREATE TABLE itens_orcamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_orcamento INT NOT NULL,
    id_material INT NOT NULL,
    largura_mm INT DEFAULT 0,  -- Para Comunicação Visual
    altura_mm INT DEFAULT 0,   -- Para Comunicação Visual
    quantidade INT NOT NULL,   -- Para Materiais Impressos
    valor_bruto_item DECIMAL(10,2) NOT NULL,
    valor_final_item DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_orcamento) REFERENCES orcamentos(id) ON DELETE CASCADE,
    FOREIGN KEY (id_material) REFERENCES materiais(id)
);

-- =================================================================
-- DATOS INICIALES (Para poder probar el login en el Sprint 1)
-- =================================================================
INSERT INTO usuarios (nome, email, senha_hash, cargo, status) 
VALUES ('Admin', 'admin@grafica.com', 'hash_generado_aqui', 'Administrador', 'Ativo');