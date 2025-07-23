# Video Metadata Service

A secure REST API developed in **Java 17** and **Spring Boot** for managing video metadata from multiple platforms (YouTube, Vimeo, internal), featuring JWT authentication and interactive Swagger documentation.

## Features

- **JWT Authentication** and role-based access control (ADMIN, USER)
- **Full CRUD** for video metadata
- **Simulated integration** with external APIs (YouTube, Vimeo, internal)
- **Advanced filtering** (by source, date, duration, title)
- **Statistics & analytics** per platform
- **Swagger/OpenAPI 3** interactive documentation
- **Robust validation** and exception handling
- **Comprehensive unit tests** with JUnit 5 and Mockito

## Technologies

- Java 17
- Spring Boot 3.x & Spring Framework
- Spring Security 6 (with JWT)
- Spring Data JPA
- H2 Database (in-memory for dev/testing)
- JUnit 5, Mockito
- Maven
- Swagger/OpenAPI (springdoc-openapi for docs)

## Project Structure

```
src/main/java/com/goldmediatech/videometadata/
├── VideometadataApplication.java
├── config/                // Security, OpenAPI configs
├── domain/                // Entities & DTOs
├── persistence/repository/ // JPA repositories
├── service/               // Business logic & integrations
├── util/                  // Mappers, JWT helper
└── web/                   // Controllers, filters, exceptions

src/main/resources/
├── application.yml
└── data.sql

src/test/java/...
```

## How to Run

### Prerequisites

- Java 17+
- Maven 3.6+
- Git

### Steps

1. **Clone the repository**
   ```bash
   git clone 
   cd video-metadata-service
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   The default port is **8080**.

## Authentication: How to Log In and Use the Token

### Default Users

| Username | Password   | Role  |
|----------|------------|-------|
| admin    | admin123   | ADMIN |
| user     | user123    | USER  |

### 1. Obtain JWT Token

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJ...",
  "type": "Bearer",
  "username": "admin",
  "role": "ROLE_ADMIN",
  "expiresAt": "2025-07-23T10:30:00.000+00:00"
}
```

### 2. Use the Access Token

Add the following HTTP header to every protected API request:
```
Authorization: Bearer 
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/videos \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJ..."
```

## 🌐 API Examples

### List Videos

```bash
curl -X GET http://localhost:8080/api/videos \
  -H "Authorization: Bearer "
```

### Get Statistics

```bash
curl -X GET http://localhost:8080/api/videos/stats \
  -H "Authorization: Bearer "
```

## Swagger / API Documentation

- **OpenAPI/Swagger:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **H2 Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

## Design Decisions & Assumptions

- **Layered Architecture:** Service, repository, and web/controller layers are separated for clarity, testing, and maintainability.
- **JWT Authentication:** Stateless for scalability and simplicity; no server-side sessions required.
- **Role-Based Security:** Only ADMIN users can create, update, or delete videos; USERs have read access.
- **Video Import Simulation:** Instead of calling real external APIs, simulated video entries are created for demonstration and speed.
- **DTO Strategy:** All API input and output use DTOs for security and flexibility.
- **Validation & Exception Handling:** Bean Validation and global exception handlers safeguard against invalid data.
- **Unit Testing Practice:** Key classes like `VideoService` and `VideoController` are covered by unit tests using JUnit 5 and Mockito.

## Example Postman Collection (How to test)

You can import the following request set into Postman or Insomnia:

**Step 1:** Send `POST /api/auth/login` and copy the token value.  
**Step 2:** Use the value for future requests in the `Authorization` header as `Bearer `.  
**Step 3:** Test endpoints like `/api/videos`, `/api/videos/import`, etc., with the token.

## How to Run Tests

```bash
mvn test
```
All unit tests for services and controllers will run automatically.
