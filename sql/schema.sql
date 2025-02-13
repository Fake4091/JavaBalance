CREATE TABLE budgetsheet (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    budget_sheet_id INT REFERENCES budgetsheet(id) ON DELETE CASCADE,
    type VARCHAR(10) CHECK (type IN ('expenses', 'income')),
    name VARCHAR(50),
    amount MONEY,
    priority VARCHAR(10) CHECK (priority IN ('Need', 'Want', 'Savings')) DEFAULT 'Need'
);


DELETE FROM transactions;


DELETE FROM budgetsheet;







