#!/bin/bash
(cd .. && java -jar -Dspring.profiles.active=local warehouse-service/build/libs/warehouse-service.jar)