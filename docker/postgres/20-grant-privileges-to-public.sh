#!/usr/bin/env bash

set -e

export PGPASSWORD=postgres
psql -U postgres -d store -с 'GRANT ALL PRIVILEGES ON SCHEMA public TO program;'
psql -U postgres -d orders -с 'GRANT ALL PRIVILEGES ON SCHEMA public TO program;'
psql -U postgres -d warehouse -с 'GRANT ALL PRIVILEGES ON SCHEMA public TO program;'
psql -U postgres -d warranty -с 'GRANT ALL PRIVILEGES ON SCHEMA public TO program;'
