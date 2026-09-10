CREATE INDEX idx_clients_search ON clients
    USING GIN (to_tsvector('simple', name));

CREATE INDEX idx_sellers_search ON sellers
    USING GIN (to_tsvector('simple', name));

CREATE INDEX idx_raffles_search ON raffles
    USING GIN (to_tsvector('portuguese', coalesce(name, '') || ' ' || coalesce(description, '')));

DROP INDEX IF EXISTS idx_clients_name;
DROP INDEX IF EXISTS idx_sellers_name;
