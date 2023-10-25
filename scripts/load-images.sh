#!/usr/bin/env bash

version=${1:-v3.0}

images=(
  "romanowalex/store-service:$version"
  "romanowalex/order-service:$version"
  "romanowalex/warehouse-service:$version"
  "romanowalex/warranty-service:$version"
)

for image in "${images[@]}"; do
  kind load docker-image "$image"
done
