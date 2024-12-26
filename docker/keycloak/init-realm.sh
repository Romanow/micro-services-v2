#!/usr/bin/env bash

"$KEYCLOAK_BIN_DIR"/kc.sh import --file "$KEYCLOAK_INITSCRIPTS_DIR"/realm.json
