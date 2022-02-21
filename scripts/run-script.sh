#!/usr/bin/env bash

while true; do
    newman run ../postman/store-collection.json -e ../postman/[os]\ environment.json
    sleep 1;
done