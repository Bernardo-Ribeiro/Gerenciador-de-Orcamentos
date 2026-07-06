-- Script para adicionar suporte a PDFs no banco de dados existente
USE orcamento_db;

-- Adicionar coluna id_produto na tabela itens_orcamento
ALTER TABLE itens_orcamento 
ADD COLUMN id_produto INT NOT NULL AFTER id_orcamento,
ADD COLUMN id_layout INT AFTER id_material,
ADD FOREIGN KEY (id_produto) REFERENCES produtos(id),
ADD FOREIGN KEY (id_layout) REFERENCES layouts_produto(id);

-- Criar tabela de configuração de PDF
CREATE TABLE IF NOT EXISTS configuracao_pdf (
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

-- Inserir configuração padrão
INSERT INTO configuracao_pdf (nome_empresa, rodape) 
VALUES ('Minha Gráfica', 'Este orçamento tem validade de 15 dias. Valores sujeitos a alteração sem aviso prévio.');