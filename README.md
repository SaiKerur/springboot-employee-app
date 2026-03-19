# Employee Management API

A Spring Boot REST API for managing employees with validation, structured responses, and global exception handling.

## Tech Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Jakarta Validation
- Lombok
- Maven

## Features

- Create employee with request validation
- Get all employees
- Real-time operations with work logs, live notifications, and presence status
- Consistent API response format using `ResponseEntity` + `ApiResponse`
- Global exception handling
- Duplicate email protection
- Transactional service layer (`@Transactional`)

## Project Structure

```text
src/main/java/com/example/demo
|- controller
|- dto
|- exception
|- model
|- repository
|- service
```

## Getting Started

### Prerequisites

- JDK 17+
- Maven 3.9+

### Run the Application

```bash
mvn spring-boot:run
```

Application runs on:

- `http://localhost:8080`

### MySQL Configuration

- URL: `jdbc:mysql://localhost:3306/employee_management?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`
- Username: `*****`
- Password: `**************`

Make sure MySQL server is running locally on port `3306`.

## API Endpoints

### 1) Create Employee

- **Method:** `POST`
- **URL:** `/employee`
- **Request Body:**

```json
{
  "name": "Virat",
  "email": "virat@gmail.com",
  "salary": 5000
}
```

- **Success Response (201):**

```json
{
  "timestamp": "2026-03-04T20:00:00",
  "status": 201,
  "message": "Employee created successfully",
  "data": {
    "id": 1,
    "name": "Virat",
    "email": "virat@gmail.com",
    "salary": 5000.0
  }
}
```

### 2) Get All Employees

- **Method:** `GET`
- **URL:** `/employee`

- **Success Response (200):**

```json
{
  "timestamp": "2026-03-04T20:01:00",
  "status": 200,
  "message": "Employees fetched successfully",
  "data": [
    {
      "id": 1,
      "name": "Virat",
      "email": "virat@gmail.com",
      "salary": 5000.0
    }
  ]
}
```

### 3) Work Log CRUD

- `POST /work-log`
- `GET /work-log`
- `GET /work-log/{id}`
- `PUT /work-log/{id}`
- `DELETE /work-log/{id}?confirm=true`

### 4) Live Notification CRUD

- `POST /live-notification`
- `GET /live-notification`
- `GET /live-notification/{id}`
- `PUT /live-notification/{id}`
- `DELETE /live-notification/{id}?confirm=true`

### 5) Presence Status CRUD

- `POST /presence-status`
- `GET /presence-status`
- `GET /presence-status/{id}`
- `PUT /presence-status/{id}`
- `DELETE /presence-status/{id}?confirm=true`

## Validation Rules

For `EmployeeRequestDTO`:

- `name`: cannot be blank
- `email`: must be valid and cannot be blank
- `salary`: cannot be null and must be at least `100`

## Error Responses

Errors are also returned in a consistent structure:

```json
{
  "timestamp": "2026-03-04T20:02:00",
  "status": 400,
  "message": "Validation failed",
  "data": {
    "salary": "Salary must be greater than 99"
  }
}
```

Duplicate email response:

- **Status:** `409 CONFLICT`
- **Message:** `Resource already exists`

## Build

```bash
mvn clean install
```

## Author

- Sai Kerur
