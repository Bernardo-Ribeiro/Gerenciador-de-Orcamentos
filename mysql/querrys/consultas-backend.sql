-- [RF001.01] Login - Validar credenciales del usuario
SELECT id, nome, email, status 
FROM usuarios 
WHERE email = ? AND senha_hash = ? AND status = 'Ativo';

-- [RF001.02] Obtener usuario por ID
SELECT id, nome, email, status 
FROM usuarios 
WHERE id = ?;

-- [RF001.03] Listar todos los usuarios
SELECT id, nome, email, status 
FROM usuarios 
ORDER BY nome ASC;

-- [RF001.04] Crear nuevo usuario
INSERT INTO usuarios (nome, email, senha_hash, status) 
VALUES (?, ?, ?, 'Ativo');

-- [RF001.05] Actualizar usuario
UPDATE usuarios 
SET nome = ?, email = ?, status = ? 
WHERE id = ?;

-- [RF001.06] Cambiar contraseña de usuario
UPDATE usuarios 
SET senha_hash = ? 
WHERE id = ? AND status = 'Ativo';

-- [RF001.07] Bloquear/Inactivar usuario
UPDATE usuarios 
SET status = 'Inativo' 
WHERE id = ?;

-- [RF002.01] Crear nuevo cliente
INSERT INTO clientes (nome, cpf_cnpj, email_contato, telefone_whatsapp, status) 
VALUES (?, ?, ?, ?, 'Ativo');

-- [RF002.02] Obtener cliente por ID
SELECT id, nome, cpf_cnpj, email_contato, telefone_whatsapp, status, data_cadastro 
FROM clientes 
WHERE id = ?;

-- [RF002.03] Obtener cliente por CNPJ/CPF
SELECT id, nome, cpf_cnpj, email_contato, telefone_whatsapp, status, data_cadastro 
FROM clientes 
WHERE cpf_cnpj = ?;

-- [RF002.04] Listar todos los clientes activos
SELECT id, nome, cpf_cnpj, email_contato, telefone_whatsapp, status, data_cadastro 
FROM clientes 
WHERE status = 'Ativo' 
ORDER BY nome ASC;

-- [RF002.05] Listar todos los clientes (incluyendo inactivos)
SELECT id, nome, cpf_cnpj, email_contato, telefone_whatsapp, status, data_cadastro 
FROM clientes 
ORDER BY data_cadastro DESC;

-- [RF002.06] Actualizar datos del cliente
UPDATE clientes 
SET nome = ?, cpf_cnpj = ?, email_contato = ?, telefone_whatsapp = ?, status = ? 
WHERE id = ?;

-- [RF002.07] Inactivar cliente
UPDATE clientes 
SET status = 'Inativo' 
WHERE id = ?;

-- [RF002.08] Buscar cliente por nombre (autocomplete)
SELECT id, nome, cpf_cnpj 
FROM clientes 
WHERE nome LIKE ? AND status = 'Ativo' 
LIMIT 10;

-- [RF003.01] Crear nuevo material/servicio
INSERT INTO materiais (nome, categoria, custo_base, custo_producao, status) 
VALUES (?, ?, ?, ?, 'Ativo');

-- [RF003.02] Obtener material por ID
SELECT id, nome, categoria, custo_base, custo_producao, status 
FROM materiais 
WHERE id = ?;

-- [RF003.03] Listar todos los materiales activos
SELECT id, nome, categoria, custo_base, custo_producao, status 
FROM materiais 
WHERE status = 'Ativo' 
ORDER BY categoria, nome ASC;

-- [RF003.04] Listar materiales por categoría
SELECT id, nome, categoria, custo_base, custo_producao, status 
FROM materiais 
WHERE categoria = ? AND status = 'Ativo' 
ORDER BY nome ASC;

-- [RF003.05] Actualizar material
UPDATE materiais 
SET nome = ?, categoria = ?, custo_base = ?, custo_producao = ?, status = ? 
WHERE id = ?;

-- [RF003.06] Inactivar material
UPDATE materiais 
SET status = 'Inativo' 
WHERE id = ?;

-- [RF003.07] Buscar material por nombre (autocomplete)
SELECT id, nome, categoria, custo_base, custo_producao 
FROM materiais 
WHERE nome LIKE ? AND status = 'Ativo' 
LIMIT 10;

-- [RN003.01] Obtener escala productiva aplicable a una cantidad
SELECT desconto_percentual 
FROM escalas_produtivas 
WHERE id_material = ? AND ? BETWEEN qtd_minima AND qtd_maxima 
LIMIT 1;

-- [RN003.02] Listar todas las escalas de un material
SELECT id, qtd_minima, qtd_maxima, desconto_percentual 
FROM escalas_produtivas 
WHERE id_material = ? 
ORDER BY qtd_minima ASC;

-- [RN003.03] Crear nueva escala productiva
INSERT INTO escalas_produtivas (id_material, qtd_minima, qtd_maxima, desconto_percentual) 
VALUES (?, ?, ?, ?);

-- [RN003.04] Actualizar escala productiva
UPDATE escalas_produtivas 
SET qtd_minima = ?, qtd_maxima = ?, desconto_percentual = ? 
WHERE id = ?;

-- [RN003.05] Eliminar escala productiva
DELETE FROM escalas_produtivas 
WHERE id = ?;

-- [RN003.06] Obtener el descuento máximo para un material
SELECT MAX(desconto_percentual) as desconto_maximo 
FROM escalas_produtivas 
WHERE id_material = ?;

-- [RF004.01] Crear nuevo orçamento
INSERT INTO orcamentos (id_cliente, id_usuario, data_validade, status, valor_bruto, margem_lucro_percentual, desconto_progressivo, valor_final) 
VALUES (?, ?, ?, 'Pendente', ?, ?, ?, ?);

-- [RF004.02] Obtener orçamento por ID (completo)
SELECT 
    o.id, o.id_cliente, o.id_usuario, o.data_emissao, o.data_validade, o.status,
    o.valor_bruto, o.margem_lucro_percentual, o.desconto_progressivo, o.valor_final,
    c.nome as cliente_nome, u.nome as usuario_nome
FROM orcamentos o
LEFT JOIN clientes c ON o.id_cliente = c.id
LEFT JOIN usuarios u ON o.id_usuario = u.id
WHERE o.id = ?;

-- [RF004.03] Obtener todos los items de un orçamento
SELECT 
    io.id, io.id_material, io.largura_mm, io.altura_mm, io.quantidade,
    io.valor_bruto_item, io.valor_final_item,
    m.nome as material_nome, m.categoria, m.custo_base, m.custo_producao
FROM itens_orcamento io
LEFT JOIN materiais m ON io.id_material = m.id
WHERE io.id_orcamento = ?
ORDER BY io.id ASC;

-- [RF004.04] Agregar item al orçamento
INSERT INTO itens_orcamento (id_orcamento, id_material, largura_mm, altura_mm, quantidade, valor_bruto_item, valor_final_item)
VALUES (?, ?, ?, ?, ?, ?, ?);

-- [RF004.05] Actualizar item del orçamento
UPDATE itens_orcamento 
SET largura_mm = ?, altura_mm = ?, quantidade = ?, valor_bruto_item = ?, valor_final_item = ?
WHERE id = ?;

-- [RF004.06] Eliminar item del orçamento
DELETE FROM itens_orcamento 
WHERE id = ? AND id_orcamento = ?;

-- [RF004.07] Actualizar totales del orçamento
UPDATE orcamentos 
SET valor_bruto = ?, margem_lucro_percentual = ?, desconto_progressivo = ?, valor_final = ?
WHERE id = ?;

-- [RF004.08] Verificar si orçamento permite descuento (RN005 - Límite de descuento)
SELECT 
    valor_bruto, margem_lucro_percentual, desconto_progressivo, valor_final,
    (valor_bruto * (1 + margem_lucro_percentual/100)) as valor_con_margem
FROM orcamentos 
WHERE id = ?;

-- [RF007.01] Cambiar status del orçamento
UPDATE orcamentos 
SET status = ? 
WHERE id = ?;

-- [RF007.02] Obtener orçamentos por status
SELECT id, numero_orcamento, id_cliente, data_emissao, status, valor_final
FROM orcamentos 
WHERE status = ?
ORDER BY data_emissao DESC;

-- [RF007.03] Verificar si orçamento puede ser modificado (si no está aprobado)
SELECT status FROM orcamentos WHERE id = ?;

-- [RF008.01] Listar histórico de orçamentos por cliente
SELECT 
    o.id, o.data_emissao, o.status, o.valor_final,
    u.nome as usuario, c.nome as cliente
FROM orcamentos o
LEFT JOIN clientes c ON o.id_cliente = c.id
LEFT JOIN usuarios u ON o.id_usuario = u.id
WHERE o.id_cliente = ? 
ORDER BY o.data_emissao DESC;

-- [RF008.02] Buscar orçamentos por rango de fechas
SELECT id, data_emissao, id_cliente, status, valor_final
FROM orcamentos 
WHERE data_emissao BETWEEN ? AND ?
ORDER BY data_emissao DESC;

-- [RF008.03] Buscar orçamentos por cliente y rango de fechas
SELECT 
    o.id, o.data_emissao, o.status, o.valor_final, o.valor_bruto, o.margem_lucro_percentual,
    c.nome as cliente, u.nome as usuario
FROM orcamentos o
LEFT JOIN clientes c ON o.id_cliente = c.id
LEFT JOIN usuarios u ON o.id_usuario = u.id
WHERE o.id_cliente = ? AND o.data_emissao BETWEEN ? AND ?
ORDER BY o.data_emissao DESC;

-- [RF009.01] Reporte - Total de orçamentos emitidos (período)
SELECT 
    COUNT(*) as total_orcamentos,
    SUM(CASE WHEN status = 'Aprovado (Venda)' THEN 1 ELSE 0 END) as orcamentos_aprovados,
    SUM(valor_final) as valor_total_emitido,
    SUM(CASE WHEN status = 'Aprovado (Venda)' THEN valor_final ELSE 0 END) as valor_aprovado
FROM orcamentos 
WHERE data_emissao BETWEEN ? AND ?;

-- [RF009.02] Reporte - Taxa de conversión de orçamentos
SELECT 
    COUNT(*) as total_orcamentos,
    SUM(CASE WHEN status = 'Aprovado (Venda)' THEN 1 ELSE 0 END) as orcamentos_vendidos,
    ROUND(
        (SUM(CASE WHEN status = 'Aprovado (Venda)' THEN 1 ELSE 0 END) / COUNT(*)) * 100, 
        2
    ) as taxa_conversao_percentual
FROM orcamentos 
WHERE data_emissao BETWEEN ? AND ?;

-- [RF009.03] Reporte - Materiales más utilizados
SELECT 
    m.nome, m.categoria,
    COUNT(io.id) as vezes_utilizado,
    SUM(io.quantidade) as quantidade_total,
    SUM(io.valor_final_item) as valor_total
FROM itens_orcamento io
JOIN materiais m ON io.id_material = m.id
JOIN orcamentos o ON io.id_orcamento = o.id
WHERE o.data_emissao BETWEEN ? AND ?
GROUP BY m.id, m.nome, m.categoria
ORDER BY vezes_utilizado DESC;

-- [RF009.04] Reporte - Performance por usuario
SELECT 
    u.nome,
    COUNT(o.id) as total_orcamentos,
    SUM(CASE WHEN o.status = 'Aprovado (Venda)' THEN 1 ELSE 0 END) as orcamentos_vendidos,
    SUM(o.valor_final) as valor_total,
    ROUND(
        (SUM(CASE WHEN o.status = 'Aprovado (Venda)' THEN 1 ELSE 0 END) / COUNT(o.id)) * 100,
        2
    ) as taxa_conversao
FROM orcamentos o
JOIN usuarios u ON o.id_usuario = u.id
WHERE o.data_emissao BETWEEN ? AND ?
GROUP BY u.id, u.nome
ORDER BY valor_total DESC;

-- [RF009.05] Reporte - Top clientes (mayor valor de ventas)
SELECT 
    c.nome, c.cpf_cnpj,
    COUNT(o.id) as total_orcamentos,
    SUM(CASE WHEN o.status = 'Aprovado (Venda)' THEN 1 ELSE 0 END) as vendas,
    SUM(o.valor_final) as valor_total_emitido,
    SUM(CASE WHEN o.status = 'Aprovado (Venda)' THEN o.valor_final ELSE 0 END) as valor_vendido
FROM orcamentos o
JOIN clientes c ON o.id_cliente = c.id
WHERE o.data_emissao BETWEEN ? AND ?
GROUP BY c.id, c.nome, c.cpf_cnpj
ORDER BY valor_vendido DESC
LIMIT 10;

-- [AUX.01] Contar orçamentos pendientes
SELECT COUNT(*) as total_pendientes 
FROM orcamentos 
WHERE status = 'Pendente';

-- [AUX.02] Obtener próxima ID para tabla
SELECT AUTO_INCREMENT 
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?;

-- [AUX.03] Verificar existencia de cliente
SELECT COUNT(*) as existe FROM clientes WHERE id = ?;

-- [AUX.04] Verificar existencia de material
SELECT COUNT(*) as existe FROM materiais WHERE id = ?;

-- [AUX.05] Obtener datos mínimos de usuario para sesión
SELECT id, nome, email FROM usuarios WHERE id = ? AND status = 'Ativo';

-- [AUX.06] Verificar duplicado de email (para registro)
SELECT COUNT(*) as duplicados FROM usuarios WHERE email = ? AND id != ?;

-- [AUX.07] Verificar duplicado de CNPJ/CPF
SELECT COUNT(*) as duplicados FROM clientes WHERE cpf_cnpj = ? AND id != ?;