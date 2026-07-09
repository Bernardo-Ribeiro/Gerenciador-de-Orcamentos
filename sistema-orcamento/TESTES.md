# Execução de Testes - Sistema de Orçamentos

## Comandos Maven

### Executar todos os testes
```bash
mvn test
```

### Executar testes específicos
```bash
mvn test -Dtest=ClienteTest
mvn test -Dtest=CalculoServiceTest
mvn test -Dtest=FluxoCompletoIntegrationTest
```

### Executar testes com relatório de cobertura
```bash
mvn clean test jacoco:report
```

### Executar testes e gerar pacote
```bash
mvn clean package
```

## Estrutura de Testes

### Testes Unitários
- **model/**: Testes das entidades (Cliente, Orcamento, EscalaProdutiva)
- **service/**: Testes das regras de negócio (CalculoService, PdfService)

### Testes de Integração
- **dao/**: Testes de acesso a banco de dados com H2 em memória
- **integration/**: Testes de fluxos completos (E2E)

## Relatório de Cobertura

Após executar `mvn test jacoco:report`, o relatório estará em:
`sistema-orcamento/target/site/jacoco/index.html`

## Dependências de Teste

- **JUnit 5**: Framework de testes
- **Mockito**: Mocking de dependências
- **AssertJ**: Assertions fluent e legíveis
- **H2 Database**: Banco em memória para testes