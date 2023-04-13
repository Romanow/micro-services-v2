#!/usr/bin/env bash

set -e

export PGPASSWORD=postgres
psql -U postgres -d store -c 'GRANT ALL PRIVILEGES ON SCHEMA public TO program;'
psql -U postgres -d orders -c 'GRANT ALL PRIVILEGES ON SCHEMA public TO program;'
psql -U postgres -d warehouse -c 'GRANT ALL PRIVILEGES ON SCHEMA public TO program;'
psql -U postgres -d warranty -c 'GRANT ALL PRIVILEGES ON SCHEMA public TO program;'
