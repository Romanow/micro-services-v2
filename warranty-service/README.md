# Warranty Service

## Описание API
* `GET /api/v1/warranty/{itemUid}` – информация о статусе гарантии;
* `POST /api/v1/warranty/{itemUid}/warranty` – запрос решения по гарантии;
* `POST /api/v1/warranty/{itemUid}` – запрос на начало гарантийного периода;
* `DELETE /api/v1/warranty/{itemUid}` – запрос на закрытие гарантии.

[https://warranty-service-app.herokuapp.com/api-docs](https://warranty-service-app.herokuapp.com/api-docs)

## Структура таблиц
```postgresql
CREATE TABLE warranty
(
    id            SERIAL CONSTRAINT warranty_pkey PRIMARY KEY,
    comment       VARCHAR(1024),
    item_uid      UUID         NOT NULL CONSTRAINT idx_warranty_item_uid UNIQUE,
    status        VARCHAR(255) NOT NULL,
    warranty_date TIMESTAMP    NOT NULL
);
```