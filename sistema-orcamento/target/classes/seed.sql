BEGIN TRANSACTION;

-- =====================================================
-- 0. USUÁRIO PADRÃO
-- =====================================================

INSERT INTO usuarios (nome, email, senha_hash, status) VALUES
('Administrador', 'admin@grafica.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'ATIVO');

-- =====================================================
-- 1. PRODUTOS
-- =====================================================

INSERT INTO produtos (nome, categoria) VALUES
('Boné', 'RIGIDOS'),
('Caneca Térmica', 'RIGIDOS'),
('Copo Retrátil', 'RIGIDOS'),
('Bloco de Anotações', 'IMPRESSOS'),
('Canivete Retrátil', 'RIGIDOS'),
('Caneta Esferográfica', 'RIGIDOS'),
('Chaveiro com Lanterna LED', 'RIGIDOS'),
('Sacola Ecológica', 'RIGIDOS'),
('Cuia', 'RIGIDOS'),
('Kit Garrafa Térmica', 'RIGIDOS'),
('Folder', 'IMPRESSOS'),
('Cartilha', 'IMPRESSOS');


-- =====================================================
-- 2. LAYOUTS / MEDIDAS
-- Somente medidas explicitamente informadas
-- =====================================================

-- Sacola: 35 x 40 cm
INSERT INTO layouts_produto (
    id_produto, nome_layout, largura_mm, altura_mm
)
SELECT
    id, '35 x 40 cm', 350, 400
FROM produtos
WHERE nome = 'Sacola Ecológica';


-- Folder: A4 = 21 x 29,7 cm
INSERT INTO layouts_produto (
    id_produto, nome_layout, largura_mm, altura_mm
)
SELECT
    id, 'A4 (21 x 29,7 cm)', 210, 297
FROM produtos
WHERE nome = 'Folder';


-- Cartilha: 13 x 21 cm fechada
INSERT INTO layouts_produto (
    id_produto, nome_layout, largura_mm, altura_mm
)
SELECT
    id, '13 x 21 cm', 130, 210
FROM produtos
WHERE nome = 'Cartilha';


-- =====================================================
-- 3. MATERIAIS BASE
--
-- custo_base = custo unitário informado na imagem.
-- Nos casos com escala específica, representa o custo
-- de referência/fallback.
-- =====================================================

INSERT INTO materiais (
    nome,
    categoria,
    tipo_item,
    tipo_cobranca,
    custo_base,
    custo_producao,
    status
) VALUES

-- Boné
(
    'Poliéster off-white com personalização em serigrafia',
    'RIGIDOS',
    'BASE',
    'UNIDADE',
    33.10,
    0.00,
    'ATIVO'
),

-- Caneca térmica
(
    'Inox 350 ml com parede dupla e personalização a laser',
    'RIGIDOS',
    'BASE',
    'UNIDADE',
    48.85,
    0.00,
    'ATIVO'
),

-- Copo retrátil
(
    'Silicone 350 ml livre de BPA com personalização',
    'RIGIDOS',
    'BASE',
    'UNIDADE',
    31.00,
    0.00,
    'ATIVO'
),

-- Bloco de anotações
(
    'Material reciclável com folhas pautadas e personalização',
    'IMPRESSOS',
    'BASE',
    'UNIDADE',
    19.00,
    0.00,
    'ATIVO'
),

-- Canivete
(
    'Aço inoxidável com trava e gravação a laser',
    'RIGIDOS',
    'BASE',
    'UNIDADE',
    58.20,
    0.00,
    'ATIVO'
),

-- Caneta
(
    'Caneta branca e verde com carga azul e personalização',
    'RIGIDOS',
    'BASE',
    'UNIDADE',
    6.35,
    0.00,
    'ATIVO'
),

-- Chaveiro
(
    'Alumínio com lanterna LED e 4 pilhas LR44',
    'RIGIDOS',
    'BASE',
    'UNIDADE',
    14.22,
    0.00,
    'ATIVO'
),

-- Sacola
(
    'Algodão off-white com serigrafia frente e verso',
    'RIGIDOS',
    'BASE',
    'UNIDADE',
    21.69,
    0.00,
    'ATIVO'
),

-- Cuia
(
    'Madeira de imbuia com detalhe verde e gravação a laser',
    'RIGIDOS',
    'BASE',
    'UNIDADE',
    78.00,
    0.00,
    'ATIVO'
),

-- Kit garrafa
(
    'Garrafa térmica inox 450 ml com duas tampas/xícaras',
    'RIGIDOS',
    'BASE',
    'UNIDADE',
    84.00,
    0.00,
    'ATIVO'
),

-- Folder
(
    'Papel couchê 120g, brilho, impressão offset e 2 dobras',
    'IMPRESSOS',
    'BASE',
    'TIRAGEM',
    1.359,
    0.00,
    'ATIVO'
),

-- Cartilha
(
    '32 páginas, capa reciclado 180g, miolo reciclado 90g, 4x4 cores, alceada e grampeada',
    'IMPRESSOS',
    'BASE',
    'TIRAGEM',
    16.932,
    0.00,
    'ATIVO'
);


-- =====================================================
-- 4. RELACIONAMENTO PRODUTO x MATERIAL
-- =====================================================

INSERT INTO produto_materiais (
    id_produto,
    id_material,
    material_padrao
)
SELECT p.id, m.id, 1
FROM produtos p
JOIN materiais m ON

       (p.nome = 'Boné'
        AND m.nome =
        'Poliéster off-white com personalização em serigrafia')

    OR (p.nome = 'Caneca Térmica'
        AND m.nome =
        'Inox 350 ml com parede dupla e personalização a laser')

    OR (p.nome = 'Copo Retrátil'
        AND m.nome =
        'Silicone 350 ml livre de BPA com personalização')

    OR (p.nome = 'Bloco de Anotações'
        AND m.nome =
        'Material reciclável com folhas pautadas e personalização')

    OR (p.nome = 'Canivete Retrátil'
        AND m.nome =
        'Aço inoxidável com trava e gravação a laser')

    OR (p.nome = 'Caneta Esferográfica'
        AND m.nome =
        'Caneta branca e verde com carga azul e personalização')

    OR (p.nome = 'Chaveiro com Lanterna LED'
        AND m.nome =
        'Alumínio com lanterna LED e 4 pilhas LR44')

    OR (p.nome = 'Sacola Ecológica'
        AND m.nome =
        'Algodão off-white com serigrafia frente e verso')

    OR (p.nome = 'Cuia'
        AND m.nome =
        'Madeira de imbuia com detalhe verde e gravação a laser')

    OR (p.nome = 'Kit Garrafa Térmica'
        AND m.nome =
        'Garrafa térmica inox 450 ml com duas tampas/xícaras')

    OR (p.nome = 'Folder'
        AND m.nome =
        'Papel couchê 120g, brilho, impressão offset e 2 dobras')

    OR (p.nome = 'Cartilha'
        AND m.nome =
        '32 páginas, capa reciclado 180g, miolo reciclado 90g, 4x4 cores, alceada e grampeada');


-- =====================================================
-- 5. ESCALAS PRODUTIVAS
-- =====================================================

-- Boné: 200 unidades = R$ 33,10/un.
INSERT INTO escalas_produtivas
(id_material, qtd_minima, qtd_maxima, desconto_percentual, custo_unitario)
SELECT id, 200, 200, 0.00, 33.10
FROM materiais
WHERE nome = 'Poliéster off-white com personalização em serigrafia';


-- Caneca térmica: 200 unidades = R$ 48,85/un.
INSERT INTO escalas_produtivas
(id_material, qtd_minima, qtd_maxima, desconto_percentual, custo_unitario)
SELECT id, 200, 200, 0.00, 48.85
FROM materiais
WHERE nome = 'Inox 350 ml com parede dupla e personalização a laser';


-- Copo retrátil: 200 unidades = R$ 31,00/un.
INSERT INTO escalas_produtivas
(id_material, qtd_minima, qtd_maxima, desconto_percentual, custo_unitario)
SELECT id, 200, 200, 0.00, 31.00
FROM materiais
WHERE nome = 'Silicone 350 ml livre de BPA com personalização';


-- Bloco: 200 unidades = R$ 19,00/un.
INSERT INTO escalas_produtivas
(id_material, qtd_minima, qtd_maxima, desconto_percentual, custo_unitario)
SELECT id, 200, 200, 0.00, 19.00
FROM materiais
WHERE nome = 'Material reciclável com folhas pautadas e personalização';


-- Canivete: 200 unidades = R$ 58,20/un.
INSERT INTO escalas_produtivas
(id_material, qtd_minima, qtd_maxima, desconto_percentual, custo_unitario)
SELECT id, 200, 200, 0.00, 58.20
FROM materiais
WHERE nome = 'Aço inoxidável com trava e gravação a laser';


-- Caneta: 200 unidades = R$ 6,35/un.
INSERT INTO escalas_produtivas
(id_material, qtd_minima, qtd_maxima, desconto_percentual, custo_unitario)
SELECT id, 200, 200, 0.00, 6.35
FROM materiais
WHERE nome = 'Caneta branca e verde com carga azul e personalização';


-- Chaveiro: 200 unidades = R$ 14,22/un.
INSERT INTO escalas_produtivas
(id_material, qtd_minima, qtd_maxima, desconto_percentual, custo_unitario)
SELECT id, 200, 200, 0.00, 14.22
FROM materiais
WHERE nome = 'Alumínio com lanterna LED e 4 pilhas LR44';


-- Sacola: 200 unidades = R$ 21,69/un.
INSERT INTO escalas_produtivas
(id_material, qtd_minima, qtd_maxima, desconto_percentual, custo_unitario)
SELECT id, 200, 200, 0.00, 21.69
FROM materiais
WHERE nome = 'Algodão off-white com serigrafia frente e verso';


-- Cuia: 50 unidades = R$ 78,00/un.
INSERT INTO escalas_produtivas
(id_material, qtd_minima, qtd_maxima, desconto_percentual, custo_unitario)
SELECT id, 50, 50, 0.00, 78.00
FROM materiais
WHERE nome = 'Madeira de imbuia com detalhe verde e gravação a laser';


-- Kit garrafa: 50 unidades = R$ 84,00/un.
INSERT INTO escalas_produtivas
(id_material, qtd_minima, qtd_maxima, desconto_percentual, custo_unitario)
SELECT id, 50, 50, 0.00, 84.00
FROM materiais
WHERE nome = 'Garrafa térmica inox 450 ml com duas tampas/xícaras';


-- Folder: 1.000 unidades = R$ 1,359/un.
INSERT INTO escalas_produtivas
(id_material, qtd_minima, qtd_maxima, desconto_percentual, custo_unitario)
SELECT id, 1000, 1000, 0.00, 1.359
FROM materiais
WHERE nome = 'Papel couchê 120g, brilho, impressão offset e 2 dobras';


-- Cartilha: 200 unidades = R$ 16,932/un.
INSERT INTO escalas_produtivas
(id_material, qtd_minima, qtd_maxima, desconto_percentual, custo_unitario)
SELECT id, 200, 200, 0.00, 16.932
FROM materiais
WHERE nome =
'32 páginas, capa reciclado 180g, miolo reciclado 90g, 4x4 cores, alceada e grampeada';


-- Cartilha: 500 unidades = R$ 9,96/un.
INSERT INTO escalas_produtivas
(id_material, qtd_minima, qtd_maxima, desconto_percentual, custo_unitario)
SELECT id, 500, 500, 0.00, 9.96
FROM materiais
WHERE nome =
'32 páginas, capa reciclado 180g, miolo reciclado 90g, 4x4 cores, alceada e grampeada';


COMMIT;