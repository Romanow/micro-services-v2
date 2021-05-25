#!/bin/bash

cd ..
(
  trap 'kill 0' SIGINT;
  export SPRING_PROFILES_ACTIVE=docker &
  java -jar -Dspring.profiles.active=docker warranty-service/build/libs/warranty-service.jar &
  java -jar -Dspring.profiles.active=docker warehouse-service/build/libs/warehouse-service.jar &
  java -jar -Dspring.profiles.active=docker order-service/build/libs/order-service.jar &
  java -jar -Dspring.profiles.active=docker store-service/build/libs/store-service.jar
)