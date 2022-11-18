#!/usr/bin/env bash

version=${1:-v2.2}

images=(
  "romanowalex/store-service:$version"
  "romanowalex/order-service:$version"
  "romanowalex/warehouse-service:$version"
  "romanowalex/warranty-service:$version"
  "postgres:13"
  "docker.elastic.co/elasticsearch/elasticsearch:7.15.0"
  "grafana/grafana:8.2.5"
  "prom/prometheus:v2.31.1"
  "prom/node-exporter:v1.2.2"
)

for image in "${images[@]}"; do
  kind load docker-image "$image"
done
