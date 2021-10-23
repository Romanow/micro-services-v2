-- V 1.0 Create warranty table
CREATE TABLE warranty
(
    id            SERIAL PRIMARY KEY,
    item_uid      uuid         NOT NULL,
    warranty_date TIMESTAMP    NOT NULL,
    status        VARCHAR(255) NOT NULL,
    comment       VARCHAR(1024)
);

CREATE UNIQUE INDEX idx_warranty_item_uid ON warranty (item_uid);