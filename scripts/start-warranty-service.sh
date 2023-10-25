#!/bin/bash
(cd .. && java -jar -Dspring.profiles.active=local warranty-service/build/libs/warranty-service.jar)
