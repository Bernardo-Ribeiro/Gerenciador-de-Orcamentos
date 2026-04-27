# Documento de Requisitos

## Sistema de Orçamentação para Gráfica

**Equipe Responsável pela Elaboração**

- Facundo Mederos
- Felipe de Avila
- Bernardo Ribeiro
- Mauricio Carvalho
- Maximiliano Olagorta

**Público-alvo**: desenvolvedores, testadores, professores orientadores e demais stakeholders envolvidos no projeto.

## Sumário

1. [Introdução](#introdução)
2. [Descrição Geral do Sistema](#capítulo-1-descrição-geral-do-sistema)
3. [Requisitos Funcionais](#capítulo-2-requisitos-funcionais-casos-de-uso)
4. [Requisitos Não Funcionais](#capítulo-3-requisitos-não-funcionais)
5. [Regras de Negócio](#capítulo-4-regras-de-negócio)
6. [Descrição da Interface com o Usuário](#capítulo-5-descrição-da-interface-com-o-usuário)

## Introdução

Este documento especifica o Sistema de Orçamentação para Gráfica, fornecendo aos desenvolvedores as informações necessárias para o projeto, implementação, testes e homologação do sistema. O objetivo principal é automatizar a geração de orçamentos, solucionando o gargalo de um processo manual que consome cerca de 30 minutos e apresenta alta incidência de erros de cálculo.

### Visão geral deste documento

Esta introdução define os objetivos e convenções adotadas. As demais seções estão organizadas da seguinte forma:

- Capítulo 1 - Descrição geral do sistema: apresenta o escopo, análise de mercado e o perfil dos usuários.
- Capítulo 2 - Requisitos funcionais: detalha os casos de uso, fluxos de eventos e atores.
- Capítulo 3 - Requisitos não funcionais: especifica critérios de usabilidade, desempenho e segurança.
- Capítulo 4 - Regras de negócio: descreve as políticas corporativas, lógicas de cálculo e restrições comerciais.
- Capítulo 5 - Descrição da interface: apresenta os protótipos de tela do sistema.

### Convenções, termos e abreviações

- Identificação dos requisitos: referenciados pelo nome da subseção seguido do identificador, por exemplo, [Cadastro.RF001] ou [Segurança.NF004].
- Prioridades:
	- Essencial: requisito imprescindível para o funcionamento.
	- Importante: requisito que deve ser implementado, mas não impede o uso básico.
	- Desejável: requisito que pode ser deixado para versões posteriores.

## Capítulo 1 - Descrição Geral do Sistema

O sistema será uma aplicação desktop desenvolvida para automatizar a precificação e geração de documentos comerciais. O propósito central é eliminar a complexidade do cálculo manual de insumos, garantindo precisão técnica e agilidade no atendimento ao cliente.

### 1.1 Abrangência e sistemas relacionados

O sistema é independente e autocontido. Suas funcionalidades principais abrangem a gestão de cadastros, cálculos automatizados com regras de negócio e geração de PDF. Diferente de soluções de mercado que operam como ERPs complexos, este projeto foca exclusivamente na agilidade da orçamentação.

| Funcionalidade | Sistema Proposto | Calcme | GestorGraf | Logic Print | SisGraf |
| --- | --- | --- | --- | --- | --- |
| Automação | Sim | Sim | Sim | Sim | Sim |
| Geração de PDF | Sim | Sim | Sim | Sim | Sim |
| Gestão de Estoque | Não | Sim | Sim | Sim | Sim |
| WhatsApp | Não | Não | Sim | Sim | Não |
| Controle Financeiro | Não | Sim | Sim | Não | Sim |
| Foco em Design | Não | Não | Não | Sim | - |

### 1.2 Descrição dos usuários

O sistema atende a um perfil de usuário específico:

1. Funcionário responsável pelos orçamentos
	 - Perfil: profissional operacional que necessita de rapidez no cálculo de materiais como lona, adesivo e papel.
	 - Desafios: baixa produtividade devido ao cálculo manual e dificuldade em recuperar dados históricos.
	 - Responsabilidades: cadastrar clientes, configurar custos de materiais e emitir orçamentos finais.

## Capítulo 2 - Requisitos Funcionais (Casos de Uso)

Nesta seção, são apresentados os requisitos funcionais do sistema, agrupados por categorias para facilitar a visualização do escopo operacional.

### 2.1 Gestão de Acesso e Segurança

Esta subseção agrupa as funcionalidades relacionadas ao controle de entrada e saída de usuários no sistema desktop.

#### [RF001] Efetuar Login e Logout

- Ator: funcionário responsável pelos orçamentos.
- Prioridade: essencial.
- Entradas e pré-condições: nome de usuário e senha previamente cadastrados; sistema inicializado.
- Saídas e pós-condições: acesso liberado ao menu principal ou encerramento da sessão com segurança.
- Fluxo principal:
	1. O usuário insere suas credenciais na tela de autenticação.
	2. O sistema valida os dados em um banco de dados criptografado.
	3. O sistema concede acesso e exibe as funcionalidades de acordo com o perfil.
	4. Para o logout, o usuário seleciona a opção "Sair", e o sistema retorna à tela de login.

### 2.2 Gestão de Cadastros e Dados

Funcionalidades destinadas à manutenção da base de dados necessária para a operação da gráfica.

#### [RF002] Cadastrar Clientes

- Ator: funcionário responsável pelos orçamentos.
- Prioridade: essencial.
- Entradas e pré-condições: dados cadastrais como Nome da Empresa, CNPJ/CPF, Nome do Responsável, Telefone e E-mail.
- Saídas e pós-condições: cliente registrado e disponível para seleção em novos orçamentos.
- Fluxo principal:
	1. O usuário acessa o módulo de "Clientes" e seleciona "Novo Cadastro".
	2. O sistema apresenta o formulário; o usuário preenche os campos obrigatórios.
	3. O sistema valida o formato do CNPJ/CPF e salva o registro.

#### [RF003] Cadastrar Serviços e Materiais

- Ator: funcionário responsável pelos orçamentos.
- Prioridade: essencial.
- Entradas e pré-condições: tipo de material, tamanho padrão e definição de custos.
- Saídas e pós-condições: serviço ou material categorizado e disponível para cálculo.
- Fluxo principal:
	1. O usuário define se o item é de "Comunicação Visual" ou "Materiais Impressos".
	2. Insere o custo base, por exemplo custo por metro quadrado ou por unidade.
	3. Regra de escala: o usuário configura se o preço deve variar conforme a quantidade produzida, usando tabelas progressivas ou regressivas.

### 2.3 Geração e Processamento de Orçamentos

Subseção que descreve o núcleo de inteligência do sistema: a automação da precificação.

#### [RF004] Elaborar e Calcular Orçamento Automaticamente

- Ator: funcionário responsável pelos orçamentos.
- Prioridade: essencial.
- Entradas e pré-condições: cliente selecionado, material escolhido e dimensões inseridas.
- Saídas e pós-condições: exibição do valor bruto e valor final calculado.
- Fluxo principal:
	1. O sistema busca o custo unitário do material cadastrado no [RF003].
	2. O sistema calcula a área total ou tiragem e aplica o valor base.
	3. Variação por quantidade: o sistema ajusta o custo unitário automaticamente caso a quantidade atinja os gatilhos configurados.
	4. O sistema soma o tempo de produção estimado ao custo dos materiais.

#### [RF005] Aplicar Margem de Lucro Estimada e Descontos

- Ator: funcionário responsável pelos orçamentos.
- Prioridade: essencial.
- Fluxo principal:
	1. O sistema sugere uma margem de lucro padrão baseada no tipo de serviço, por exemplo 50% para Comunicação Visual.
	2. O usuário pode ajustar manualmente a porcentagem de lucro para o pedido específico.
	3. Caso necessário, o usuário insere um valor de desconto em reais ou porcentagem.
	4. O sistema atualiza o valor final do PDF em tempo real.

#### [RF006] Gerar Orçamento em PDF

- Ator: funcionário responsável pelos orçamentos.
- Prioridade: essencial.
- Saídas e pós-condições: arquivo PDF gerado e salvo no diretório local.
- Fluxo principal:
	1. O sistema consolida dados do cliente, descrição dos serviços, valores unitários, totais e condições de pagamento.
	2. Gera o arquivo em formato PDF com o logotipo da gráfica para envio ao cliente.

#### [RF007] Atualizar Status do Orçamento

- Ator: funcionário responsável pelos orçamentos.
- Prioridade: essencial.
- Entradas e pré-condições: orçamento previamente gerado e salvo no banco de dados.
- Saídas e pós-condições: status do orçamento atualizado, impactando os relatórios gerenciais.
- Fluxo principal:
	1. O usuário acessa o módulo de "Histórico de Orçamentos".
	2. O usuário localiza o orçamento desejado por cliente ou data e seleciona a opção "Atualizar Status".
	3. O sistema apresenta as opções "Pendente", "Aprovado (Venda)" e "Cancelado/Reprovado".
	4. O usuário seleciona o novo status e confirma a alteração.
	5. O sistema salva a atualização, permitindo que o módulo de relatórios contabilize a conversão da venda.

### 2.4 Histórico e Relatórios Gerenciais

Funcionalidades para acompanhamento de vendas e recuperação de dados.

#### [RF008] Gerenciar Histórico de Orçamentos

- Ator: sistema.
- Prioridade: essencial.
- Descrição: armazena digitalmente cada orçamento finalizado para consultas futuras. Permite buscar por Nome/ID do cliente para visualizar orçamentos antigos, facilitando a reemissão de pedidos.

#### [RF009] Gerar Relatórios Gerenciais Consolidados

- Ator: funcionário responsável pelos orçamentos.
- Prioridade: essencial.
- Saídas e pós-condições: relatório exibido em tela ou exportável com métricas de desempenho.
- Fluxo principal:
	1. O usuário define o período desejado, por exemplo mensal.
	2. O sistema processa o banco de dados e exibe o valor total de orçamentos emitidos.
	3. O relatório detalha o faturamento bruto total, os materiais mais utilizados e a média de conversão de orçamentos em vendas.

## Capítulo 3 - Requisitos Não Funcionais

Este capítulo descreve os requisitos não funcionais do sistema, que representam as características de qualidade, restrições técnicas e critérios de desempenho necessários para garantir a eficácia da solução.

### 3.1 Usabilidade

#### [NF001] Interface Simples e Intuitiva

- Descrição: o sistema deve apresentar uma interface baseada no conceito de Clean Design, priorizando a clareza visual e a redução de elementos desnecessários.
- Detalhamento: a intuitividade é definida pela capacidade de um novo funcionário realizar um orçamento completo em menos de 5 minutos sem treinamento prévio exaustivo.
- Requisitos específicos:
	1. Utilizar navegação linear no fluxo cadastro → cálculo → PDF.
	2. Implementar preenchimento automático para busca de clientes e materiais já cadastrados.
	3. Oferecer feedbacks visuais imediatos com mensagens de confirmação e erro claras.
- Prioridade: essencial.
- Caso(s) de uso associado(s): [RF002], [RF004].

#### [NF002] Facilidade na Atualização de Parâmetros

- Descrição: o sistema deve permitir que o administrador atualize os custos de insumos e as margens de lucro sem intervenção técnica no código-fonte.
- Detalhamento: a interface de configuração deve ser centralizada, permitindo que alterações nos valores por metro quadrado reflitam instantaneamente em todos os novos cálculos de orçamento.
- Prioridade: essencial.

### 3.2 Desempenho

#### [NF003] Resposta Rápida nos Cálculos e Geração de PDF

- Descrição: o processamento matemático de precificação e a renderização do arquivo PDF não devem exceder o tempo de 2 segundos.
- Detalhamento: considerando que o sistema é uma aplicação desktop independente, o tempo de resposta deve ser quase instantâneo para evitar filas no atendimento ao cliente da gráfica.
- Prioridade: essencial.

### 3.3 Segurança

#### [NF004] Mecanismo de Autenticação e Criptografia

- Descrição: o acesso ao sistema é restrito e deve ser protegido por camadas de segurança técnica.
- Detalhamento:
	1. Criptografia de senhas: as senhas dos usuários não devem ser armazenadas em texto plano. O sistema deve utilizar algoritmos de hashing robustos, como BCrypt ou SHA-256.
	2. Sessão local: o sistema deve encerrar a sessão local sempre que a aplicação for fechada, exigindo nova autenticação no próximo acesso.
- Prioridade: essencial.
- Caso(s) de uso associado(s): [RF001].

#### [NF005] Integridade e Armazenamento dos Dados

- Descrição: garantir que os dados históricos não sejam corrompidos ou alterados após a finalização da venda.
- Detalhamento: orçamentos com status "Aprovado" devem tornar-se "Somente Leitura" para preservar a auditoria dos valores cobrados. O banco de dados deve possuir rotinas de integridade referencial para evitar a exclusão de clientes que possuam orçamentos vinculados.
- Prioridade: essencial.

### 3.4 Hardware e Software

#### [NF006] Requisitos de Ambiente

- Descrição: especificação do ambiente necessário para execução.
- Detalhamento: sendo uma aplicação desktop desenvolvida, o sistema deve ser compatível com Windows 10 ou superior, exigindo um mínimo de 4 GB de RAM para garantir a fluidez da interface gráfica e do motor de geração de PDFs.
- Prioridade: importante.

## Capítulo 4 - Regras de Negócio

Esta seção descreve as políticas corporativas, lógicas de cálculo e restrições comerciais que o sistema deve incorporar e validar automaticamente para garantir que a gráfica opere com lucro e consistência.

### 4.1 Estrutura de Serviços e Custos

#### [RN001] Categorização de Serviços e Insumos

- Descrição: o sistema deve tratar os serviços em duas categorias principais, com lógicas de cálculo distintas.
	1. Comunicação Visual: o custo base é calculado pela área multiplicada pelo custo do metro quadrado do insumo.
	2. Materiais Impressos: o custo base é calculado por tiragem do papel utilizado.

#### [RN002] Composição do Custo Total de Produção

- Descrição: o custo total não é apenas o valor do material. O sistema deve somar as variáveis abaixo para encontrar o custo base.
	1. Custo do insumo: valor pago ao fornecedor pela matéria-prima.
	2. Custo de produção: valor estimado da hora/máquina ou hora/operador necessária para concluir o serviço, opcional e configurável.
- Fórmula lógica: CustoBase = CustoInsumo + CustoProducao.

### 4.2 Políticas de Precificação e Variação

#### [RN003] Regra de Variação por Quantidade (Escala Produtiva)

- Descrição: o valor de venda unitário deve diminuir à medida que a quantidade solicitada aumenta, incentivando compras em maior volume.
- Configuração da variação: essa variação será configurável pelo usuário administrador através de tabelas de regressão cadastradas no sistema.
- Exemplo prático: o administrador configura que impressões de 1 a 50 unidades não têm desconto no custo. De 51 a 500 unidades, o custo base do papel sofre redução de 10%. Acima de 501, redução de 15%. O sistema deve ler essa tabela antes de aplicar a margem de lucro.

#### [RN004] Aplicação da Margem de Lucro Estimada

- Descrição: a margem de lucro não é um valor fixo no orçamento, mas sim um percentual configurável aplicado sobre o custo base total.
- Detalhamento da aplicação:
	1. O administrador pode definir margens padrão por categoria, por exemplo 40% sobre Impressos e 60% sobre Comunicação Visual.
	2. Fórmula lógica: Valor de Venda = CustoBase + (CustoBase × Margem / 100).
	3. O funcionário pode, no momento do orçamento, alterar a margem padrão para mais ou para menos, dependendo da negociação com o cliente específico, mas o sistema sempre exibirá qual é a porcentagem aplicada.

### 4.3 Restrições Comerciais

#### [RN005] Limite de Desconto Comercial

- Descrição: o sistema não deve permitir que o funcionário aplique um desconto comercial que torne o valor final menor que o custo total de produção.
- Detalhamento: se o desconto inserido ultrapassar a margem de lucro configurada, o sistema deve bloquear a emissão do PDF e exibir um aviso de "Operação Inválida: Desconto resulta em margem negativa".

#### [RN006] Validade do Orçamento

- Descrição: todos os orçamentos gerados em PDF devem conter, obrigatoriamente, uma cláusula de validade do documento.
- Detalhamento: devido à variação do preço das matérias-primas no mercado, o sistema configurará automaticamente o campo "Válido por" para 15 dias corridos a partir da data de emissão.

## Capítulo 5 - Descrição da Interface com o Usuário

Este capítulo apresenta os protótipos das interfaces gráficas do sistema, detalhando a disposição dos elementos e as interações previstas para atender aos requisitos funcionais descritos no Capítulo 2.

### 5.1 Interface de Autenticação (Login)

- Descrição: tela inicial de acesso ao sistema, projetada para ser minimalista e segura.
- Elementos: campos para Usuário, Senha com máscara de caracteres e botão Entrar.
- Casos de uso relacionados: [RF001].

Figura 5.1: Protótipo da Tela de Login.

### 5.2 Dashboard Principal

- Descrição: central de navegação do sistema, onde o usuário tem acesso rápido aos módulos de cadastro, orçamentação e relatórios.
- Elementos: menu lateral ou superior com ícones intuitivos e área central com atalhos para os serviços mais utilizados.
- Críticas da interface: o sistema deve destacar alertas caso existam orçamentos pendentes de atualização de status.

Figura 5.2: Protótipo da Tela de Dashboard Principal.

### 5.3 Tela de Cadastro de Clientes

- Descrição: formulário estruturado para a inserção de dados de novos clientes ou edição de registros existentes.
- Elementos: campos de texto para Nome, CNPJ/CPF com máscara, E-mail e Telefone. Botões de Salvar, Limpar e Excluir.
- Casos de uso relacionados: [RF002].

Figura 5.3: Protótipo da Tela de Cadastro de Clientes.

### 5.4 Módulo de Elaboração de Orçamento (Calculadora)

- Descrição: interface principal de operação, onde o cálculo automatizado ocorre em tempo real.
- Elementos:
	1. Seleção de cliente via autocomplete.
	2. Seleção de material e serviço.
	3. Campos de entrada para Largura, Altura e Quantidade.
	4. Painel de exibição do Valor Bruto, Margem Aplicada e Valor Final.
	5. Botão Gerar PDF.
- Casos de uso relacionados: [RF004], [RF005], [RF006].

Figura 5.4: Protótipo da Interface de Elaboração de Orçamentos.

### 5.5 Consulta de Histórico e Relatórios

- Descrição: tela de busca e visualização de dados consolidados.
- Elementos: tabela com filtros por data e cliente; botões para atualização de status; gráficos simples de desempenho de vendas por período.
- Casos de uso relacionados: [RF007], [RF008].

Figura 5.5: Protótipo da Interface de Relatórios Gerenciais.

### 5.6 Painel de Controle do Administrador

- Descrição: módulo de uso restrito à gerência para parametrização centralizada do sistema, garantindo que as regras de negócio, escalas produtivas e custos estejam sempre atualizados com a realidade do mercado, sem necessidade de manutenção no código-fonte.
- Subtelas e funcionalidades:
	1. Gestão de usuários: cadastro, edição e bloqueio de credenciais dos funcionários autorizados a emitir orçamentos.
	2. Tabela de custos de insumos: interface para atualizar o custo base de compra de materiais.
	3. Configuração de escala produtiva: grade de configuração editável de descontos progressivos por volume.
	4. Margens de lucro padrão: configuração das porcentagens automáticas por categoria de serviço.
	5. Configurações do documento PDF: upload de logomarca e edição de dados do rodapé.
- Casos de uso relacionados: [RF001], [RF003].
- Regras de negócio relacionadas: [RN001], [RN002], [RN003], [RN004].

Figura 5.6: Protótipo da Interface do Painel de Controle do Administrador (com abas de navegação para usuários, custos e escala produtiva).

