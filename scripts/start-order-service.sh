#!/bin/bash
(cd .. && java -jar -Dspring.profiles.active=local order-service/build/libs/order-service.jar)