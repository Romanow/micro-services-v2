# Deploy microservices to k8s

```shell
$ helm install elasticsearch elasticsearch-chart/

$ helm install monitoring monitoring-chart/

$ helm install logging logging-chart/

$ helm install logging logging-chart/

$ helm install postgres postgres-chart/

$ helm install services services-chart/
```

Grafana dashboards: `Create` -> `Import` -> `Import via grafana.com`.

* Node Exporter – [12486](https://grafana.com/grafana/dashboards/12486-node-exporter-full/)
* Spring Boot – [10280](https://grafana.com/grafana/dashboards/10280-microservices-spring-boot-2-1/)

## Alerting

* Создать бота
  ```
  @BotFather
  /newbot
  name: k8s_monitoring
  id: k8s_monitoring_bot
  ```
* Создать канал K8S Monitoring, добавить `@k8s_monitoring_bot` как администратора. Отправить хотя бы одно сообщение в
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

## Ссылки

1. [Config Telegram bot for Grafana alerts.](https://gist.github.com/ilap/cb6d512694c3e4f2427f85e4caec8ad7)