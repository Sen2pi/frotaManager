-- Initialize Fleet Management Database
-- This script runs when the PostgreSQL container starts for the first time

-- Create database (if not exists - this is handled by POSTGRES_DB env var)
-- CREATE DATABASE IF NOT EXISTS fleetdb;

-- Create user (if not exists - this is handled by POSTGRES_USER env var)
-- CREATE USER IF NOT EXISTS fleetuser WITH PASSWORD 'fleetpass';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE fleetdb TO fleetuser;

-- Connect to the database
\c fleetdb;

-- Grant schema privileges
GRANT ALL ON SCHEMA public TO fleetuser;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO fleetuser;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO fleetuser;

-- Set default privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO fleetuser;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO fleetuser;