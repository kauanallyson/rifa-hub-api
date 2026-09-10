# Rifa-Hub
API RESTful desenvolvida para o sistema de gestão de rifas "Rifa-Hub". Esta API é responsável por toda a lógica de negócio, incluindo a criação de rifas, venda de tickets (bilhetes), gestão de vendedores e compradores, e a realização dos sorteios.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.6
- Lombok
- Maven
- PostgreSQL

## Como Executar o Projeto

### 1. Clone o repositório

```bash
    git clone https://github.com/kauanallyson/rifa-hub-api.git
    cd rifa-hub-api
```

### 2. Configure as variáveis de ambiente

Copie o arquivo de exemplo e ajuste o que precisar. O `.env` está no `.gitignore`.

```bash
    cp .env.example .env
```

### 3. Suba a aplicação com Docker Compose

```bash
    docker compose up --build
```

### 4. Rodando a aplicação fora do Compose

Se preferir rodar a aplicação pela IDE ou pelo Maven, deixe só o banco no Docker:

```bash
    docker compose up -d db
    ./mvnw spring-boot:run
```

Nesse caso vale o `DB_URL` do `.env`, que aponta para `localhost:5432`.
