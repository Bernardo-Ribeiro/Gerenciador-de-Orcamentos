-- [RN001.01] Obtener costo total de producción para material específico
SELECT 
    id, nome, categoria, tipo_cobranca,
    (custo_base + custo_producao) as costo_total_produccion,
    custo_base, custo_producao
FROM materiais 
WHERE id = ? AND status = 'ATIVO';

-- [RN001.02] Listar materiales de categoría "Comunicación Visual"
SELECT id, nome, categoria, tipo_item, tipo_cobranca, custo_base, custo_producao, (custo_base + custo_producao) as costo_total
FROM materiais 
WHERE categoria = 'COMUNICACAO_VISUAL' AND status = 'ATIVO'
ORDER BY nome ASC;

-- [RN001.03] Listar materiales de categoría "Materiales Impresos"
SELECT id, nome, categoria, tipo_item, tipo_cobranca, custo_base, custo_producao, (custo_base + custo_producao) as costo_total
FROM materiais 
WHERE categoria = 'IMPRESSOS' AND status = 'ATIVO'
ORDER BY nome ASC;

-- [RN003.01] Obtener descuento aplicable según cantidad
SELECT 
    IFNULL((
        SELECT desconto_percentual 
        FROM escalas_produtivas 
        WHERE id_material = ? 
        AND ? BETWEEN qtd_minima AND qtd_maxima 
        LIMIT 1
    ), 0) as desconto_aplicable;

-- [RN003.02] Validar si existe descuento para cantidad
SELECT EXISTS(
    SELECT 1 FROM escalas_produtivas 
    WHERE id_material = ? 
    AND ? BETWEEN qtd_minima AND qtd_maxima
) as existe_descuento;

-- [RN003.03] Obtener todos los tramos de descuento para un material
SELECT qtd_minima, qtd_maxima, desconto_percentual 
FROM escalas_produtivas 
WHERE id_material = ?
ORDER BY qtd_minima ASC;


-- [RN005.01] Validar si descuento es permitido según margen de lucro
SELECT 
    valor_bruto,
    margem_lucro_percentual,
    (valor_bruto * (1 + margem_lucro_percentual/100)) as valor_con_margem,
    CASE 
        WHEN ? <= (valor_bruto * margem_lucro_percentual/100) THEN 'PERMITIDO'
        ELSE 'RECHAZADO - Margen negativa'
    END as validacion_descuento
FROM orcamentos 
WHERE id = ?;

-- [RN005.02] Calcular descuento máximo permitido
SELECT 
    valor_bruto,
    margem_lucro_percentual,
    (valor_bruto * margem_lucro_percentual/100) as descuento_maximo_permitido
FROM orcamentos 
WHERE id = ?;

-- [RN006.02] Verificar si orçamento aún es válido
SELECT 
    id, data_emissao, data_validade,
    CASE 
        WHEN CURDATE() <= data_validade THEN 'VIGENTE'
        ELSE 'VENCIDO'
    END as estado_vigencia,
    DATEDIFF(data_validade, CURDATE()) as dias_restantes
FROM orcamentos 
WHERE id = ?;

-- [RN006.03] Listar orçamentos próximos a vencer (< 3 días)
SELECT id, id_cliente, data_validade, DATEDIFF(data_validade, CURDATE()) as dias_restantes
FROM orcamentos 
WHERE DATEDIFF(data_validade, CURDATE()) BETWEEN 0 AND 3 
AND status = 'PENDENTE'
ORDER BY data_validade ASC;

-- [RN006.04] Listar orçamentos vencidos
SELECT id, id_cliente, data_validade
FROM orcamentos 
WHERE CURDATE() > data_validade AND status = 'PENDENTE'
ORDER BY data_validade DESC;


-- [CALC.01] Obtener desglose completo de un orçamento (con cálculos)
SELECT 
    o.id as orcamento_id,
    o.valor_bruto,
    o.margem_lucro_percentual,
    (o.valor_bruto * (1 + o.margem_lucro_percentual/100)) as valor_con_margem,
    o.desconto_progressivo,
    o.valor_final,
    c.nome_razao_social as cliente_nome,
    u.nome as usuario_nombre,
    DATEDIFF(o.data_validade, CURDATE()) as dias_vigencia
FROM orcamentos o
LEFT JOIN clientes c ON o.id_cliente = c.id
LEFT JOIN usuarios u ON o.id_usuario = u.id
WHERE o.id = ?;

-- [CALC.02] Calcular valor total items de un orçamento
SELECT 
    id_orcamento,
    COUNT(*) as cantidad_items,
    SUM(valor_bruto_item) as bruto_total,
    SUM(valor_final_item) as final_total
FROM itens_orcamento 
WHERE id_orcamento = ?
GROUP BY id_orcamento;

-- [CALC.03] Desglose de items con margen aplicado
SELECT 
    io.id, io.id_material, m.nome,
    io.quantidade, 
    io.valor_bruto_item,
    ? as margem_percentual_aplicada,
    ROUND(io.valor_bruto_item * (1 + ?/100), 2) as valor_con_margem,
    io.valor_final_item,
    io.area_calculada,
    io.custo_unitario,
    io.tipo_cobranca_aplicado
FROM itens_orcamento io
JOIN materiais m ON io.id_material = m.id
WHERE io.id_orcamento = ?
ORDER BY io.id ASC;

-- [DIAG.01] Contar items por estado de orçamento
SELECT 
    status,
    COUNT(*) as total_orcamentos,
    SUM((SELECT COUNT(*) FROM itens_orcamento io WHERE io.id_orcamento = orcamentos.id)) as total_items
FROM orcamentos
GROUP BY status;

-- [DIAG.02] Verificar integridad referencial de orçamentos
SELECT o.id, COUNT(io.id) as items_count
FROM orcamentos o
LEFT JOIN itens_orcamento io ON o.id = io.id_orcamento
GROUP BY o.id
HAVING items_count = 0;

-- [DIAG.03] Detectar materials sin escala productiva
SELECT m.id, m.nome, COUNT(ep.id) as escalas_count
FROM materiais m
LEFT JOIN escalas_produtivas ep ON m.id = ep.id_material
WHERE m.status = 'ATIVO'
GROUP BY m.id
HAVING escalas_count = 0;
