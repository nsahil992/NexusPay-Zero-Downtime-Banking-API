CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(100) NOT NULL UNIQUE,
    balance NUMERIC(19,2) NOT NULL
);

INSERT INTO users(account_number, balance)
VALUES
('ACC1001', 10000.00),
('ACC1002', 5000.00);