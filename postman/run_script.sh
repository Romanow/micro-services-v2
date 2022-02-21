#!/usr/bin/env bash

while true; do
    newman run postman_collection.json -e postman-k8s-environment.json
    sleep 1;
done