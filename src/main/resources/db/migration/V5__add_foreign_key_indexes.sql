CREATE INDEX idx_tickets_client ON tickets(client_id);
CREATE INDEX idx_tickets_seller ON tickets(seller_id);
CREATE INDEX idx_prizes_raffle ON prizes(raffle_id);
CREATE INDEX idx_raffles_status ON raffles(status);
