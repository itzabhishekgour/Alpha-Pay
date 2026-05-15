#  Alpha-Pay: Distributed Digital Wallet System

Alpha-Pay is a production-ready, microservices-based digital wallet application. Built with **Spring Boot 3.x** and **Java 21**, it features a fully dockerized environment, secure JWT authentication, and a robust CI/CD pipeline.

##  System Architecture

The application is architected as a set of decoupled microservices that communicate over a virtualized Docker network:

*   **API Gateway (8080):** Central entry point using Spring Cloud Gateway. Handles JWT validation and request routing.
*   **User Service (8081):** Manages user lifecycle, authentication, and secure profile storage.
*   **Wallet Service (8083):** Core ledger service that manages user balances.
*   **Transaction Service (8082):** Auditing service that tracks every credit/debit event across the system.

##  Tech Stack & Tools

*   **Language:** Java 21 (LTS)
*   **Framework:** Spring Boot 3.x, Spring Cloud Gateway, Spring Data JPA
*   **Security:** Spring Security, JSON Web Token (JWT)
*   **Database:** PostgreSQL (Cloud-hosted via **Neon DB**)
*   **Containerization:** Docker & Docker Compose
*   **CI/CD:** GitHub Actions (Automated build & test pipelines)

##  Local Setup Instructions

### 1. Prerequisites
- Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- Install [Git](https://git-scm.com/)

### 2. Configuration
Create a `.env` file in the root directory:
```env
DB_URL=jdbc:postgresql://your-neon-db-instance-url
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
```

### 3. Launching the System
Run the following command to build and start all services:

```bash
docker-compose up --build
```
The services will be accessible at `localhost:8080` via the Gateway.

##  API Endpoints & Testing Flow

### Step 1: User Registration
```http
POST http://localhost:8080/alphapay/user/register
```

**Payload:**
```json
{
  "fullName": "John",
  "email": "john@test.com",
  "password": "securePassword123"
}
```

### Step 2: Login (Get JWT Token)
```http
POST http://localhost:8080/alphapay/user/login
```
*(Copy the generated token for subsequent requests)*

### Step 3: Add Money to Wallet
```http
POST http://localhost:8080/alphapay/wallet/{userId}/add-money?amount=500
```

**Headers:**
```
Authorization: Bearer <your_jwt_token>
```

## 🔄 CI/CD Workflow

This repository is integrated with GitHub Actions. The pipeline automatically builds all 4 microservices individually on every push to the `beta` and `alpha` branches to ensure build stability.

## 🚧 Roadmap

- [ ] **Rate Limiting:** Implement request throttling at the Gateway level.
- [ ] **Saga Pattern:** Distributed transaction management for data consistency.
- [ ] **Service Discovery:** Integration with Netflix Eureka.

---

*Developed by Abhishek Gour*

*Solving digital payment complexities one microservice at a time.*
