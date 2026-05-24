# Payment Management API

Backend technical test developed for Ginko Financial Solutions.

---

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- H2 Database
- Maven
- Swagger / OpenAPI
- JUnit 5
- Mockito

---

## How to Run

### Clone repository

```bash
git clone <repository-url>
cd payment-management
```

### Run application

Windows:

```bash
.\mvnw spring-boot:run
```

Linux / Mac:

```bash
./mvnw spring-boot:run
```

---

## API Documentation

### Swagger UI

```txt
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI Docs

```txt
http://localhost:8080/v3/api-docs
```

---

## H2 Database Console

```txt
http://localhost:8080/h2-console
```

### H2 Credentials

| Property | Value |
|---|---|
| JDBC URL | jdbc:h2:mem:testdb |
| User | sa |
| Password | (empty) |

---

## Running Tests

Windows:

```bash
.\mvnw test
```

Linux / Mac:

```bash
./mvnw test
```

---

## Design Decisions

### H2 Database

H2 was selected to simplify local execution and evaluation without requiring external infrastructure.

### DTO Pattern

DTOs were implemented to decouple persistence entities from API contracts and avoid exposing internal models directly.

### Global Exception Handling

A centralized exception handling strategy was implemented using `@RestControllerAdvice` to provide consistent and descriptive HTTP error responses.

### Layered Architecture

The application follows a layered architecture separating responsibilities between:
- Controllers
- Services
- Repositories

This improves maintainability, readability and testability.

### Enum Persistence Strategy

Enums are persisted using `EnumType.STRING` to improve readability and avoid issues caused by ordinal persistence.

---

# API Examples

---

## Create Provider

### Request

`POST /api/providers`

```json
{
  "businessName": "ACME SAS",
  "taxId": "123456789",
  "email": "acme@test.com"
}
```

### Response

```json
{
  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "businessName": "ACME SAS",
  "taxId": "123456789",
  "email": "acme@test.com",
  "status": "ACTIVE"
}
```

---

## Get Providers With Pagination

### Request

`GET /api/providers?page=0&size=5&sort=businessName,asc`

### Response

```json
{
  "content": [
    {
      "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "businessName": "ACME SAS",
      "taxId": "123456789",
      "email": "acme@test.com",
      "status": "ACTIVE"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "size": 5,
  "number": 0
}
```

---

## Filter Providers By Status

### Request

`GET /api/providers?status=ACTIVE`

---

## Get Total Amount Paid By A Provider

### Request

`GET /api/payment-orders/reports/provider-payments?providerId=UUID&startDate=2026-05-01&endDate=2026-05-31`

### Response

```json
{
  "providerId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "providerName": "ACME SAS",
  "startDate": "2026-05-01",
  "endDate": "2026-05-31",
  "totalPaid": 4500000
}
```

---
## Get Soon To Expire Orders

### Request
`GET /api/payment-orders/expiring`

### Response
```json
[
  {
    "orderId": "4c6f9c8d-ef71-4d15-a95b-92cce9b9f444",
    "providerName": "ACME SAS",
    "amount": 1500000,
    "concept": "Software development services",
    "createdAt": "2026-04-01T10:00:00",
    "daysPending": 28
  }
]
```

---

## Create Payment Order

### Request

`POST /api/payment-orders`

```json
{
  "providerId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "amount": 1500000,
  "concept": "Software development services"
}
```

### Response

```json
{
  "id": "4c6f9c8d-ef71-4d15-a95b-92cce9b9f444",
  "providerId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "providerName": "ACME SAS",
  "amount": 1500000,
  "concept": "Software development services",
  "createdAt": "2026-05-24T15:30:00",
  "status": "DRAFT"
}
```

---

## Update Payment Order Status

### Request

`PATCH /api/payment-orders/{id}/status`

```json
{
  "status": "APPROVED"
}
```

---

## Valid Status Transitions

| Current Status | Allowed Transition |
|---|---|
| DRAFT | APPROVED |
| DRAFT | REJECTED |
| APPROVED | PAID |

Any other transition returns HTTP 400.

---

## Example Error Response

```json
{
  "timestamp": "2026-05-24T16:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid status transition from PAID to APPROVED"
}
```

---

## Pending Features

The following features were intentionally left out to prioritize core business requirements and code quality:

- Idempotency-Key support
- Concurrency handling

---
## Additional Features

### Provider Payment Report

An aggregated reporting endpoint was implemented to calculate the total amount paid to a provider within a date range.

#### Business Rules

- Only payment orders with status `PAID` are included.
- The report filters by provider and creation date range.

#### Endpoint

```txt
GET /api/payment-orders/reports/provider-payments
```

#### Example

```txt
GET /api/payment-orders/reports/provider-payments?providerId=UUID&startDate=2026-05-01&endDate=2026-05-31
```

---

### Expiring Payment Orders

An endpoint was implemented to retrieve payment orders considered close to expiration.

#### Business Rule

A payment order is considered "expiring" when:
- its status is `APPROVED`
- and it has been pending for more than 25 days

This assumes a payment SLA of 30 days.

#### Endpoint

```txt
GET /api/payment-orders/expiring
```
---
## Author

Victor Mora