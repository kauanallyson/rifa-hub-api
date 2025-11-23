CREATE TABLE clients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE sellers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE raffles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    ticket_price NUMERIC(38, 2) NOT NULL,
    ticket_amount BIGINT NOT NULL,
    draw_date TIMESTAMP,
    status VARCHAR(255)
);

CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    number BIGINT NOT NULL,
    status VARCHAR(255),
    sale_date TIMESTAMP,

    raffle_id BIGINT NOT NULL,
    client_id BIGINT,
    seller_id BIGINT,

    CONSTRAINT fk_tickets_raffle FOREIGN KEY (raffle_id) REFERENCES raffles(id),
    CONSTRAINT fk_tickets_client FOREIGN KEY (client_id) REFERENCES clients(id),
    CONSTRAINT fk_tickets_seller FOREIGN KEY (seller_id) REFERENCES sellers(id),

    CONSTRAINT uk_tickets_number_raffle UNIQUE (number, raffle_id)
);

CREATE TABLE prizes (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    placement INTEGER NOT NULL,

    raffle_id BIGINT NOT NULL,
    winning_ticket_id BIGINT UNIQUE,

    CONSTRAINT fk_prizes_raffle FOREIGN KEY (raffle_id) REFERENCES raffles(id),
    CONSTRAINT fk_prizes_ticket FOREIGN KEY (winning_ticket_id) REFERENCES tickets(id)
);