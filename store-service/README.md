# Store Service

## Описание API

1. `GET /api/v1/store/orders` – получить список заказов пользователя;
1. `GET /api/v1/store/{orderUid}` – информация по конкретному заказу;
1. `POST /api/v1/store/{orderUid}/warranty` – запрос гарантии по заказу;
1. `POST /api/v1/store/purchase` – выполнить покупку;
1. `POST /api/v1/store/{orderUid}/refund` – вернуть заказ;

[https://store-service-app.herokuapp.com/api-docs](https://store-service-app.herokuapp.com/api-docs)

## Логика работы

Сервис является своеобразным gateway, все запросы проходят через него от имени пользователя. Информация о заказах
собирается с Order Service, а потом опционально с Warehouse и Warranty Service. Остальные методы проверяют пользователя
и делегируют запрос дальше на OrderService, т.к. вся информация о заказе хранится там.