# 🎬 RentVideo — Video Rental REST API

A RESTful API built with **Spring Boot 3**, **Spring Security (Basic Auth)**, **Spring Data JPA**, and **MySQL** for managing an online video rental system.

---

## 🛠️ Tech Stack

| Layer          | Technology                  |
|----------------|-----------------------------|
| Framework      | Spring Boot 3.2             |
| Security       | Spring Security (Basic Auth) |
| Persistence    | Spring Data JPA / Hibernate |
| Database       | MySQL 8                     |
| Password Hash  | BCrypt                      |
| Build Tool     | Maven                       |
| Java Version   | Java 17                     |

---

## ⚙️ Setup & Configuration

### 1. Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8+ running locally

### 2. Database Setup

```sql
CREATE DATABASE rentvideo_db;
```

### 3. Configure `application.properties`

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rentvideo_db?createDatabaseIfNotExist=true
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## 📌 API Endpoints

### Auth (Public)

| Method | Endpoint              | Description          | Auth Required |
|--------|-----------------------|----------------------|---------------|
| POST   | `/api/auth/register`  | Register a new user  | No            |
| POST   | `/api/auth/login`     | Login (Basic Auth)   | Yes (Basic)   |

### Videos

| Method | Endpoint           | Description                      | Role Required  |
|--------|--------------------|----------------------------------|----------------|
| GET    | `/api/videos`      | List all available videos        | Any Auth User  |
| GET    | `/api/videos/{id}` | Get a single video by ID         | Any Auth User  |
| GET    | `/api/videos/all`  | List all videos (incl. unavail.) | ADMIN          |
| POST   | `/api/videos`      | Create a new video               | ADMIN          |
| PUT    | `/api/videos/{id}` | Update an existing video         | ADMIN          |
| DELETE | `/api/videos/{id}` | Delete a video                   | ADMIN          |

---

## 📦 Request / Response Examples

### Register a User

**POST** `/api/auth/register`

```json
{
  "email": "alice@example.com",
  "password": "securepass",
  "firstName": "Alice",
  "lastName": "Smith",
  "role": "CUSTOMER"
}
```

> `role` is optional — defaults to `CUSTOMER`.

**Response 201:**
```json
{
  "id": 1,
  "email": "alice@example.com",
  "firstName": "Alice",
  "lastName": "Smith",
  "role": "CUSTOMER"
}
```

---

### Login

**POST** `/api/auth/login`  
Use **Basic Auth** header: `Authorization: Basic base64(email:password)`

**Response 200:**
```json
{
  "message": "Login successful",
  "email": "alice@example.com",
  "role": "ROLE_CUSTOMER"
}
```

---

### Create a Video (ADMIN only)

**POST** `/api/videos`

```json
{
  "title": "Inception",
  "director": "Christopher Nolan",
  "genre": "Sci-Fi",
  "available": true
}
```

**Response 201:**
```json
{
  "id": 1,
  "title": "Inception",
  "director": "Christopher Nolan",
  "genre": "Sci-Fi",
  "available": true
}
```

---

## 🔐 Authentication & Authorization

- Authentication uses **HTTP Basic Auth** (email + password).
- Passwords are hashed with **BCrypt**.
- Two roles exist: `CUSTOMER` and `ADMIN`.
- Public endpoints: `/api/auth/register`, `/api/auth/login`
- All other endpoints require authentication.
- Video management (POST/PUT/DELETE) is restricted to `ADMIN` role.

---

## ❌ Error Handling

| HTTP Status | Scenario                         |
|-------------|----------------------------------|
| 400         | Validation error (missing fields)|
| 401         | Unauthenticated request          |
| 403         | Forbidden (wrong role)           |
| 404         | Video/User not found             |
| 409         | Email already registered         |
| 500         | Unexpected server error          |

---

## 🧪 Running Tests

```bash
./mvnw test
```

Tests use an in-memory **H2** database and **Mockito** — no MySQL required.

---

## 📁 Project Structure

```
src/
├── main/java/com/rentvideo/
│   ├── config/          # Security & UserDetailsService
│   ├── controller/      # REST controllers
│   ├── dto/             # Request/Response DTOs
│   ├── entity/          # JPA entities (User, Video)
│   ├── exception/       # Custom exceptions & global handler
│   ├── repository/      # Spring Data JPA repositories
│   └── service/         # Business logic
└── test/java/com/rentvideo/
    └── service/         # Unit tests (UserService, VideoService)
```
