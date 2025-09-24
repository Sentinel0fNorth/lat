# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

LAT is a Spring Boot REST API application for attendance tracking. It manages students and their attendance records with a PostgreSQL database backend and token-based authentication.

## Architecture

The application follows a standard Spring Boot layered architecture:

- **Controllers** (`controller/`): REST endpoints for students and attendance management
- **Services** (`service/`): Business logic layer 
- **Repositories** (`repository/`): JPA data access layer using Spring Data
- **Models** (`model/`): JPA entities (Student, AttendanceRecord)
- **Config** (`config/`): Custom authentication filter for token validation

### Key Domain Models
- **Student**: Basic student information (name, roll number)
- **AttendanceRecord**: Links students to attendance status (PRESENT/ABSENT) by date

### Authentication
Uses a simple token-based authentication via `AuthFilter` that validates Bearer tokens against `app.auth.token` property.

## Development Commands

### Building and Running
```bash
# Build the application
./mvnw clean compile

# Run tests
./mvnw test

# Run the application
./mvnw spring-boot:run

# Package as JAR
./mvnw clean package
```

### Testing
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=LatApplicationTests

# Run tests with coverage
./mvnw test jacoco:report
```

### Database Setup
The application expects a PostgreSQL database:
- Database: `attendance_db`
- Default credentials: `postgres/12345678`
- Port: `5432`
- JPA will auto-create/update tables based on entities

### API Usage
All endpoints require `Authorization: Bearer demo-token-123` header.

**Student endpoints:**
- GET `/students` - List all students
- POST `/students` - Create student

**Attendance endpoints:** 
- POST `/attendance` - Mark attendance
- GET `/attendance?date=YYYY-MM-DD` - Get attendance by date

## Development Notes

### Configuration
- Main config in `application.properties`
- Uses Spring Security with custom authentication filter
- PostgreSQL dialect configured for JPA
- Lombok used for reducing boilerplate code

### Database Schema
- Auto-generated via JPA annotations
- `ddl-auto=update` will modify schema on startup
- SQL queries logged when `show-sql=true`

### Technology Stack
- Spring Boot 3.5.6
- Java 21
- PostgreSQL driver
- Spring Data JPA
- Spring Security
- Lombok
- JUnit 5 for testing

### Code Patterns
- Uses `@Autowired` for dependency injection
- Lombok `@Data` for entity classes
- Standard Spring MVC controller patterns
- JpaRepository for data access
- Enum for attendance status