CREATE TABLE transaction
(
    id          SERIAL PRIMARY KEY,
    amount      DOUBLE PRECISION NOT NULL,
    description VARCHAR(256)     NOT NULL,
    category    VARCHAR(256)     NOT NULL,
    user_id     BIGINT           NOT NULL
);