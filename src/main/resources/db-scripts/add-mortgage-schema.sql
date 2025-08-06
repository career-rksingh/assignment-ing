CREATE TABLE mortgage_rates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    maturity_period INT NOT NULL,
    interest_rate DECIMAL(5, 2) NOT NULL,
    timestamp TIMESTAMP NOT NULL
);
