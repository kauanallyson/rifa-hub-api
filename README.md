# Rifa-Hub—API

API RESTful desenvolvida para o sistema de gestão de rifas "Rifa-Hub". Esta API é responsável por toda a lógica de negócio, incluindo a criação de rifas, venda de pontos (bilhetes), gestão de vendedores e compradores, e a realização dos sorteios.

## 🚀 Tecnologias Utilizadas

- **Java 17+**: Linguagem de programação principal
- **Spring Boot 3.x**: Framework principal para a construção da aplicação
- **Spring Data JPA / Hibernate**: Para persistência de dados e comunicação com o banco de dados
- **Spring Web**: Para a criação dos endpoints RESTful
- **Bean Validation**: Para validação dos dados de entrada (DTOs)
- **Lombok**: Para reduzir código boilerplate em entidades e DTOs
- **Maven**: Gerenciador de dependências e build do projeto
- **Banco de Dados**: Projetado para ser compatível com qualquer banco SQL relacional (ex: PostgreSQL, MySQL, H2)

## 📋 Pré-requisitos

Antes de começar, você precisará ter as seguintes ferramentas instaladas na sua máquina:

- JDK 17 ou superior
- Maven 3.8 ou superior
- Um SGBD da sua preferência (ex: PostgreSQL)

## ⚙️ Como Executar o Projeto

### 1. Clone o repositório

```bash
    git clone https://github.com/kauanallyson/rifa-hub-api.git
    cd rifa-hub-api
```

### 2. Configure o Banco de Dados

Abra o arquivo `src/main/resources/application.properties` e configure as credenciais de acesso ao seu banco de dados.

**Exemplo para PostgreSQL:**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/rifa_hub_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
```

### 3. Compile e Execute a Aplicação

Use o Maven para compilar e iniciar o servidor Spring Boot:

```bash
    mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

## 📚 Documentação da API

### 1. Rifas (`/api/rifas`)

Recurso principal para o gerenciamento de rifas.

#### `POST /api/rifas`
- **Descrição**: Cria uma rifa, incluindo os seus prêmios e gerando todos os seus pontos
- **Corpo da Requisição**: `RifaCreateDTO`
- **Resposta**: `201 Created` com `RifaResponseDTO` no corpo

#### `GET /api/rifas`
- **Descrição**: Retorna uma lista paginada de todas as rifas
- **Parâmetros de Query**: `page`, `size`, `sort`
- **Resposta**: `200 OK` com um objeto `Page<RifaResponseDTO>`

#### `GET /api/rifas/buscar-por-status`
- **Descrição**: Retorna uma lista paginada de rifas filtradas por um status específico
- **Parâmetros de Query**:
    - `status` (valores: `ABERTA`, `FINALIZADA`, `CANCELADA`)
    - `page`, `size`, `sort`
- **Resposta**: `200 OK` com `Page<RifaResponseDTO>`

#### `GET /api/rifas/{id}`
- **Descrição**: Busca uma rifa específica pelo seu ID
- **Resposta**: `200 OK` com `RifaResponseDTO`

#### `PUT /api/rifas/{id}`
- **Descrição**: Atualiza os dados de uma rifa existente (apenas se estiver com status ABERTA)
- **Corpo da Requisição**: `RifaUpdateDTO`
- **Resposta**: `200 OK` com o `RifaResponseDTO` atualizado

#### `DELETE /api/rifas/{id}`
- **Descrição**: Realiza o "soft delete" de uma rifa, alterando o seu status para CANCELADA. Apenas rifas ABERTAs e sem pontos vendidos podem ser canceladas
- **Resposta**: `204 No Content`

#### `POST /api/rifas/{id}/realizar-sorteio`
- **Descrição**: Executa o sorteio de uma rifa ABERTA, atribui os vencedores e altera o seu status para FINALIZADA
- **Resposta**: `200 OK` com o `RifaResponseDTO` contendo os resultados

### 2. Prêmios (Aninhado em Rifas)

#### `POST /api/rifas/{rifaId}/premios`
- **Descrição**: Adiciona um novo prêmio a uma rifa existente
- **Corpo da Requisição**: `PremioCreateDTO`
- **Resposta**: `201 Created` com `PremioResponseDTO`

#### `DELETE /api/rifas/{rifaId}/premios/{premioId}`
- **Descrição**: Remove um prêmio de uma rifa existente
- **Resposta**: `204 No Content`

### 3. Pontos (Aninhado em Rifas)

#### `GET /api/rifas/{rifaId}/pontos`
- **Descrição**: Lista todos os pontos de uma rifa específica, de forma paginada
- **Resposta**: `200 OK` com `Page<PontoResponseDTO>`

#### `POST /api/rifas/{rifaId}/pontos/{numeroPonto}/vender`
- **Descrição**: Efetiva a venda de um ponto específico para um comprador, realizada por um vendedor
- **Corpo da Requisição**: `PontoVendaRequestDTO`
- **Resposta**: `200 OK` com o `PontoResponseDTO` atualizado

### 4. Vendedores (`/api/vendedores`)

Recurso para o gerenciamento de vendedores.

- **`POST /api/vendedores`**: Cria um vendedor
- **`GET /api/vendedores`**: Lista todos os vendedores ativos de forma paginada
- **`GET /api/vendedores/{id}`**: Busca um vendedor ativo pelo ID
- **`PUT /api/vendedores/{id}`**: Atualiza um vendedor existente
- **`DELETE /api/vendedores/{id}`**: Inativa um vendedor (soft delete)

### 5. Compradores (`/api/compradores`)

Recurso para o gerenciamento de compradores.

- **`POST /api/compradores`**: Cria um comprador
- **`GET /api/compradores`**: Lista todos os compradores ativos de forma paginada
- **`GET /api/compradores/{id}`**: Busca um comprador ativo pelo ID
- **`PUT /api/compradores/{id}`**: Atualiza um comprador existente
- **`DELETE /api/compradores/{id}`**: Inativa um comprador (soft delete)

---

## 📝 Licença

Este projeto é licenciado sob os termos da licença MIT. Consulte o arquivo LICENSE na raiz do repositório para mais detalhes.
