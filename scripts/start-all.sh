#!/bin/bash

cd ..
(
  trap 'kill 0' SIGINT;
  export SPRING_PROFILES_ACTIVE=local &
  java -jar warranty-service/build/libs/warranty-service.jar &
  java -jar warehouse-service/build/libs/warehouse-service.jar &
  java -jar order-service/build/libs/order-service.jar &
  java -jar store-service/build/libs/store-service.jar
)