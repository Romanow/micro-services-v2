# Deploy microservices to k8s

Бдуем использовать managed kubernetes on DigitalOcean. Развертывание с
помощью [terraform](https://github.com/Romanow/ansible-kubernetes):

```shell
# Устанавливаем Elasticsearch (нужен для Jaeger и ELK)
helm install elasticsearch elasticsearch-chart/

# Устанавливаем монитоинг Prometheus, Grafana и NodeExporter
helm install monitoring monitoring-chart/

# Устанавливаем ELK stack (ElasticSearch установлен выше)
helm install logging logging-chart/

# Устанавливаем Jaeger
helm install logging logging-chart/

# Устанавливаем базу данных и сервисы
helm install services services-chart/
```

Grafana dashboards: `Create` -> `Import` -> `Import via grafana.com`.

* Node Exporter – 1860
* Spring Boot – 10280

Адреса сервисов:

* Store Service: https://store.romanow-alex.ru
* Order Service: https://order.romanow-alex.ru
* Warehouse Service: https://warehouse.romanow-alex.ru
* Warranty Service: https://warranty.romanow-alex.ru
* Prometheus: https://prometheus.romanow-alex.ru
* Grafana: https://grafana.romanow-alex.ru
* Jaeger: https://jaeger.romanow-alex.ru

## Grafana alerting

* Создать бота
  ```
  @BotFather
  /newbot
  name: k8s_monitoring
  id: k8s_monitoring_bot
  ```
* Создать канал K8S Monitoring, добавить @k8s_monitoring_bot как администратора. Отправить хотя бы одно сообщение в
  группу.
* После этого через Telegram API получить chart ID: `https://api.telegram.org/bot<token>/getUpdates`.
* `Grafana` -> `Alerting` -> `Notification Channels` -> `Telegram Bot`.
* Создать новый dashboard:
    * Title: Request Count
    * Variables: `instance` -> `label_values(jvm_classes_loaded_classes{}, instance)`
    * Panel: `Query: sum(irate(http_server_requests_seconds_count{application="store-service"}[5m]))`
    * Alerting:
        * Condition: `Evaluate every 10s for 0, when avg() of query(A, 10s, now) is above 10`
        * Send to: `Telegram bot`, message: `Too many requests`

## Деплой в OpenShift

Для использования Nexus (Private Docker Registry) нужно в OS создать secret:

```shell
oc create secret docker-registry \
  --docker-server=nexus.edu.inno.tech \
  --docker-username=<username> \
  --docker-password=<password> \
  --docker-email=unused \
  private-registry
```

## Ссылки

1. [Config Telegrambot for grafana's alerts.](https://gist.github.com/ilap/cb6d512694c3e4f2427f85e4caec8ad7)