#!/bin/bash
(cd .. && java -jar -Dspring.profiles.active=local store-service/build/libs/store-service.jar)
