-- V 1.0 Create item and order_item tables
CREATE TABLE items
(
    id              SERIAL PRIMARY KEY,
    model           VARCHAR(255)  NOT NULL,
    size            VARCHAR(255)  NOT NULL,
    available_count INT DEFAULT 0 NOT NULL
);

CREATE TABLE order_item
(
    id             SERIAL NOT NULL,
    order_uid      uuid   NOT NULL,
    order_item_uid uuid   NOT NULL,
    item_id        INT
        CONSTRAINT fk_order_item_item_id REFERENCES items (id),
    canceled       BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_order_item_item_id ON order_item (item_id);
CREATE INDEX idx_order_item_order_uid ON order_item (order_uid);
CREATE UNIQUE INDEX idx_order_item_item_uid ON order_item (order_item_uid);