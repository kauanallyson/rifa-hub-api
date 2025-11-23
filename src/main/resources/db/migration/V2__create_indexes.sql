CREATE INDEX idx_clients_name ON clients(name);
CREATE INDEX idx_sellers_name ON sellers(name);
CREATE INDEX idx_tickets_raffle_status ON tickets(raffle_id, status);