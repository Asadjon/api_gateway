# API Gateway

## 📜 Overview


The **API Gateway** is responsible for routing external client requests to internal microservices ([auth_service](https://github.com/Asadjon/auth_service.git), [balance_service](https://github.com/Asadjon/balance_service.git), and [transaction_service](https://github.com/Asadjon/transaction_service.git)). It also performs token-based authentication using custom filter and forwards user information to downstream services.

---

## 🚀 Tech Stack

- Java 21
- Spring Boot 3.5.3
- Spring Security
- Spring Cloud Gateway
- WebFlux (Reactive Web)
- Docker

---

## ⚙️ Setup Instruction
> You can view the installation manual in the [transaction-management-system](https://github.com/Asadjon/transaction-management-system/blob/master/README.md) repository.

---

## API Gateway Routes

| Service                                                                   | Path Pattern             | Filters Applied        |
|---------------------------------------------------------------------------|--------------------------|------------------------|
| [auth_service](https://github.com/Asadjon/auth_service.git)               | `/api/v1/auth/**`        | ❌ No authentication    |
| [balance_service](https://github.com/Asadjon/balance_service.git)         | `/api/v1/balance/**`     | ✅ AuthenticationFilter |
| [transaction_service](https://github.com/Asadjon/transaction_service.git) | `/api/v1/transaction/**` | ✅ AuthenticationFilter |


---

## Authentication Filter
A custom AuthenticationFilter is used to validate JWT tokens for balance and transaction requests. The filter:

- Extracts the token from the `Authorization: Bearer <token> header`.
- Validates the token via the [auth_service](https://github.com/Asadjon/auth_service.git) endpoint `/api/v1/auth/validate`.
- If valid, adds an `x-User-Id` header to the request and allows it to proceed.
- If invalid, returns a `401 UNAUTHORIZED` response.

---

## Example of Authorization Header
When calling secured endpoints, provide the token in the request header:
```html
Authorization: Bearer <your_jwt_token>
```