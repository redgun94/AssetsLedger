e# AssetsLedger — Proof of Funds API

REST API built with **Java** and **Spring Boot** to verify proof of funds and track client assets in mortgage lending workflows.

This is a portfolio project grounded in real domain experience: in mortgage lending, underwriters need to confirm that a borrower has enough verified assets for down payment, closing costs, and reserves. AssetsLedger models that process as a backend service: registering clients and their accounts, recording asset statements, and calculating whether verified funds meet the requirements of a loan.

> 🚧 **Status:** in active development. See the [Roadmap](#roadmap) for what is done and what is next.

---

## Why this project

This one is built around a real business problem, with the constraints that come with financial data:

- **Data sensitivity:** asset and account information is personal financial data, so validation, access control, and what gets exposed in responses matter.
- **Business rules:** not every dollar counts. Large unexplained deposits, account types, and seasoning of funds affect what can be used to qualify.
- **Traceability:** every verification should be explainable. The system should show *why* funds were accepted or excluded.

---

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Web | Spring Web (REST controllers) |
| Validation | Jakarta Bean Validation |
| Persistence | Spring Data JPA *(planned)* |
| Database | PostgreSQL *(planned)*, H2 for local development |
| Build | Maven |
| Testing | JUnit 5, Mockito |

---

## Project structure

```
src/
├── main/
│   ├── java/com/assetsledger/
│   │   ├── controller/   # REST controllers (HTTP layer)
│   │   ├── service/      # Business logic and verification rules
│   │   ├── repository/   # Data access
│   │   ├── model/        # Domain entities
│   │   ├── dto/          # Request/response objects
│   │   └── exception/    # Custom exceptions and global error handling
│   └── resources/
│       └── application.yml
└── test/
    └── java/com/assetsledger/
```

The layers are kept separate on purpose: controllers only handle HTTP, services hold the business rules, and entities are never exposed directly in API responses (DTOs are used instead).

---

## Getting started

### Prerequisites

- Java 21+
- Maven 3.9+ (or use the included Maven wrapper)

### Run locally

```bash
git clone https://github.com/<your-username>/assets-ledger.git
cd assets-ledger
./mvnw spring-boot:run
```

The API starts at `http://localhost:8080`.

### Run tests

```bash
./mvnw test
```

---

## API endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/...` | *To be documented as endpoints are implemented* |

---

## Roadmap

- [x] Project setup with Spring Boot
- [ ] REST controller for core resources
- [ ] Request validation and global exception handling (`@ControllerAdvice`)
- [ ] Persistence with Spring Data JPA and PostgreSQL
- [ ] Database migrations with Flyway
- [ ] Proof of funds verification rules (sufficient funds, large deposit flags)
- [ ] Unit and integration tests (Testcontainers)
- [ ] Authentication and authorization with Spring Security + JWT
- [ ] API documentation with OpenAPI / Swagger
- [ ] Docker setup

---

## Design decisions

Key technical decisions and trade-offs will be documented here as the project evolves.

---

## Author

**Leo** — Mortgage/lending professional transitioning into backend development with Java.


---

