# product-service
Product Service is used for product resource information get/fetch/update/insert/delete

Prerequisites:
- openjdk 11.0.14.1 2022-02-08 LTS

Steps to startup of the service:

1. Create postgres DB named 'Barista'
2. Run sql Barista_Coffee_Shop.sql which will create necessary tables and sequences
3. Run spring-integration-lock.sql which will create spring jdbc lock related tables
3. Build the service using maven 'clean install' command
4. Start the service using maven 'spring-boot:run' command
