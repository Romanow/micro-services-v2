# Microservices

## Состав

* [Store Service](store-service/README.md)
* [Order Service](order-service/README.md)
* [Warehouse Service](warehouse-service/README.md)
* [Warranty Service](warranty-service/README.md)

## Взаимодействие сервисов

```mermaid
graph TD
  A[Store Service]--> B[Order Service]
  A[Store Service] --> C[Warehouse Service]
  A[Store Service] --> D[Warranty Service]
  B[OrderService] --> D[Warranty Service]
  B[OrderService] --> C[Warehouse Service]
  C[Warehouse Service] --> D[Warranty Service]
```

## Сборка и запуск

```shell
$ ./gradlew clean build

$ docker compose build

$ dcoker compose up -d
```

## Настройка Auth0

1. Регистрируемся на [Auth0](https://auth0.com).
2. Создаем приложение: `Applications` -> `Create Application`: `Native`, заходим в созданное приложение и
   копируем `Client ID` и `Client Secret`.
3. Переходим в `Advanced Settings` -> `Grant Types`: только `Password` (Resource Owner Password Flow).
4. Переходим в `API` -> `Create API`:
    * Name: `Cinema Aggregator Service`;
    * Identifier: `http://store-service.romanow-alex.ru`;
    * Signing Algorithm: RS256.
5. Настраиваем хранилище паролей: `Settings` -> `Tenant Settings` -> `API Authorization Settings`:
    * Default Audience: `http://store-service.romanow-alex.ru`;
    * Default Directory: `Username-Password-Authentication`.
6. Создаем тестового пользователя: `User Management` -> `Users` -> `Create User`:
    * Email: `ronin@romanow-alex.ru`;
    * Password: `Qwerty123`;
    * Connection: `Username-Password-Authentication`.

После настройки у вас должен успешно выполняться запрос на проверку получение токена (подставить свои настройки):

```shell
curl --location --request POST 'https://romanowalex.eu.auth0.com/oauth/token' \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --data-urlencode 'grant_type=password' \
  --data-urlencode 'username=ronin@romanow-alex.ru' \
  --data-urlencode 'password=Qwerty123' \
  --data-urlencode 'scope=openid' \
  --data-urlencode 'client_id=<Client ID>' \
  --data-urlencode 'client_secret=<Client Secret>'
```

В ответ получаем токен:

```json
{
  "access_token": "...",
  "id_token": "...",
  "scope": "openid profile email ...",
  "expires_in": 86400,
  "token_type": "Bearer"
}
```

## Тестирование

Для проверки работоспособности системы используются скрипты Postman. В папке [postman](postman)
содержится [коллекция запросов](postman/collection.json) к серверу и два enviroment'а:

* [local](postman/local-environment.json);

Для автоматизированной проверки используется [GitHub Actions](.github/workflows/main.yml), CI/CD содержит шаги:

* сборка;
* запуск в docker compose;
* прогон скриптов postman через newman.

## TODO

1. Add labels: app=<name>.
2. Resources limits, requests.
3. Health Checks.


1. Terragrunt.
2. [10 антипаттернов деплоя в Kubernetes: распространенные практики, для которых есть другие решения](https://habr.com/ru/company/mailru/blog/529152/).
3. Release history with `helm install`.
4. [How do you rollback deployments in Kubernetes?](https://learnk8s.io/kubernetes-rollbacks)