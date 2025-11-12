# Rifa-Hub
API RESTful desenvolvida para o sistema de gestão de rifas "Rifa-Hub". Esta API é responsável por toda a lógica de negócio, incluindo a criação de rifas, venda de tickets (bilhetes), gestão de vendedores e compradores, e a realização dos sorteios.

## 🚀 Tecnologias Utilizadas

- **Java 25**: Linguagem de programação principal
- **Spring Boot 3.5.6**: Framework principal para a construção da aplicação
- **Spring Data JPA / Hibernate**: Para persistência de dados e comunicação com o banco de dados
- **Spring Web**: Para a criação dos endpoints RESTful
- **Bean Validation**: Para validação dos dados de entrada (DTOs)
- **Lombok**: Para reduzir código boilerplate em entidades e DTOs
- **Maven**: Gerenciador de dependências e build do projeto
- **Banco de Dados**: Projetado para ser compatível com qualquer banco SQL relacional (ex: PostgreSQL, MySQL, H2)

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
