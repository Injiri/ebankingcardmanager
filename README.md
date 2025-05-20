## My understanding of the problem & Solution Outline

To implement a microservices-based banking solution with three micorservices:

* Customer Service: CRUD for customer data (first name, last name mandatory; other name optional)
* Account Service: CRUD for accounts (each account has IBAN, BIC SWIFT, belongs to one customer)
* Card Service: CRUD for cards (card alias editable; account ID, card type, PAN, CVV not editable)

Key rules considered in dev:
* One customer can have multiple accounts.
* Each account belongs to exactly one customer.
* Each account can have max 2 cards.
* One card per type (virtual/physical) per account.
* Sensitive card fields (PAN, CVV) are masked by default on API responses; override param allows unmasking.

Data Retrieval/query APIs support:
* pagination and filtering:Customers by name (full text search) and created date range. 
* Accounts by IBAN, BIC SWIFT, card alias.
* Cards by card alias, card type, PAN.


## How to Test the solution Locally

### 1. Start the PostgreSQL Database Container

Run the following command to start a PostgreSQL container:

```bash
docker run --name dtpebanking-postgres -e POSTGRES_PASSWORD=devdtbept -p 5432:5432 -d postgres:15
```

### 2. Build and Run the Application from the Monorepo Root

Make sure the build script is executable, then run it while at the root dir:

```bash
mvn clean install && chmod +x dtb-build-all.sh && ./dtb-build-all.sh
```


### 3. Start the Services cia docker compose

Build and start the containers with:

```bash
docker-compose -f docker-compose.yml up --build
```


### 4. To Rebuild and Restart Containers

If you need do a clean up and rebuild containers from scratch then :

```bash
docker-compose down -v
docker-compose up --build
```
### My test coverage `` mvn clean verify`` see...

|     Service      | Coverage Report Link                                            |
|:----------------:| --------------------------------------------------------------- |
| Customer Service | [View Report](./customer-service/target/site/jacoco/index.html) |
| Account Service  | [View Report](./account-service/target/site/jacoco/index.html)  |
|   Card Service   | [View Report](./card-service/target/site/jacoco/index.html)     |


