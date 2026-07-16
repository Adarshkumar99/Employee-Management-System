-- =====================================================================
-- schema.sql
-- Purpose : Creates the database and table used by the Employee
--           Management System.
-- Run this once (in psql or pgAdmin) before starting the Java app.
-- =====================================================================

-- Create a dedicated database (run this line separately, outside a
-- transaction, if your client complains about "CREATE DATABASE" inside
-- a script).
-- CREATE DATABASE employee_management;

-- Connect to that database, then run the rest of this file.

-- Drop the table first so this script can be re-run safely during
-- development without manual cleanup.
DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
    id            SERIAL PRIMARY KEY,        -- auto-incrementing unique ID
    name          VARCHAR(100) NOT NULL,     -- employee full name
    email         VARCHAR(100) UNIQUE NOT NULL, -- unique so we can search/identify by email
    department    VARCHAR(50)  NOT NULL,     -- e.g. "Engineering", "HR"
    salary        NUMERIC(10, 2) NOT NULL,   -- salary with 2 decimal places
    joining_date  DATE NOT NULL DEFAULT CURRENT_DATE
);

-- A few sample rows so you can test SELECT/UPDATE/DELETE immediately
-- without typing everything by hand.
INSERT INTO employees (name, email, department, salary, joining_date) VALUES
    ('Adarsh Kumar',   'adarsh@example.com',   'Engineering', 55000.00, '2024-01-15'),
    ('Priya Sharma',   'priya@example.com',    'HR',          42000.00, '2023-06-01'),
    ('Rahul Verma',    'rahul@example.com',    'Sales',       38000.00, '2022-11-20');
