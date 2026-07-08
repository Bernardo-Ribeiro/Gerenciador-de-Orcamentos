-- SQLite Seed Data for Budget Management System
-- Converted from MySQL seed script

-- =====================================================
-- 1. Categorias de Lucro (Parent Table)
-- =====================================================
INSERT INTO categorias_lucro (nome, descricao, margem_padrao) VALUES 
('Impressão Digital Solvent', 'Banners, Lonas e Adesivos em grandes formatos', 45.00),
('Acabamento e Refile', 'Serviços de corte, solda eletrônica e ilhós', 60.00),
('Letra Caixa Inox', 'Letreiros metálicos com ou sem iluminação LED', 120.00),
('Serralheria Estrutural', 'Painéis, totens e cavaletes de metal', 85.00),
('Instalação em Altura', 'Serviços externos com necessidade de balancim', 150.00),
('Design e Vetorização', 'Criação de artes e ajuste de arquivos técnicos', 200.00),
('Impressos em Geral', 'Cartões, folhetos e papéis em geral', 100.00);

-- =====================================================
-- 2. Usuários (Parent Table)
-- =====================================================
INSERT INTO usuarios (nome, email, senha_hash, status) VALUES
('Administrador', 'admin@grafica.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'ATIVO'),
('Operador Comercial', 'comercial@grafica.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'ATIVO'),
('Gestor Produccion', 'gestor@grafica.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'ATIVO');

-- =====================================================
-- 3. Clientes (Parent Table)
-- =====================================================
INSERT INTO clientes (nome_razao_social, cpf_cnpj, email_contato, telefone_whatsapp, status, data_cadastro) VALUES
('Construtora Horizonte Ltda', '12345678000101', 'contato@horizonte.com', '11999990001', 'ATIVO', datetime('now', '-20 days')),
('Mercado Central SA', '22345678000102', 'compras@mercadocentral.com', '11999990002', 'ATIVO', datetime('now', '-10 days')),
('Clinica Vida Plena', '32345678000103', 'financeiro@vidaplena.com', '11999990003', 'ATIVO', datetime('now', '-5 days'));

-- =====================================================
-- 4. Produtos (Parent Table)
-- =====================================================
INSERT INTO produtos (nome, categoria) VALUES
('Cartão de Visita', 'IMPRESSOS'),
('Flyer', 'IMPRESSOS'),
('Folder', 'IMPRESSOS'),
('Banner', 'COMUNICACAO_VISUAL'),
('Faixa', 'COMUNICACAO_VISUAL'),
('Adesivo', 'COMUNICACAO_VISUAL'),
('Placa PVC', 'RIGIDOS');

-- =====================================================
-- 5. Materiais (Base and Finishing)
-- =====================================================
-- PAPÉIS (IMPRESSOS, BASE)
INSERT INTO materiais (nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status) VALUES
('Papel Sulfite 75g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.08, 0.02, 'ATIVO'),
('Papel Sulfite 90g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.12, 0.03, 'ATIVO'),
('Papel Sulfite 120g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.18, 0.04, 'ATIVO'),
('Papel Sulfite 180g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.35, 0.08, 'ATIVO'),
('Papel Couché 90g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.15, 0.03, 'ATIVO'),
('Papel Couché 115g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.20, 0.04, 'ATIVO'),
('Papel Couché 150g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.28, 0.05, 'ATIVO'),
('Papel Couché 170g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.35, 0.07, 'ATIVO'),
('Papel Couché 250g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.55, 0.10, 'ATIVO'),
('Papel Couché 300g', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.70, 0.12, 'ATIVO'),
('Papel A4 75g (500 folhas)', 'IMPRESSOS', 7, 'BASE', 'PACOTE', 35.00, 0.00, 'ATIVO'),
('Papel A4 90g (500 folhas)', 'IMPRESSOS', 7, 'BASE', 'PACOTE', 45.00, 0.00, 'ATIVO'),
('Papel Reciclado', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.18, 0.04, 'ATIVO'),
('Papel Fotográfico', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 1.20, 0.10, 'ATIVO'),
('Papel Vergê', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.60, 0.08, 'ATIVO'),
('Papel Kraft', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.45, 0.06, 'ATIVO'),
('Papel Supremo / Cartão', 'IMPRESSOS', 7, 'BASE', 'FOLHA', 0.85, 0.10, 'ATIVO');

-- COMUNICAÇÃO VISUAL (COMUNICACAO_VISUAL, BASE)
INSERT INTO materiais (nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status) VALUES
('Lona Front Light 280g', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 18.00, 6.00, 'ATIVO'),
('Lona Front Light 340g', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 22.00, 7.00, 'ATIVO'),
('Lona Front Light 440g', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 25.00, 8.90, 'ATIVO'),
('Lona Backlight', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 42.00, 12.00, 'ATIVO'),
('Adesivo Vinil Branco Brilho', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 30.00, 5.00, 'ATIVO'),
('Adesivo Vinil Branco Fosco', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 32.00, 5.50, 'ATIVO'),
('Adesivo Transparente', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 40.00, 6.50, 'ATIVO'),
('Adesivo Perfurado (One Way Vision)', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 55.00, 8.00, 'ATIVO'),
('Adesivo Blackout', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 45.00, 7.00, 'ATIVO'),
('Plotagem', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 18.00, 4.00, 'ATIVO'),
('Envelopamento', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 55.00, 10.00, 'ATIVO');

-- RÍGIDOS (RIGIDOS, BASE)
INSERT INTO materiais (nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status) VALUES
('PVC Expandido 2mm', 'RIGIDOS', 1, 'BASE', 'AREA', 45.00, 10.00, 'ATIVO'),
('PVC Expandido 5mm', 'RIGIDOS', 1, 'BASE', 'AREA', 75.00, 15.00, 'ATIVO'),
('Acrílico 2mm', 'RIGIDOS', 1, 'BASE', 'AREA', 95.00, 18.00, 'ATIVO'),
('Acrílico 3mm', 'RIGIDOS', 1, 'BASE', 'AREA', 120.00, 22.00, 'ATIVO'),
('ACM 3mm', 'RIGIDOS', 1, 'BASE', 'AREA', 180.00, 25.00, 'ATIVO'),
('MDF 3mm', 'RIGIDOS', 1, 'BASE', 'AREA', 55.00, 10.00, 'ATIVO'),
('MDF 6mm', 'RIGIDOS', 1, 'BASE', 'AREA', 85.00, 15.00, 'ATIVO'),
('Polionda', 'RIGIDOS', 1, 'BASE', 'AREA', 28.00, 5.00, 'ATIVO');

-- ACABAMENTOS (IMPRESSOS, ACABAMENTO)
INSERT INTO materiais (nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status) VALUES
('Laminação Fosca', 'IMPRESSOS', 2, 'ACABAMENTO', 'SERVICO', 2.50, 0.50, 'ATIVO'),
('Laminação Brilho', 'IMPRESSOS', 2, 'ACABAMENTO', 'SERVICO', 2.50, 0.50, 'ATIVO'),
('Verniz UV', 'IMPRESSOS', 2, 'ACABAMENTO', 'UNIDADE', 0.20, 0.05, 'ATIVO'),
('Corte Reto', 'IMPRESSOS', 2, 'ACABAMENTO', 'UNIDADE', 0.05, 0.01, 'ATIVO'),
('Corte Especial (Plotter)', 'IMPRESSOS', 2, 'ACABAMENTO', 'SERVICO', 8.00, 2.00, 'ATIVO'),
('Dobra', 'IMPRESSOS', 2, 'ACABAMENTO', 'UNIDADE', 0.08, 0.02, 'ATIVO'),
('Furação', 'IMPRESSOS', 2, 'ACABAMENTO', 'UNIDADE', 0.30, 0.05, 'ATIVO'),
('Ilhós', 'IMPRESSOS', 2, 'ACABAMENTO', 'UNIDADE', 0.50, 0.10, 'ATIVO'),
('Encadernação Espiral', 'IMPRESSOS', 2, 'ACABAMENTO', 'SERVICO', 8.00, 2.00, 'ATIVO'),
('Refile', 'IMPRESSOS', 2, 'ACABAMENTO', 'SERVICO', 5.00, 1.00, 'ATIVO');

-- =====================================================
-- 6. Layouts de Produto (Child of Produto)
-- =====================================================
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

-- =====================================================
-- 7. Relacionamento: Produto x Materiais
-- =====================================================
INSERT INTO produto_materiais (id_produto, id_material, material_padrao) VALUES
-- Cartão de Visita (IMPRESSOS) -> Papéis
(1, 9, 1),
(1, 2, 0),
(1, 17, 0),
-- Flyer (IMPRESSOS) -> Papéis
(2, 7, 1),
(2, 5, 0),
-- Folder (IMPRESSOS) -> Papéis
(3, 9, 1),
(3, 8, 0),
-- Banner (COMUNICACAO_VISUAL) -> Lonas
(4, 20, 1),
(4, 18, 0),
(4, 21, 0),
-- Faixa (COMUNICACAO_VISUAL) -> Lonas
(5, 19, 1),
(5, 20, 0),
-- Adesivo (COMUNICACAO_VISUAL) -> Adesivos
(6, 22, 1),
(6, 23, 0),
(6, 24, 0),
-- Placa PVC (RIGIDOS) -> Rígidos
(7, 29, 1),
(7, 30, 0);

-- =====================================================
-- 8. Relacionamento: Produto x Acabamentos
-- =====================================================
INSERT INTO produto_acabamentos (id_produto, id_material, obrigatorio) VALUES
-- Cartão de Visita -> Acabamentos de impressão
(1, 37, 0),
(1, 38, 0),
(1, 39, 0),
(1, 40, 0),
-- Flyer -> Acabamentos de impressão
(2, 37, 0),
(2, 38, 0),
(2, 46, 0),
-- Folder -> Acabamentos de impressão
(3, 37, 0),
(3, 38, 0),
(3, 42, 0),
-- Banner -> Acabamentos de comunicação visual
(4, 44, 0),
(4, 46, 0),
(4, 41, 0),
-- Faixa -> Acabamentos de comunicação visual
(5, 44, 0),
(5, 46, 0),
-- Adesivo -> Acabamentos de impressão
(6, 37, 0),
(6, 38, 0),
(6, 46, 0),
-- Placa PVC -> Acabamentos de rígidos
(7, 46, 0),
(7, 41, 0);

-- =====================================================
-- 9. Escalas Produtivas (Child of Material)
-- =====================================================
INSERT INTO escalas_produtivas (id_material, qtd_minima, qtd_maxima, desconto_percentual) VALUES
(20, 1, 10, 0.00),
(20, 11, 50, 5.00),
(20, 51, 500, 10.00);

-- =====================================================
-- 10. Orçamentos (Child of Cliente and Usuario)
-- =====================================================
INSERT INTO orcamentos (id_cliente, id_usuario, data_emissao, data_validade, status, valor_bruto, margem_lucro_percentual, desconto_progressivo, valor_final) VALUES
(1, 1, date('now', '-2 days'), date('now', '+13 days'), 'PENDENTE', 4200.00, 28.00, 0.00, 5376.00),
(2, 2, date('now', '-1 day'), date('now', '+14 days'), 'APROVADO', 1800.00, 25.00, 2.00, 2205.00),
(3, 3, date('now'), date('now', '+15 days'), 'REPROVADO', 950.00, 22.00, 0.00, 1159.00);

-- =====================================================
-- 11. Itens de Orçamento (Child of Orcamento, Produto, Material, Layout)
-- =====================================================
INSERT INTO itens_orcamento (id_orcamento, id_produto, id_material, id_layout, largura_mm, altura_mm, quantidade, area_calculada, valor_bruto_item, valor_final_item, custo_unitario, tipo_cobranca_aplicado) VALUES
(1, 4, 20, 8, 3000, 1500, 2, 9.0000, 1200.00, 1536.00, 41.40, 'AREA'),
(2, 6, 22, NULL, 1500, 1000, 1, 1.5000, 550.00, 673.75, 31.10, 'AREA'),
(3, 2, 7, 4, 0, 0, 2500, 0.0000, 950.00, 1159.00, 0.47, 'TIRAGEM');

-- =====================================================
-- 12. Auditoria
-- =====================================================
INSERT INTO auditoria_log (tabela, id_registro, campo, valor_antigo, valor_novo, id_usuario, data_evento) VALUES
('orcamentos', 1, 'status', 'PENDENTE', 'PENDENTE', 1, datetime('now', '-2 days')),
('orcamentos', 2, 'status', 'PENDENTE', 'APROVADO', 2, datetime('now', '-1 day')),
('materiais', 7, 'custo_base', '0.25', '0.28', 3, datetime('now'));

-- =====================================================
-- 13. Configuração PDF (Default)
-- =====================================================
INSERT INTO configuracao_pdf (nome_empresa, rodape) 
VALUES ('Minha Gráfica', 'Este orçamento tem validade de 15 dias. Valores sujeitos a alteração sem aviso prévio.');