# Appliance Management System — Backend

A REST API for managing appliances, clients, evaluation sessions, and users, built with Spring Boot.

**Live API:** https://isicod-projet-production.up.railway.app
**Frontend repo:** [isicod-projet-frontend](https://github.com/oussamaelater9/isicod-projet-frontend)
**Live app:** https://appliance-mgmt.vercel.app

## Features

- JWT authentication and role-based authorization (`ADMIN`, `CONSULTANT`, `SUPERADMIN`)
- CRUD APIs for appliances, clients, evaluations, and sessions
- Real-time notifications over WebSocket (STOMP/SockJS)
- Centralized exception handling with consistent JSON error responses
- Bean validation on request DTOs
- Database schema versioning with Flyway
- API documentation via Swagger / OpenAPI
- Unit, controller-slice, and integration tests (JUnit 5, Mockito, Spring Test)
- Dockerized, multi-stage build; CI pipeline via GitHub Actions; deployed on Railway

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.4, Spring MVC, Spring Security, Spring Data JPA |
| Database | PostgreSQL, Flyway |
| Auth | JWT (jjwt) |
| Docs | springdoc-openapi (Swagger UI) |
| Testing | JUnit 5, Mockito, H2 (test scope) |
| CI/CD | GitHub Actions, Docker, Railway |

## Architecture

```
controller  →  service  →  repository  →  database
     ↓              ↓
    dto          entity
```

- **controller** — REST endpoints
- **service** — business logic
- **repository** — Spring Data JPA interfaces
- **dto / entity** — request/response payloads, JPA models
- **config** — security, CORS, WebSocket, OpenAPI
- **exception** — global exception handling
- **util** — JWT generation/validation, request filter

## Getting Started

### Prerequisites

- Java 21, Maven 3.9+, PostgreSQL 16 (or Docker)

### Run locally

```bash
git clone https://github.com/oussamaelater9/isicod-projet.git
cd isicod-projet
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

API at `http://localhost:8080`, Swagger UI at `http://localhost:8080/swagger-ui.html`

### Run with Docker Compose

```bash
docker-compose up --build
```

Uses the `prod` profile internally, connecting to the bundled Postgres container via env vars.

### Environment variables (`prod` profile — used on Railway and in Docker Compose)

| Variable | Description |
|---|---|
| `SPRING_PROFILES_ACTIVE` | Set to `prod` |
| `PGHOST` / `PGPORT` / `PGDATABASE` / `PGUSER` / `PGPASSWORD` | PostgreSQL connection |
| `JWT_SECRET` | Secret key used to sign JWTs |

## API Overview

| Resource | Base path | Notes |
|---|---|---|
| Auth | `/api/auth` | signup, login |
| Users | `/api/users` | admin/consultant-only |
| Clients | `/api/clients` | CRUD, search, lookup by email |
| Appliances | `/api/appliances` | CRUD |
| Evaluations | `/api/evaluations` | CRUD, lookup by appliance |
| Sessions | `/api/sessions` | CRUD, lookup by appliance/status |
| Notifications | `/api/notifications` | list, mark as read (auth required) |
| Logs | `/api/logs` | audit/activity log |

Full interactive docs via Swagger UI once the app is running.

## Testing

```bash
mvn clean verify
```

Unit (service layer), controller-slice (`@WebMvcTest`), and integration tests against an in-memory H2 database.

## CI/CD

GitHub Actions builds, tests, and Docker-builds on every push/PR to `main`. Railway auto-deploys `main` to production; Vercel auto-deploys the frontend the same way.

## Roadmap

- Expand test coverage beyond the `User` module
- Add Actuator/Micrometer/Prometheus/Grafana observability
