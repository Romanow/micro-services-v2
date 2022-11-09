-- file: 10-create-user-and-dbs.sql
CREATE ROLE program WITH PASSWORD 'test';
ALTER ROLE program WITH LOGIN;

CREATE DATABASE store;
GRANT ALL PRIVILEGES ON DATABASE store TO program;

CREATE DATABASE orders;
GRANT ALL PRIVILEGES ON DATABASE orders TO program;

CREATE DATABASE warehouse;
GRANT ALL PRIVILEGES ON DATABASE warehouse TO program;

CREATE DATABASE warranty;
GRANT ALL PRIVILEGES ON DATABASE warranty TO program;