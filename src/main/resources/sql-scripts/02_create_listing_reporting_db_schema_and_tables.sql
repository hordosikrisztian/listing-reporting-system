---------------------
-- SCHEMA CREATION --
---------------------
DROP SCHEMA IF EXISTS list_rep
    CASCADE;

CREATE SCHEMA list_rep
    AUTHORIZATION list_rep_user;

--------------------
-- TABLE CREATION --
--------------------
DROP TABLE IF EXISTS
    list_rep.listings,
    list_rep.locations,
    list_rep.listing_statuses,
    list_rep.marketplaces;

-- Locations
CREATE TABLE list_rep.locations (
    id UUID PRIMARY KEY,
    manager_name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    address_primary VARCHAR(255) NOT NULL,
    address_secondary VARCHAR(255) DEFAULT NULL,
    country VARCHAR(255) NOT NULL,
    town VARCHAR(255) NOT NULL,
    postal_code VARCHAR(255) NOT NULL
);

-- Statuses
CREATE TABLE list_rep.listing_statuses (
    id INTEGER PRIMARY KEY,
    status_name VARCHAR(255) NOT NULL
);

-- Marketplaces
CREATE TABLE list_rep.marketplaces (
    id INTEGER PRIMARY KEY,
    marketplace_name VARCHAR(255) NOT NULL
);

-- Listings
CREATE TABLE list_rep.listings (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    location_id UUID NOT NULL REFERENCES list_rep.locations (id),
    listing_price NUMERIC(1000, 2) NOT NULL,
    currency CHAR(3) NOT NULL,
    quantity INTEGER NOT NULL,
    listing_status_id INTEGER NOT NULL REFERENCES list_rep.listing_statuses (id),
    marketplace_id INTEGER NOT NULL REFERENCES list_rep.marketplaces (id),
    upload_time DATE NOT NULL,
    owner_email_address VARCHAR(255) NOT NULL
);
