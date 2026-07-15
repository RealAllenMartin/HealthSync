# HealthSync

HealthSync is a backend REST API built with Java and Spring Boot. The project is designed to model a healthcare-style system where authenticated users can securely access patient-related data.

This project is being built incrementally to demonstrate backend engineering skills, clean architecture, API security, validation, exception handling, DTO-based design, and automated testing.

## Current Features

- Patient CRUD API foundation
- PostgreSQL database integration
- DTO-based request and response models
- Request validation with clear error responses
- Global exception handling
- User registration and login
- Password hashing with BCrypt
- JWT-based authentication
- Protected patient endpoints
- Swagger/OpenAPI support
- Automated unit and web-layer tests

## Tech Stack

- Java
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Maven
- JUnit
- Mockito
- MockMvc
- Git/GitHub

## Project Structure

```text
com.example.healthsync
├── controller
├── service
├── repository
├── entity
├── dto
├── security
├── exception
└── mapper
```

## API Areas

### Authentication

The authentication API allows users to register and log in.

- `POST /auth/register`
- `POST /auth/login`

After logging in successfully, the API returns a JWT. This token is required when accessing protected endpoints.

### Patients

The patient API allows authenticated users to manage patient data.

- `GET /patients`
- `GET /patients/{id}`
- `POST /patients`

Patient endpoints are protected and require a valid JWT in the request header.

## Security

HealthSync uses Spring Security with JWT authentication.

Users can register and log in through the authentication endpoints. After a successful login, the API returns a JWT. The client must send this token with protected requests using the `Authorization` header.

```text
Authorization: Bearer <token>
```

Current security behavior:

- `/auth/register` is public
- `/auth/login` is public
- Swagger endpoints are public
- patient endpoints are protected
- passwords are hashed with BCrypt before being stored
- JWT tokens are validated before protected routes are accessed

## Testing

The project includes automated tests for core backend behavior.

Current test coverage includes:

- authentication service logic
- patient service logic
- patient DTO mapping
- request validation
- controller behavior
- JWT token generation and validation
- protected and public route security

Run the full test suite with:

```bash
./mvnw test
```

Current test count:

```text
24 passing tests
```

## How to Run

Start the application with:

```bash
./mvnw spring-boot:run
```

By default, the API runs at:

```text
http://localhost:8080
```

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

## Project Status

HealthSync is currently completing Phase 3.

Completed:

- project setup
- PostgreSQL database connection
- patient entity and repository
- service layer
- REST controller layer
- request validation
- global exception handling
- DTO-based API structure
- user registration and login
- BCrypt password hashing
- JWT authentication
- protected API routes
- initial automated test coverage

Remaining Phase 3 cleanup:

- perform final cleanup review
- commit final Phase 3 changes

## Roadmap

Planned future improvements include:

- appointment management
- doctor entity and doctor profiles
- medical records
- pagination and sorting
- filtering and search
- API versioning
- Docker support
- GitHub Actions CI/CD
- cloud deployment preparation
- frontend integration

