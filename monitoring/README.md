## Prometheus & Grafana

```shell
kubectl create namespace monitoring
kubectl apply -f node-exporter-deployment.yml -n monitoring
kubectl apply -f prometheus-configmap.yml -f prometheus-deployment.yml -n monitoring
```