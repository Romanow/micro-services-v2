#!/bin/bash
set -e

export PGPASSWORD=test
psql -U program -d services <<-EOSQL
    CREATE SCHEMA IF NOT EXISTS store;
    CREATE SCHEMA IF NOT EXISTS orders;
    CREATE SCHEMA IF NOT EXISTS warehouse;
    CREATE SCHEMA IF NOT EXISTS warranty;
    CREATE SCHEMA IF NOT EXISTS keycloak;
EOSQL
