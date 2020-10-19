# Order Service

## Описание API
* `GET /api/v1/orders/{userUid}` – ;
* `GET /api/v1/orders/{userUid}/{orderUid}` – ;
* `POST /api/v1/orders/{userUid}` – ;
* `POST /api/v1/orders/{orderUid}/warranty` – ;
* `DELETE /api/v1/orders/{orderUid}` – .

[https://order-service-app.herokuapp.com/api-docs](https://order-service-app.herokuapp.com/api-docs)

## Структура таблиц
```postgresql
CREATE TABLE orders
(
    id         SERIAL CONSTRAINT orders_pkey PRIMARY KEY,
    item_uid   UUID         NOT NULL,
    order_date TIMESTAMP    NOT NULL,
    order_uid  UUID         NOT NULL CONSTRAINT idx_orders_order_uid UNIQUE,
    status     VARCHAR(255) NOT NULL,
    user_uid   UUID         NOT NULL
);
```