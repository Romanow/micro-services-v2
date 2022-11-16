#!/usr/bin/env bash

images=(
  "romanowalex/store-service:v2.2"
  "romanowalex/order-service:v2.2"
  "romanowalex/warehouse-service:v2.2"
  "romanowalex/warranty-service:v2.2"
  "docker.elastic.co/elasticsearch/elasticsearch:7.15.0"
  "grafana/grafana:8.2.5"
  "prom/prometheus:v2.31.1"
  "prom/node-exporter:v1.2.2"
)

for image in "${images[@]}"; do
  kind load docker-image "$image"
done
