# reward-mgmt-service

## Project Summary
reward-mgmt-service is a Spring Boot backend application that calculates customer reward points from transaction data over a rolling three-month period. The calculation is fully date-driven and does not rely on fixed or hardcoded months.

## Technology Stack
- Java 17  
- Spring Boot 3.2.5  
- PostgreSQL  
- Maven  
- OpenAPI / Swagger  
- JUnit 5  
- Mockito  
- Spring Cache Abstraction  

## Reward Calculation Rules
Reward points are calculated using the following logic:

- 2 points for every dollar spent above $100  
- 1 point for every dollar spent between $50 and $100  
- No reward points for amounts less than or equal to $50  

## API Details

### Endpoint
GET `/api/rewards`

### Query Parameters
- `start` – Start date (yyyy-MM-dd)  
- `end` – End date (yyyy-MM-dd)  
- `page` – Page index (0-based)  
- `size` – Number of records per page  

### Sample Request
/api/rewards?start=2024-01-01&end=2024-03-31&page=0&size=5

## Features
- Paginated API responses  
- Caching for improved performance  
- Swagger-based API documentation  
- Global exception handling mechanism  
- Unit and integration test coverage  
- Reward calculation based on transaction dates (no hardcoded months)  

## Running the Application
mvn clean install
mvn spring-boot:run

Swagger:
http://localhost:8080/swagger-ui/index.html
