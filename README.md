# 🎬 RentVideo — Video Rental REST API

A RESTful API built with **Spring Boot 3**, **Spring Security (JWT)**, **Spring Data JPA**, and **MySQL** for managing an online video rental system.

---

## 🛠️ Tech Stack

| Layer          | Technology                  |
|----------------|-----------------------------|
| Framework      | Spring Boot 3.2             |
| Security       | Spring Security (JWT)       |
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
CREATE DATABASE rentvideo;
```

### 3. Configure `application.yml`

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rentvideo
    username: YOUR_USERNAME
    password: YOUR_PASSWORD
app:
  jwt:
    secret: 404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
    expiration: 86400000
```

---

## 📌 API Endpoints

### Auth (Public)

| Method | Endpoint              | Description          |
|--------|-----------------------|----------------------|
| POST   | `/api/auth/register`  | Register a new user  |
| POST   | `/api/auth/login`     | Login (Returns JWT)  |

### Videos

| Method | Endpoint           | Description                      | Role Required  |
|--------|--------------------|----------------------------------|----------------|
| GET    | `/api/videos`      | List all available videos        | Any Auth User  |
| GET    | `/api/videos/{id}` | Get a single video by ID         | Any Auth User  |
| GET    | `/api/videos/all`  | List all videos (incl. unavail.) | ADMIN          |
| POST   | `/api/videos`      | Create a new video               | ADMIN          |
| PUT    | `/api/videos/{id}` | Update an existing video         | ADMIN          |
| DELETE | `/api/videos/{id}` | Delete a video                   | ADMIN          |

### Rentals (Core Logic)

| Method | Endpoint                    | Description               | Role Required |
|--------|-----------------------------|---------------------------|---------------|
| POST   | `/api/videos/{id}/rent`     | Rent a video (Max 2)      | CUSTOMER      |
| POST   | `/api/videos/{id}/return`   | Return a rented video     | CUSTOMER      |

---

## 🔑 Key Features & Design Decisions

- **Stateless Auth**: Uses JWT for scalable, stateless authentication.
- **Max 2 Rentals**: Enforced at the service layer; users cannot rent more than 2 active videos.
- **Role-Based Security**: Clear separation between `ADMIN` (inventory management) and `CUSTOMER` (rental operations).
- **Soft Records**: `Rental` table keeps history even after videos are returned.

---

## 📁 Project Structure

```
src/
├── main/java/com/rentvideo/
│   ├── config/          # Security configuration (JWT filter chain)
│   ├── controller/      # Auth, Video, and Rental controllers
│   ├── dto/             # Request/Response DTOs
│   ├── entity/          # JPA entities (User, Video, Rental, Role)
│   ├── exception/       # Custom exceptions & global handler
│   ├── repository/      # Spring Data JPA repositories
│   ├── security/        # JwtUtil, JwtFilter, CustomUserDetailsService
│   └── service/         # Business logic (RentalService, VideoService)
```
