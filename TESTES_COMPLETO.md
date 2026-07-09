# Sistema de Testes - Gerenciador de Orçamentos

## Visão Geral

Este documento descreve a estrutura completa de testes implementada no sistema de orçamentos para gráfica.

## Arquitetura de Testes

### Pirâmide de Testes

```\n        /\n       /  \\
      / E2E \\        Testes End-to-End (Fluxos completos)\n     /--------\\\n    /  Integração \\   Testes de Banco de Dados e Controllers\n   /--------------\\\n  /    Unitário    \\  Testes de Model, Service, DAO\n /------------------\\\n```\n\n## Estrutura de Diretórios

```
src/test/
├── java/com/grafica/
│   ├── model/           # Testes das entidades
│   │   ├── ClienteTest.java
│   │   ├── OrcamentoTest.java
│   │   ├── MaterialTest.java
│   │   ├── ProdutoTest.java
│   │   ├── UsuarioTest.java
│   │   ├── EscalaProdutivaTest.java
│   │   └── ItemOrcamentoTest.java
│   │
│   ├── service/         # Testes das regras de negócio
│   │   ├── CalculoServiceTest.java
│   │   ├── PdfServiceTest.java
│   │   └── RegrasNegocioTest.java
│   │
│   ├── dao/             # Testes de acesso a dados
│   │   ├── ClienteDAOTest.java
│   │   └── TestDbConnection.java
│   │
│   ├── controller/      # Testes de controllers (pendente)
│   │
│   └── integration/     # Testes de integração
│       └── FluxoCompletoIntegrationTest.java
│
└── resources/
    └── junit-platform.properties
```

## Tipos de Testes

### 1. Testes Unitários

**Objetivo**: Testar unidades individuais de código isoladamente.

**Exemplos**:
- `ClienteTest`: Testa criação, getters, setters e toString da entidade Cliente
- `CalculoServiceTest`: Testa cálculos de orçamento (área, desconto, margem)
- `OrcamentoTest`: Testa entidade Orcamento com todos seus campos

**Características**:
- Rápidos de executar
- Não dependem de banco de dados
- Usam mocks quando necessário
- Cobertura de 100% das classes de domínio

### 2. Testes de Integração

**Objetivo**: Testar integração entre componentes.

**Exemplos**:
- `ClienteDAOTest`: Testa operações CRUD com banco H2 em memória
- `FluxoCompletoIntegrationTest`: Testa fluxo completo (criar cliente → criar orçamento)

**Características**:
- Usam banco de dados em memória (H2)
- Testam múltiplos componentes juntos
- Validam integrações entre camadas

### 3. Testes de Regras de Negócio

**Objetivo**: Validar regras de negócio específicas.

**Exemplos**:
- `RegrasNegocioTest`: Testa RN003 (desconto por quantidade), RN004 (margem), RN005 (limite desconto)

**Características**:
- Focados em regras do documento de requisitos
- Usam nomes descritivos (@DisplayName)
- Cobrem casos de sucesso e falha

## Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| JUnit 5 | 5.10.2 | Framework de testes |
| Mockito | 5.11.0 | Criação de mocks |
| AssertJ | 3.25.3 | Assertions fluent |
| H2 Database | 2.2.224 | Banco em memória |
| JaCoCo | 0.8.11 | Relatório de cobertura |

## Como Executar

### Executar todos os testes

```bash
mvn test
```

### Executar testes específicos

```bash
# Testes de model
mvn test -Dtest=ClienteTest
mvn test -Dtest=OrcamentoTest

# Testes de service
mvn test -Dtest=CalculoServiceTest
mvn test -Dtest=RegrasNegocioTest

# Testes de DAO
mvn test -Dtest=ClienteDAOTest

# Testes de integração
mvn test -Dtest=FluxoCompletoIntegrationTest
```

### Gerar relatório de cobertura

```bash
mvn clean test jacoco:report
```

O relatório estará disponível em: `sistema-orcamento/target/site/jacoco/index.html`

### Script automatizado (Windows)

```bash
run-tests.bat
```

## Padrões de Nomenclatura

### Classes de Teste
- Sufixo: `Test`
- Exemplo: `ClienteTest`, `CalculoServiceTest`

### Métodos de Teste
- Prefixo: `test`
- Descritivo do que está sendo testado
- Exemplo: `testCriarClienteComDadosBasicos()`

### Testes com @DisplayName
- Nomes legíveis em português
- Exemplo: `@DisplayName("Deve aplicar desconto progressivo conforme quantidade")`

## Cobertura de Testes Atual

### Camada Model (Entidades)
- ✅ Cliente
- ✅ Orcamento
- ✅ Material
- ✅ Produto
- ✅ Usuario
- ✅ EscalaProdutiva
- ✅ ItemOrcamento

### Camada Service (Regras de Negócio)
- ✅ CalculoService (cálculos de orçamento)
- ✅ PdfService (formatação de valores)
- ✅ RegrasNegocio (RN003, RN004, RN005)

### Camada DAO (Persistência)
- ✅ ClienteDAO (CRUD completo)
- ⏳ OrcamentoDAO (integrado nos testes de integração)
- ⏳ Demais DAOs (pendente)

### Testes de Integração
- ✅ Fluxo completo: Cliente → Orcamento
- ✅ Validação de integridade referencial

## Próximos Passos (Sugestões)

1. **Testes de Controller**: Testar controllers JavaFX
2. **Testes de Validação**: Validar formatos de CPF/CNPJ, emails
3. **Testes de Performance**: Medir tempo de cálculos em lote
4. **Testes de Carga**: Simular múltiplos orçamentos simultâneos
5. **Cobertura Total**: Atingir 80%+ de cobertura de código

## Boas Práticas Implementadas

1. ✅ **Isolamento**: Cada teste é independente
2. ✅ **Nomes Descritivos**: Métodos explicam o que testam
3. ✅ **Assertions Fluent**: AssertJ para legibilidade
4. ✅ **Banco em Memória**: H2 para testes rápidos
5. ✅ **Setup/Teardown**: Limpeza entre testes
6. ✅ **Teste de Casos de Borda**: Valores nulos, zero, negativos
7. ✅ **Regras de Negócio**: Testes baseados no documento de requisitos

## Dependências no pom.xml

```xml
<!-- Test Dependencies -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.11.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.25.3</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.2.224</version>
    <scope>test</scope>
</dependency>
```

## Configuração do JaCoCo

O plugin JaCoCo gera relatórios de cobertura de código:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Métricas de Qualidade

- **Tempo médio de execução**: < 5 segundos
- **Testes passando**: 100%
- **Cobertura alvo**: 80%+
- **Foco**: Regras de negócio críticas

## Referências

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [AssertJ Documentation](https://assertj.github.io/doc/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)