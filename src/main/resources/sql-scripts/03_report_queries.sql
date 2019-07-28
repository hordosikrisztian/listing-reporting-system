--------------------
-- Report queries --
--------------------

-- Postgres queries to be adapted to JPQL/HQL.

-- Total listing count
SELECT COUNT(*) AS total_listing_count
FROM list_rep.listings;

-- Total eBay listing count
SELECT COUNT(*) AS total_ebay_listing_count
FROM list_rep.listings AS l
INNER JOIN list_rep.marketplaces AS m
    ON l.marketplace_id = m.id
WHERE m.marketplace_name = 'EBAY';

-- Total eBay listing price
SELECT SUM(l.listing_price) AS total_ebay_listing_price
FROM list_rep.listings AS l
INNER JOIN list_rep.marketplaces AS m
    ON l.marketplace_id = m.id
WHERE m.marketplace_name = 'EBAY';

-- Average eBay listing price
SELECT ROUND(AVG(l.listing_price), 2) AS average_ebay_listing_price
FROM list_rep.listings AS l
INNER JOIN list_rep.marketplaces AS m
    ON l.marketplace_id = m.id
WHERE m.marketplace_name = 'EBAY';

-- Total Amazon listing count
SELECT COUNT(*) AS total_amazon_listing_count
FROM list_rep.listings AS l
INNER JOIN list_rep.marketplaces AS m
    ON l.marketplace_id = m.id
WHERE m.marketplace_name = 'AMAZON';

-- Total Amazon listing price
SELECT SUM(l.listing_price) AS total_amazon_listing_price
FROM list_rep.listings AS l
INNER JOIN list_rep.marketplaces AS m
    ON l.marketplace_id = m.id
WHERE m.marketplace_name = 'AMAZON';

-- Average Amazon listing price
SELECT ROUND(AVG(l.listing_price), 2) AS average_amazon_listing_price
FROM list_rep.listings AS l
INNER JOIN list_rep.marketplaces AS m
    ON l.marketplace_id = m.id
WHERE m.marketplace_name = 'AMAZON';

-- Best lister email address
SELECT
	l.owner_email_address AS best_lister_email_address,
	COUNT(*) AS listings_count
FROM list_rep.listings AS l
GROUP BY best_lister_email_address
ORDER BY listings_count DESC
LIMIT 1;

---------------------
-- Monthly reports --
---------------------

-- Total eBay listing count per month
SELECT
    TO_CHAR(l.upload_time, 'MM/YYYY') AS month_of_year,
    COUNT(*) AS total_ebay_listing_count
FROM list_rep.listings AS l
INNER JOIN list_rep.marketplaces AS m
    ON l.marketplace_id = m.id
WHERE m.marketplace_name = 'EBAY'
GROUP BY month_of_year;

-- Total eBay listing price per month
SELECT
    TO_CHAR(l.upload_time, 'MM/YYYY') AS month_of_year,
    SUM(l.listing_price) AS total_ebay_listing_price
FROM list_rep.listings AS l
INNER JOIN list_rep.marketplaces AS m
    ON l.marketplace_id = m.id
WHERE m.marketplace_name = 'EBAY'
GROUP BY month_of_year;

-- Average eBay listing price per month
SELECT
    TO_CHAR(l.upload_time, 'MM/YYYY') AS month_of_year,
    ROUND(AVG(l.listing_price), 2) AS average_ebay_listing_price
FROM list_rep.listings AS l
INNER JOIN list_rep.marketplaces AS m
    ON l.marketplace_id = m.id
WHERE m.marketplace_name = 'EBAY'
GROUP BY month_of_year;

-- Average Amazon listing price per month
SELECT
    TO_CHAR(l.upload_time, 'MM/YYYY') AS month_of_year,
    ROUND(AVG(l.listing_price), 2) AS average_amazon_listing_price
FROM list_rep.listings AS l
INNER JOIN list_rep.marketplaces AS m
    ON l.marketplace_id = m.id
WHERE m.marketplace_name = 'AMAZON'
GROUP BY month_of_year;

-- Total Amazon listing count per month
SELECT
    TO_CHAR(l.upload_time, 'MM/YYYY') AS month_of_year,
    COUNT(*) AS total_amazon_listing_count
FROM list_rep.listings AS l
INNER JOIN list_rep.marketplaces AS m
    ON l.marketplace_id = m.id
WHERE m.marketplace_name = 'AMAZON'
GROUP BY month_of_year;

-- Total Amazon listing price per month
SELECT
    TO_CHAR(l.upload_time, 'MM/YYYY') AS month_of_year,
    SUM(l.listing_price) AS total_amazon_listing_price
FROM list_rep.listings AS l
INNER JOIN list_rep.marketplaces AS m
    ON l.marketplace_id = m.id
WHERE m.marketplace_name = 'AMAZON'
GROUP BY month_of_year;

-- Best lister email address of the month
SELECT
	DISTINCT ON (month_of_year) subquery.month_of_year AS month_of_year,
	subquery.owner_email_address AS best_lister_email_address
FROM (SELECT 
	  	  TO_CHAR(l.upload_time, 'MM/YYYY') AS month_of_year,
	  	  l.owner_email_address AS owner_email_address,
	  	  COUNT(*) AS listings_count
  	  FROM list_rep.listings AS l
  	  GROUP BY month_of_year, owner_email_address
  	  ORDER BY month_of_year ASC, listings_count DESC) AS subquery;
