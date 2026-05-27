USE orcamento_db;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE itens_orcamento;
TRUNCATE TABLE escalas_produtivas;
TRUNCATE TABLE auditoria_log;
TRUNCATE TABLE orcamentos;
TRUNCATE TABLE materiais;
TRUNCATE TABLE clientes;
TRUNCATE TABLE usuarios;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO usuarios (nome, email, senha_hash, status) VALUES
('Administrador', 'admin@grafica.com', 'admin', 'ATIVO'),
('Operador Comercial', 'comercial@grafica.com', '123456', 'ATIVO'),
('Gestor Produccion', 'gestor@grafica.com', '123456', 'ATIVO');

INSERT INTO clientes (nome_razao_social, cpf_cnpj, email_contato, telefone_whatsapp, status, data_cadastro) VALUES
('Construtora Horizonte Ltda', '12345678000101', 'contato@horizonte.com', '11999990001', 'ATIVO', NOW() - INTERVAL 20 DAY),
('Mercado Central SA', '22345678000102', 'compras@mercadocentral.com', '11999990002', 'ATIVO', NOW() - INTERVAL 10 DAY),
('Clinica Vida Plena', '32345678000103', 'financeiro@vidaplena.com', '11999990003', 'ATIVO', NOW() - INTERVAL 5 DAY);

INSERT INTO materiais (nome, categoria, tipo_item, tipo_cobranca, custo_base, custo_producao, status) VALUES
('Lona Front 440g', 'COMUNICACAO_VISUAL', 'BASE', 'AREA', 32.50, 8.90, 'ATIVO'),
('Vinil Adesivo Fosco', 'COMUNICACAO_VISUAL', 'BASE', 'AREA', 24.90, 6.20, 'ATIVO'),
('Flyer Couche 150g', 'IMPRESSOS', 'BASE', 'TIRAGEM', 0.38, 0.09, 'ATIVO');

INSERT INTO escalas_produtivas (id_material, qtd_minima, qtd_maxima, desconto_percentual) VALUES
(1, 1, 10, 0.00),
(2, 11, 50, 5.00),
(3, 51, 500, 10.00);

INSERT INTO orcamentos (id_cliente, id_usuario, data_emissao, data_validade, status, valor_bruto, margem_lucro_percentual, desconto_progressivo, valor_final) VALUES
(1, 1, NOW() - INTERVAL 2 DAY, CURDATE() + INTERVAL 13 DAY, 'PENDENTE', 4200.00, 28.00, 0.00, 5376.00),
(2, 2, NOW() - INTERVAL 1 DAY, CURDATE() + INTERVAL 14 DAY, 'APROVADO', 1800.00, 25.00, 2.00, 2205.00),
(3, 3, NOW(), CURDATE() + INTERVAL 15 DAY, 'REPROVADO', 950.00, 22.00, 0.00, 1159.00);

INSERT INTO itens_orcamento (id_orcamento, id_material, largura_mm, altura_mm, quantidade, area_calculada, valor_bruto_item, valor_final_item, custo_unitario, tipo_cobranca_aplicado) VALUES
(1, 1, 3000, 1500, 2, 9.0000, 1200.00, 1536.00, 41.40, 'AREA'),
(2, 2, 1500, 1000, 1, 1.5000, 550.00, 673.75, 31.10, 'AREA'),
(3, 3, 0, 0, 2500, 0.0000, 950.00, 1159.00, 0.47, 'TIRAGEM');

INSERT INTO auditoria_log (tabela, id_registro, campo, valor_antigo, valor_novo, usuario, data_evento) VALUES
('orcamentos', 1, 'status', 'PENDENTE', 'PENDENTE', 'admin@grafica.com', NOW() - INTERVAL 2 DAY),
('orcamentos', 2, 'status', 'PENDENTE', 'APROVADO', 'comercial@grafica.com', NOW() - INTERVAL 1 DAY),
('materiais', 3, 'custo_base', '0.35', '0.38', 'gestor@grafica.com', NOW());
