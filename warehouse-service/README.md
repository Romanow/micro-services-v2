# Warehouse Service

## Описание API
* `GET /api/v1/warehouse/{orderItemUid}` – информация о вещах на складе;
* `POST /api/v1/warehouse` – запрос на получение вещи со склада по новому заказу;
* `POST /api/v1/warehouse/{orderItemUid}/warranty` – запрос решения по гарантии;
* `DELETE /api/v1/warehouse/{orderItemUid}` – вернуть заказ на склад.

[https://warehouse-service-app.herokuapp.com/api-docs](https://warehouse-service-app.herokuapp.com/api-docs)

## Структура таблиц
```postgresql
CREATE TABLE items
(
    id              SERIAL CONSTRAINT items_pkey PRIMARY KEY,
    available_count INTEGER      NOT NULL,
    model           VARCHAR(255) NOT NULL,
    size            VARCHAR(255) NOT NULL
);

CREATE TABLE order_item
(
    id             SERIAL CONSTRAINT order_item_pkey PRIMARY KEY,
    canceled       BOOLEAN,
    order_item_uid UUID NOT NULL CONSTRAINT idx_order_item_order_item_uid UNIQUE,
    order_uid      UUID NOT NULL,
    item_id        INTEGER CONSTRAINT fk_order_item_item_id REFERENCES items
);
```