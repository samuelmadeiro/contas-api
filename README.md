# Api de contas: Cooperativa de crédito

API REST que gerencia correntistas e contas, sendo elas conta corrente e conta poupança, com as suas transações e propriedades.

## Tecnologias

- Linguagem: Java 8
- Framework: Spring Boot
- Banco de dados: H2
- ORM: JPA / Hibernate (Spring Data JPA)
- Documentação da API: Swagger
- Testes: JUnit 5 e Mockito

## Como rodar

### No Windows

```bash
mvnw.cmd spring-boot:run
```

### No Mac/Linux

```bash
./mvnw spring-boot:run
```

A aplicação vai subir no `localhost:8080`.

## Banco de dados
As tabelas são criadas automaticamente durante a primeira execução, feitas pelo Spring Data JPA, o `application.properties` usa `ddl-auto=update`, não precisando rodar nada antes.

O arquivo [`schema.sql`](schema.sql), ele documenta o modelo, `correntista`, `conta` e `transação`. A conta corrente e a conta poupança ficam na mesma tabela, o que difere entre as duas é o tipo de conta e a existência do limite na conta corrente. E serve como ponto de partida para  trocar o Banco de H2 para MySQL.

## Swagger e o console do banco

Com a aplicação rodando:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI (JSON):** http://localhost:8080/v3/api-docs
- **H2 console:** http://localhost:8080/h2-console

Para acessar o console do H2:

| Campo | Valor |
|---|---|
| JDBC URL | `jdbc:h2:file:./data/contas` |
| Usuário | `sa` |
| Senha | (não há senha) |

## Endpoints

### Correntistas

| Método | Rota | Descrição | Status |
|---|---|---|---|
| POST | `/correntistas` | cadastra um correntista | 201 |
| GET | `/correntistas` | lista todos | 200 |
| GET | `/correntistas/{id}` | busca por id | 200 / 404 |
| GET | `/correntistas/{id}/contas` | lista as contas do correntista | 200 / 404 |

Corpo do cadastro:

```json
{
  "nome": "String",
  "documento": "12345678901",
  "email": "String@exemplo.com",
  "telefone": "11111111111"
}
```

O documento aceita CPF (11 dígitos) ou CNPJ (14 dígitos), apenas números.
O telefone é opcional e aceita 10 dígitos (fixo) ou 11 (celular).

### Contas

| Método | Rota | Descrição | Status |
|---|---|---|---|
| POST | `/contas` | abre uma conta | 201 |
| GET | `/contas` | lista todas | 200 |
| GET | `/contas/{id}` | busca por id | 200 / 404 |

Criação de contas:

```json
{ "idCorrentista": 1, "tipoDeConta": "CORRENTE", "limite": 500.00 }
```

```json
{ "idCorrentista": 1, "tipoDeConta": "POUPANCA" }
```

### Movimentação

| Método | Rota | Descrição | Status |
|---|---|---|---|
| POST | `/contas/{id}/depositos` | deposita e registra a transação | 201 |
| POST | `/contas/{id}/saques` | saca respeitando as regras | 201 / 400 |
| GET | `/contas/{id}/extrato` | transações da conta, da mais recente para a mais antiga | 200 |
| POST | `/contas/{id}/rendimentos?taxa=0.005` | aplica rendimento (só poupança) | 201 / 400 |
| POST | `/contas/{id}/juros?taxa=0.02` | cobra juros sobre saldo negativo (só corrente) | 201 / 400 |

Corpo do depósito e do saque:

```json
{ "valor": 200.00 }
```

Resposta:

```json
{
  "id": 1,
  "tipoDeTransacao": "DEPOSITO",
  "valor": 200.00,
  "data": "2026-09-21T18:46:23.43"
}
```

## Regra de negócio

1. Um correntista pode ter várias contas.
2. A conta corrente tem um limite: o saque máximo pode ser (saldo + limite).
3. Conta poupança não tem limite.
4. Toda movimentação, seja depósito, saque, juros ou rendimento, gera uma transação.
5. O rendimento só se aplica a contas poupança e o juros a contas correntes e negativadas, necessitando de uma taxa.
6. Qualquer operação necessita de um valor maior que 0.
7. O documento (CPF ou CNPJ) não pode ser repetido.

## Decisão de Arquitetura

Conta é uma classe abstrata, e a ContaCorrente e a ContaPoupanca herdam dela. O método sacar também é abstrato, o motivo é por causa da forma em que cada um saca; por outro lado depositar é concreto, funcionando da mesma forma nos 2. Dessa forma, o service chama apenas `conta.sacar(valor)` e não verifica que tipo de conta é.

A herança usa SINGLE_TABLE: as subclasses ficam na tabela conta, porém são separadas pelo tipo. Só existe um campo exclusivo, que é o limite, ausente na classe poupança — ele fica nulo na tabela. Se houver mais classes do tipo conta, JOINED passaria a valer a pena.

Os valores SEMPRE estarão em BigDecimal, usando 2 casas decimais, jamais utilizando o double. O ponto flutuante não representa valores como 0,10 de forma exata e o erro se acumula a cada operação, podendo ocasionar problemas de cálculo depois de várias transações.

A API não vai devolver as entidades diretamente. A conta aponta para o Correntista, que tem uma lista de contas, e serializar isso faria entrar em loop. Os DTOs de Request e Response também deixam o contrato da API independente do modelo do banco.

O saldo é encapsulado: setSaldo é protected, só as subclasses podem alterar o saldo, passando sempre pelas regras de depositar e sacar.

## Tratamento de erros

Todas as respostas de erro saem no mesmo formato, tendo um corpo formado pelo TratadorDeErros (`@RestControllerAdvice`).

A forma que sai o erro:

```json
{
  "dataHora": "2026-09-21T18:46:23.65",
  "status": 400,
  "mensagem": "Sem saldo suficiente. Valor disponível: 100.00"
}
```

| Erro | Status |
|---|---|
| Id de conta ou correntista que não existe | 404 |
| Regra de negócio violada (saldo insuficiente, documento repetido, operação no tipo errado de conta) | 400 |
| Campos inválidos no corpo | 400, com a lista de campos em `detalhes` |
| JSON mal formado ou tipo de conta inexistente | 400 |
| Parâmetro obrigatório ausente | 400 |

## Testes

```bash
mvnw.cmd test
```

### 12 testes cobrindo as regras do negócio

- **ContaCorrenteTest:** verifica se o depósito aumenta o saldo, verifica se o depósito for 0, saque usando o limite, saque maior que o limite + saldo (falha e não altera o saldo), cálculo do juros negativado e recusa se o saldo for positivo.
- **ContaPoupancaTest:** saque até o saldo chegar a 0, saque acima do saldo não alterando o saldo, cálculo do rendimento e não deixa fazer se o saldo for 0.
- **ContaServiceTest:** com o Mockito, verifica se o saldo altera com o depósito e registra a transação, e se a conta for inexistente pelo id ele retorna exceção 404.

## O que foi feito
Todos os itens obrigatórios: cada correntista podendo ter várias contas, conta corrente tendo limite e o saque até o valor saldo+limite,
Conta Poupança não permite saque além do saldo, Depósito deve aumentar o saldo e registrar a transação, saque deve reduzir o saldo e registrar a transação e os endpoints REST.

E todos os itens diferenciais foram completos: Conta poupança com um endpoint para aplicar rendimento mensal, informando a taxa, registrando a transação e atualizando o saldo. Conta corrente com um endpoint aplicando juros no saldo negativo, registrando a transação e atualizando o saldo. Testes unitários cobrindo as regras de saque, depósito e cálculo de rendimento/juros. A Documentação da API com Swagger / OpenAPI e Tratamento de erros padronizado (mensagens claras e status HTTP adequados).

## O que ficou de fora
- Edição e remoção de correntistas e contas, o enunciado só pediu cadastro e consulta.
- Autenticação, não faz parte do escopo do projeto.
- MySQL, o projeto roda só com H2.


## O que eu faria se tivesse mais tempo
1. Transferência entre contas, a operação mais utilizada no dia a dia operacional.
2. Uma paginação dos GET, extrato e listagem, além de filtros por período. Atualmente devolvem tudo de uma vez.
3. Trocar o H2 para MySQL, em um profile separado.
