-- file: 10-create-user-and-db.sql
CREATE DATABASE services;
CREATE ROLE program WITH PASSWORD 'test';
GRANT ALL PRIVILEGES ON DATABASE services TO program;
ALTER ROLE program WITH LOGIN;