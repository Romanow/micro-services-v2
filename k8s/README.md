# Deploy microservices to k8s

Бдуем использовать managed kubernetes on DigitalOcean. Развертывание с
помощью [terraform](https://github.com/Romanow/ansible-kubernetes):

```shell
# Устанавливаем базу данных и сервисы
kubectl apply -f postgres-deployment.yml
helm install services services-chart/

# Устанавливаем монитоинг Grafana + Prometheus
helm install monitoring monitoring-chart/

# Устанавливаем ELK stack
helm install logging logging-chart/
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

## Ссылки

1. [Config Telegrambot for grafana's alerts.](https://gist.github.com/ilap/cb6d512694c3e4f2427f85e4caec8ad7)