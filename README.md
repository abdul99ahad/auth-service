# Auth Service

A robust microservice-based authentication system built with Spring Boot, featuring JWT token management, role-based access control, and event-driven architecture using Kafka.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Event-Driven Architecture](#event-driven-architecture)
- [Security](#security)
- [Exception Handling](#exception-handling)
- [Contributing](#contributing)
- [License](#license)

## Overview

Auth Service is a MVP authentication and authorization microservice designed to handle user registration, login, token management, and role-based access control. It implements JWT-based authentication with refresh token support and publishes user creation events to Kafka for downstream services.

## Features

- **User Authentication**
    - User registration (signup)
    - User login with JWT token generation
    - Access token and refresh token management
    - Token validation and refresh mechanism

- **Authorization**
    - Role-based access control (RBAC)
    - Custom user details implementation
    - JWT-based request filtering

- **Event-Driven Architecture**
    - Kafka integration for publishing user creation events
    - Asynchronous event processing
    - Decoupled service communication

- **Security**
    - Password encryption
    - JWT token-based authentication
    - Secure endpoints with Spring Security
    - Custom authentication filters

- **Exception Handling**
    - Global exception handler
    - Custom exception types for specific error scenarios
    - Standardized API error responses

## Architecture

This service follows a layered architecture pattern:

```
┌─────────────────────────────────────┐
│         Controllers Layer           │
│   (AuthController, TestController)  │
└─────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────┐
│          Services Layer             │
│  (JwtService, TokenService, etc.)   │
└─────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────┐
│        Repositories Layer           │
│  (UserRepository, TokenRepository)  │
└─────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────┐
│           Database Layer            │
└─────────────────────────────────────┘

          ↓ (Events) ↓
┌─────────────────────────────────────┐
│         Kafka Message Bus           │
└─────────────────────────────────────┘
```

## Technology Stack

- **Framework**: Spring Boot
- **Language**: Java
- **Security**: Spring Security
- **Authentication**: JWT (JSON Web Tokens)
- **Message Broker**: Apache Kafka
- **Database**: JPA/Hibernate (configurable)
- **Build Tool**: Gradle
- **Architecture**: Microservices

## Project Structure

```
src/main/java/
└── org.dev/
    ├── adapters/           # Custom user details for Spring Security
    ├── configs/            # Security and application configuration
    ├── controllers/        # REST API endpoints
    ├── entities/           # JPA entities (User, Roles, RefreshToken)
    ├── exceptions/         # Custom exceptions and global exception handler
    ├── filters/            # JWT authentication filter
    ├── kafka/              # Kafka configuration, events, and serializers
    ├── repositories/       # Data access layer
    ├── request/            # Request DTOs
    ├── response/           # Response DTOs
    └── services/           # Business logic layer
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle
- Apache Kafka
- Database (PostgreSQL/MySQL/etc.)
- IDE (IntelliJ IDEA recommended)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/abdul99ahad/auth-service.git
   cd auth-service
   ```

2. **Configure the application**

   Update `application.properties` or `application.yml` with your database and Kafka configurations:
   ```properties
   # Database Configuration
   spring.datasource.url=jdbc:postgresql://localhost:5432/authdb
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   
   # JWT Configuration
   jwt.secret=your_secret_key
   jwt.expiration=86400000
   jwt.refresh.expiration=604800000
   
   # Kafka Configuration
   spring.kafka.bootstrap-servers=localhost:9092
   ```

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

The service will start on `http://localhost:8080` (or your configured port).

## API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/auth/signup` | Register a new user | No |
| POST | `/auth/login` | Login and receive tokens | No |
| POST | `/auth/refresh` | Refresh access token | No |

**Request/Response Examples:**

```http
# Sign Up
POST /auth/signup
{ "username": "user@example.com", "password": "securePassword123", "roles": ["USER"] }

# Login
POST /auth/login
{ "username": "user@example.com", "password": "securePassword123" }

# Refresh Token
POST /auth/refresh
{ "refreshToken": "your_refresh_token" }
```

**Protected Endpoints:** Include `Authorization: Bearer <access_token>` header

## Configuration

### Security Configuration

The `SecurityConfig` class handles:
- Password encoding with BCrypt
- CORS configuration
- CSRF protection
- JWT filter chain
- Endpoint security rules

### User Configuration

The `UserConfig` class manages:
- User creation and updates
- Role assignment
- User validation

### Kafka Configuration

The `KafkaConfig` class configures:
- Producer settings
- Serialization strategies
- Topic management
- Bootstrap servers

## Event-Driven Architecture

### User Created Event

When a new user registers, the service publishes a `UserCreatedEvent` to Kafka:

```java
UserCreatedEvent {
  userId: String
  username: String
  email: String
  roles: List<String>
  createdAt: Timestamp
}
```

This event can be consumed by other microservices for:
- Welcome email notifications
- User profile initialization
- Analytics and logging
- Audit trail creation

## Security

### JWT Authentication Flow

1. User submits credentials to `/auth/login`
2. Service validates credentials
3. On success, generates JWT access token and refresh token
4. Client stores tokens securely
5. Client includes access token in Authorization header for protected requests
6. `JwtAuthFilter` validates token on each request
7. When access token expires, client uses refresh token to get new tokens

### Password Security

- Passwords are encrypted using BCrypt
- Strong password policies can be enforced
- No plain text password storage

### Token Management

- **Access Token**: Short-lived (default 24 hours)
- **Refresh Token**: Long-lived (default 7 days)
- Refresh tokens are stored in database for validation
- Invalid or expired tokens are rejected

## ⚠️ Exception Handling

The service implements comprehensive exception handling:

### Custom Exceptions

- `InvalidCredentialsException`: Thrown when login credentials are incorrect
- `InvalidRefreshTokenException`: Thrown when refresh token is invalid or expired
- `UserAlreadyExistsException`: Thrown when attempting to register with existing username

### Global Exception Handler

The `GlobalExceptionHandler` catches all exceptions and returns standardized error responses:

```json
{
  "status": "error",
  "message": "User already exists",
  "timestamp": "2024-12-01T10:30:00Z",
  "path": "/auth/signup"
}
```

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Contact

Abdul Ahad - [@abdul99ahad](https://github.com/abdul99ahad)

Project Link: [https://github.com/abdul99ahad/auth-service](https://github.com/abdul99ahad/auth-service)

## Acknowledgments

- Spring Boot Documentation
- Spring Security Documentation
- Apache Kafka Documentation
- JWT.io for JWT resources