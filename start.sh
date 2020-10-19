#!/bin/bash

export SPRING_PROFILES_ACTIVE=local

echo "Starting Warranty Service"
./gradlew bootRun -p warranty-service

echo "Starting Warehouse Service"
./gradlew bootRun -p warehouse-service

echo "Starting Order Service"
./gradlew bootRun -p order-service

echo "Starting Store Service"
./gradlew bootRun -p store-service