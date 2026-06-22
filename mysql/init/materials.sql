-- =============================================================================
-- PAPÉIS
-- =============================================================================

INSERT INTO materiais
(nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status)
VALUES
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

-- =============================================================================
-- COMUNICAÇÃO VISUAL
-- =============================================================================

INSERT INTO materiais
(nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status)
VALUES
('Lona Front Light 280g', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 18.00, 6.00, 'ATIVO'),
('Lona Front Light 340g', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 22.00, 7.00, 'ATIVO'),
('Lona Front Light 440g', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 25.00, 8.90, 'ATIVO'),
('Lona Backlight', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 42.00, 12.00, 'ATIVO'),
('Adesivo Vinil Branco Brilho', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 30.00, 5.00, 'ATIVO'),
('Adesivo Vinil Branco Fosco', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 32.00, 5.50, 'ATIVO'),
('Adesivo Transparente', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 40.00, 6.50, 'ATIVO'),
('Adesivo Perfurado (One Way Vision)', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 55.00, 8.00, 'ATIVO'),
('Adesivo Blackout', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 45.00, 7.00, 'ATIVO'),
('Banner', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 25.00, 8.00, 'ATIVO'),
('Faixa', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 22.00, 7.00, 'ATIVO'),
('Plotagem', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 18.00, 4.00, 'ATIVO'),
('Envelopamento', 'COMUNICACAO_VISUAL', 1, 'BASE', 'AREA', 55.00, 10.00, 'ATIVO');

-- =============================================================================
-- RÍGIDOS (Corrigido para a categoria 'RIGIDOS')
-- =============================================================================

INSERT INTO materiais
(nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status)
VALUES
('PVC Expandido 2mm', 'RIGIDOS', 1, 'BASE', 'AREA', 45.00, 10.00, 'ATIVO'),
('PVC Expandido 5mm', 'RIGIDOS', 1, 'BASE', 'AREA', 75.00, 15.00, 'ATIVO'),
('Acrílico 2mm', 'RIGIDOS', 1, 'BASE', 'AREA', 95.00, 18.00, 'ATIVO'),
('Acrílico 3mm', 'RIGIDOS', 1, 'BASE', 'AREA', 120.00, 22.00, 'ATIVO'),
('ACM 3mm', 'RIGIDOS', 1, 'BASE', 'AREA', 180.00, 25.00, 'ATIVO'),
('MDF 3mm', 'RIGIDOS', 1, 'BASE', 'AREA', 55.00, 10.00, 'ATIVO'),
('MDF 6mm', 'RIGIDOS', 1, 'BASE', 'AREA', 85.00, 15.00, 'ATIVO'),
('Polionda', 'RIGIDOS', 1, 'BASE', 'AREA', 28.00, 5.00, 'ATIVO');

-- =============================================================================
-- ACABAMENTOS
-- =============================================================================

INSERT INTO materiais
(nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status)
VALUES
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