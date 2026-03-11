# Rewards Management System

> A Spring Boot RESTful application that calculates customer reward points earned through retail purchases.

## Table of Contents

1. Overview
2. Points Calculation Rules
3. Project Structure
4. Prerequisites
5. Maven Configuration
6. Build and Run
7. API Endpoints
8. Sample Dataset
9. Seeding Data
10. Running Tests
11. Code Documentation

## Overview

A retailer offers a rewards programme that awards points to customers based on each recorded purchase. Given a record of transactions over any time period, the application calculates the reward points earned per customer per month and their cumulative total.

## Points Calculation Rules

The points calculation follows these rules:

- **Amount ≤ $50:** 0 points
- **$50 < Amount ≤ $100:** 1 point per dollar above $50
- **Amount > $100:** 2 points per dollar above $100 plus 1 point per dollar for the $50–$100 band

**Example:** A $120 purchase earns `2 × $20 + 1 × $50 = 90 points`.

**Note:** Only whole dollars are counted and fractional cents are truncated.

## Project Structure

The project is organized as follows:

```
rewards-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/retailer/rewards/
│   │   │   ├── RewardsApplication.java
│   │   │   ├── controller/
│   │   │   │   └── RewardsController.java
│   │   │   ├── service/
│   │   │   │   ├── RewardsService.java
│   │   │   │   └── RewardsServiceImpl.java
│   │   │   ├── model/
│   │   │   │   ├── Customer.java
│   │   │   │   ├── Transaction.java
│   │   │   │   ├── MonthlyReward.java
│   │   │   │   └── CustomerRewardSummary.java
│   │   │   ├── repository/
│   │   │   │   ├── CustomerRepository.java
│   │   │   │   └── TransactionRepository.java
│   │   │   ├── data/
│   │   │   │   ├── CustomerDataStore.java
│   │   │   │   └── TransactionDataProvider.java
│   │   │   ├── util/
│   │   │   │   └── PointsCalculator.java
│   │   │   └── exception/
│   │   │       ├── CustomerNotFoundException.java
│   │   │       ├── InvalidDateRangeException.java
│   │   │       ├── InvalidTransactionException.java
│   │   │       ├── ErrorResponse.java
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/retailer/rewards/
│           ├── RewardsApplicationTest.java
│           ├── RewardsManagementIntegrationTests.java
│           ├── service/
│           │   └── RewardsServiceTest.java
│           ├── controller/
│           │   └── RewardsControllerTest.java
│           └── util/
│               └── PointsCalculatorTest.java
├── .gitignore
├── pom.xml
└── README.md
```

## Prerequisites

The following tools are required:

| Tool  | Version |
|-------|--------|
| Java  | 17+    |
| Maven | 3.6+   |

## Maven Configuration

### Local Development Profile

A Maven profile for local development has been configured in the `.m2/settings.xml` file with the following details:

**Profile ID:** `local-dev`

**Features include:**
- Uses Maven Central Repository for dependencies
- Sets `spring.profiles.active=local` property
- Environment variable `env=local`

**To activate the profile:**
```bash
mvn clean install -Plocal-dev
```

**Or run the application with the profile:**
```bash
mvn spring-boot:run -Plocal-dev
```

## Build and Run

To get started:

1. Clone the repository and navigate to the project root:
```bash
git clone your-repo-url
cd rwd-mgmt-sys-latest
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The server starts on http://localhost:8080.

## API Endpoints

### Get all customer reward summaries

**Endpoint:** `GET /api/rewards`  
**Response:** `200 OK`

Returns an array of customer reward summaries. Each summary contains:
- `customerId`: unique customer identifier
- `customerName`: customer's full name
- `monthlyRewards`: array of monthly reward objects with month and points
- `totalPoints`: sum of all points earned

**Example response:**
```json
{
  "customerId": "C001",
  "customerName": "Alice Johnson",
  "monthlyRewards": [
    { "month": "January 2024", "points": 115 },
    { "month": "February 2024", "points": 250 },
    { "month": "March 2024", "points": 205 }
  ],
  "totalPoints": 570
}
```

### Get reward summary for a specific customer

**Endpoint:** `GET /api/rewards/{customerId}`  
**Path variable:** `customerId` is the unique customer identifier such as `C001`  
**Response:** `200 OK`

Returns a single customer reward summary with the same structure as above.

**Error response for customer not found:** `404 Not Found`

Returns an error object containing:
- `status`: 404
- `error`: Not Found
- `message`: Customer not found with ID: C999
- `timestamp`: 2024-03-11T10:30:00

**Example error response:**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with ID: C999",
  "timestamp": "2024-03-11T10:30:00"
}
```

## Sample Dataset

The application ships with three customers and sixteen transactions spread across January to March 2024.

**Customer details:**

| Customer ID | Customer Name  | Jan Points | Feb Points | Mar Points | Total Points |
|-------------|----------------|------------|------------|------------|-------------|
| C001        | Alice Johnson  | 115        | 250        | 205        | 570         |
| C002        | Bob Smith      | 50         | 110        | 350        | 510         |
| C003        | Carol Davis    | 70         | 170        | 250        | 490         |

Transaction details are in `TransactionDataProvider.java`.

## Seeding Data

The application automatically seeds sample data on startup through the `TransactionDataProvider` class.

**Sample Data Includes:**
- 3 customers (C001-C003) with names and IDs
- 16 transactions across January-March 2024
- Various amounts to demonstrate different point calculations

**Automatic Loading (Default):**

1. Clone and run the application:
   ```bash
   git clone your-repo-url
   cd reward-mgmt-service
   mvn clean install
   mvn spring-boot:run
   ```

2. Data loads automatically on startup

3. Verify with API call:
   ```bash
   curl http://localhost:8080/api/rewards
   ```

**Manual Database Setup (Alternative):**

1. Create `src/main/resources/sample-data.sql`:
   ```sql
   INSERT INTO customers (customer_id, customer_name) VALUES
   ('C001', 'Alice Johnson'),
   ('C002', 'Bob Smith'),
   ('C003', 'Carol Davis');
   
   INSERT INTO transactions (transaction_id, customer_id, amount, transaction_date) VALUES
   ('T001', 'C001', 120.00, '2024-01-15'),
   ('T002', 'C001',  75.00, '2024-01-25'),
   ('T003', 'C001', 200.00, '2024-02-10'),
   ('T004', 'C001',  45.00, '2024-02-20'),
   ('T005', 'C001', 160.00, '2024-03-05'),
   ('T006', 'C001',  85.00, '2024-03-15'),
   ('T007', 'C002',  95.00, '2024-01-10'),
   ('T008', 'C002',  55.00, '2024-01-28'),
   ('T009', 'C002', 130.00, '2024-02-14'),
   ('T010', 'C002', 250.00, '2024-03-03'),
   ('T011', 'C002',  40.00, '2024-03-22'),
   ('T012', 'C003', 110.00, '2024-01-07'),
   ('T013', 'C003',  70.00, '2024-02-09'),
   ('T014', 'C003', 150.00, '2024-02-19'),
   ('T015', 'C003',  90.00, '2024-03-12'),
   ('T016', 'C003', 180.00, '2024-03-25');
   ```

2. Execute via H2 Console:
   - Start app: `mvn spring-boot:run`
   - Access: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`, Password: (empty)

**Modify Sample Data:**

Edit `src/main/java/com/retailer/rewards/data/TransactionDataProvider.java` and restart the application.

## Running Tests

To run tests:
```bash
mvn test
```

Test coverage includes:

RewardsApplicationTest: Spring Boot application context load verification

PointsCalculatorTest: 11 unit tests for PointsCalculator including:
- Boundary cases for 0, 50, 51, 100, 101, 120, 120.99, and 200 dollars
- Negative amount handling that throws InvalidTransactionException

RewardsServiceTest: 11 unit tests using Mockito including:
- Multi-customer reward summaries
- Customer with no transactions returns 0 points
- Same-month transaction aggregation
- Chronological sort of monthly entries
- Unknown customer throws CustomerNotFoundException
- Single customer across 3 months
- All transactions in the same month creates 1 monthly entry
- Date range validation

RewardsControllerTest: 8 unit tests using Mockito including:
- GET /api/rewards happy path
- GET /api/rewards empty dataset
- GET /api/rewards invalid date range returns 400
- GET /api/rewards/{customerId} valid customer
- GET /api/rewards/{customerId} unknown customer returns 404
- GET /api/rewards/{customerId} customer with no transactions
- GET /api/rewards/{customerId} multiple transactions same month are aggregated
- GET /api/rewards/{customerId} invalid date range returns 400

RewardsManagementIntegrationTests: 6 integration tests with full Spring context including:
- End-to-end API testing with real database
- Date range filtering
- Error handling validation

## Code Documentation

All classes and methods in the codebase are documented with single-line Javadoc comments following Java documentation standards:

- Classes: Brief description of the class purpose
- Methods: Concise explanation of method functionality
- Constructors: Description of constructor parameters and purpose

Example documentation:
```java
/** Service implementation for customer reward calculations. */
@Service
public class RewardsServiceImpl implements RewardsService {
    
    /** Constructs a RewardsServiceImpl with the specified data provider. */
    public RewardsServiceImpl(CustomerDataStore dataProvider) {
        this.dataProvider = dataProvider;
    }
    
    /** Retrieves reward summaries for all customers within the specified date range. */
    @Override
    public List<CustomerRewardSummary> getAllCustomerRewards() {
        // implementation
    }
}
```


