CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(10) CHECK (type IN ('expenses', 'income')),
    name VARCHAR(50),
    amount MONEY
);


