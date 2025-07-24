# API Gateway

## 📜 Overview


The **API Gateway** is responsible for routing external client requests to internal microservices (`auth_service`, `balance_service`, and `transaction_service`). It also performs token-based authentication using a custom filter and forwards user information to downstream services.

---

## 🚀 Tech Stack

- Java 21
- Spring Boot 3.5.3
- Spring Security
- Spring Cloud Gateway
- WebFlux (Reactive Web)
- Docker

---

## 📦 Clone All Required Repositories
To run the full Transaction Management System, you'll need to clone each microservice repository into a common workspace folder. You can do it manually or with the following commands:

### 1. Create a project directory
   ```
   mkdir transaction-system && cd transaction-system
   ```

### > 2. Clone all required services
> * [auth_service](https://github.com/Asadjon/balance_service.git)
> * [balance_service](https://github.com/Asadjon/balance_service.git)
> * [transaction_service](https://github.com/Asadjon/transaction_service.git)
> * [api_gateway](https://github.com/Asadjon/api_gateway.git) ← this repository

---

## 🚀 Running with Docker
### 1. Create app-network (only once)
> Make sure to build and run all dependent services ([auth_service](https://github.com/Asadjon/balance_service.git), [balance_service](https://github.com/Asadjon/balance_service.git) and [transaction_service](https://github.com/Asadjon/transaction_service.git)) from the root directory.

If you haven't created the custom network yet, run:
```
docker network create app-network
```

### 2. Build and start the container
Inside the directory where your Dockerfile and docker-compose.yml are located (e.g., auth_service), run:
```
docker-compose up --build
```

### 3. Useful Docker commands
Inspect all containers connected to app-network:
```docker
docker network inspect app-network
```

Stop and remove the container(s):
```
docker-compose down
```

---

## API Gateway Routes

| Service                                                                   | Path Pattern             | Filters Applied        |
|---------------------------------------------------------------------------|--------------------------|------------------------|
| [auth_service](https://github.com/Asadjon/balance_service.git)            | `/api/v1/auth/**`        | ❌ No authentication    |
| [auth_service](https://github.com/Asadjon/balance_service.git) View       | `/auth/view/**`          | ❌ No authentication    |
| [balance_service](https://github.com/Asadjon/balance_service.git)         | `/api/v1/balance/**`     | ✅ AuthenticationFilter |
| [transaction_service](https://github.com/Asadjon/transaction_service.git) | `/api/v1/transaction/**` | ✅ AuthenticationFilter |


---

## Authentication Filter
A custom AuthenticationFilter is used to validate JWT tokens for balance and transaction requests. The filter:

- Extracts the token from the `Authorization: Bearer <token> header`.
- Validates the token via the [auth_service](https://github.com/Asadjon/balance_service.git) endpoint `/api/v1/auth/validate`.
- If valid, adds an `x-User-Id` header to the request and allows it to proceed.
- If invalid, returns a `401 UNAUTHORIZED` response.

---

## Example of Authorization Header
When calling secured endpoints, provide the token in the request header:
```html
Authorization: Bearer <your_jwt_token>
```