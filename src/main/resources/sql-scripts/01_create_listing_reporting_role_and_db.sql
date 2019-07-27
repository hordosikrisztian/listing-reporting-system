-------------------
-- ROLE CREATION --
-------------------
DROP ROLE IF EXISTS list_rep_user;

CREATE ROLE list_rep_user
    SUPERUSER
    CREATEDB
    CREATEROLE
    LOGIN
    PASSWORD 'list_rep_user';

-----------------------
-- DATABASE CREATION --
-----------------------
DROP DATABASE IF EXISTS listing_reporting_db;

CREATE DATABASE listing_reporting_db
    OWNER = list_rep_user;
