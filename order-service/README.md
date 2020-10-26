# Order Service

## Описание API
1. `POST /api/v1/orders/{userUid}` – сделать заказ от имени пользователя;
1. `GET /api/v1/orders/{userUid}/{orderUid}` – получить информацию по конкретному заказу пользователя;
1. `GET /api/v1/orders/{userUid}` – получить все заказы пользователя;
1. `POST /api/v1/orders/{orderUid}/warranty` – запрос гарантии по заказу;
1. `DELETE /api/v1/orders/{orderUid}` – вернуть заказ.

[https://order-service-app.herokuapp.com/api-docs](https://order-service-app.herokuapp.com/api-docs)

## Логика работы
Сервис ответственен за работу с заказом, получение товара со склада (запрос к Warehouse) и создание гарантии (запрос к Warranty).
При запросе достается заказ `order`, из него получаем `item_uid` и с этим параметром выполняются необходимые запросы к Warehouse и Warranty. 

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
