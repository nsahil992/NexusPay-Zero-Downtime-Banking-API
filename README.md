# NexusPay - Zero Downtime Banking API

A production-style Spring Boot payment transaction system built to learn and implement real-world DevOps practices.
The application allows users to:

- Send Money
- Receive Money

---

# Features

- Spring Boot REST API
- PostgreSQL database persistence
- Dockerized Application
- Docker Compose Multi-Container Setup
- Migration Container
- API versioning (`/api/v1`)
- Healthcheck Endpoint
- Version Endpoint
- Chaos Testing Endpoint
- Clean layered architecture
- Unit tests for API endpoints

---

# DevOps Features

- Multi-stage Docker build
- Docker Compose development environment
- Makefile automation
- GitHub Actions CI pipeline
- Kubernetes deployment
- Helm chart packaging
- ArgoCD CD pipeline
- Prometheus metrics scraping
- Grafana metrics visualization
- Loki Log Aggregation

---

# Deployment Stack

```
Spring Boot
      │
Docker
      │
Docker Compose
      │
GitHub Actions CI/CD
      │
Kubernetes
      │
Helm Chart
      │
ArgoCD deployment
      │
Prometheus, Grafana, Loki
```

--- 

# API Endpoints

## 1 Transfer Money

**POST** `/api/v1/transfer`

Request

```json
{
  "from": "ACC1001",
  "to": "ACC1002",
  "amount": 500
}
```
Response

```json
Transfer Successful
```

---

## 2 Health Check Endpoint

**GET** `/api/v1/healthcheck`

Response

```json
UP!
```

---

## 3 Version Endpoint

**GET** `/api/v1/version`

Response

```json
{
  "version": "v1.0.0",
  "status": "stable"
}
```

---

## 4 Chaos Endpoint

**POST** `/api/v1/admin/fail`

---

---

# Environment Configuration

Configuration follows **Twelve-Factor App principles**.

All sensitive configuration is injected via **environment variables**.

Example variables

.env
```
DB_HOST=localhost/host.docker.internal/database
DB_PORT=5432
DB_NAME=nexuspay
DB_USERNAME=postgres
DB_PASSWORD=your_password
DB_URL=jdbc:postgresql://localhost/host.docker.internal/database:5432/nexuspay
PORT=8081
VERSION=your_version

Note: Choose the DB_HOST and DB_URL as per the development stage
```

These variables can be provided through:

- `.env` files
- Docker environment variables
- Kubernetes ConfigMaps
- Kubernetes Secrets

---

# Project Structure

```
nexuspay/
├── src/
│   ├── main/
│   │   ├── java/com/example/nexuspay/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   └── config/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/
│   │           └── V1__create_transaction_table.sql
│   │
│   └── test/
│
├── Dockerfile
├── docker-compose.yaml
├── Makefile
├── pom.xml
└── README.md
```
