# Store Service

## Описание API
* `GET /api/v1/store/{userUid}/orders` – ;
* `GET /api/v1/store/{userUid}/{orderUid}` – ;
* `POST /api/v1/store/{userUid}/{orderUid}/warranty` – ;
* `POST /api/v1/store/{userUid}/purchase` – ;
* `POST /api/v1/store/{userUid}/{orderUid}/refund` – ;

[https://store-service-app.herokuapp.com/api-docs](https://store-service-app.herokuapp.com/api-docs)

## Структура таблиц
```postgresql
CREATE TABLE users
(
    id       SERIAL CONSTRAINT users_pkey PRIMARY KEY,
    name     VARCHAR(255) NOT NULL CONSTRAINT idx_user_name UNIQUE,
    user_uid UUID         NOT NULL CONSTRAINT idx_user_user_uid UNIQUE
);
```