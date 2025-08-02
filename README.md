# ⚡ API Rate Limiter using Spring Boot, Redis & JWT

A robust **Rate Limiter API** built with **Spring Boot**, secured using **JWT tokens**, and backed by **Redis** for high-performance request tracking. Perfect for managing API abuse and scaling real-world backend systems.

![Java](https://img.shields.io/badge/Java-17-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring--Boot-3.0-green.svg)
![Redis](https://img.shields.io/badge/Redis-In--Memory-red)
![JWT](https://img.shields.io/badge/Auth-JWT-yellow)
![License](https://img.shields.io/badge/MIT-License-blue)

---

## 🔧 Features

- ✅ JWT-based Authentication
- ✅ Token generation endpoint
- ✅ User-specific rate limiting
- ✅ Redis-backed request tracking
- ✅ Docker/WSL Redis compatibility
- ✅ Swagger (OpenAPI) documentation

---

## 🛠️ Tech Stack

| Layer       | Tech                |
|-------------|---------------------|
| Language    | Java 17             |
| Framework   | Spring Boot 3.x     |
| Auth        | JWT (jjwt)          |
| Cache Store | Redis (via Docker/WSL) |
| Docs        | SpringDoc Swagger UI |
| Dev Tools   | VS Code / IntelliJ  |
| Build Tool  | Maven               |

---

## 🚀 How to Run Locally

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/yourusername/springboot-api-rate-limiter.git
cd springboot-api-rate-limiter
```

### 2️⃣ Install Dependencies

```bash
./mvnw clean install
```

### 3️⃣ Start Redis

> Option 1: Docker (recommended)
```bash
docker run --name redis-rate-limiter -p 6379:6379 -d redis
```

> Option 2: Memurai (for Windows WSL)
- Download: [https://www.memurai.com](https://www.memurai.com)
- Start the Redis server from your system tray.

### 4️⃣ Run the Spring Boot App

```bash
./mvnw spring-boot:run
```

> App will run at: `http://localhost:8080`

---

## 🔐 Authentication Flow

### ✅ Generate a JWT Token

`POST /auth?username=yourName`

**Example Response:**
```json
"eyJhbGciOiJIUzI1NiJ9..."
```

---

### 🧪 Access Protected Resource

`GET /api/data`

**Headers:**
```
Authorization: Bearer YOUR_JWT_TOKEN
```

**Response:**
- `200 OK` → "Data fetched successfully"
- `429 Too Many Requests` → "Rate limit exceeded"

---

## 📦 Redis Usage

- Redis stores request counts per user (key = username).
- Limits requests to _X_ per Y seconds (customizable in `RateLimiterService`).
- Keys are automatically expired after the time window.

---

## 📘 API Documentation

Visit: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 📂 Project Structure

```
├── src
│   ├── controller/RateLimiterController.java
│   ├── security/JwtUtil.java
│   ├── service/RateLimiterService.java
│   └── config/RedisConfig.java
├── application.properties
├── pom.xml
└── README.md
```

---

## 👨‍💻 Author

**Kapil Dev**  
🚀 Backend Developer | Spring Boot | Redis | System Design | DSA  
🔗 [GitHub](https://github.com/KapilXDev) | [LinkedIn](https://www.linkedin.com/in/kapil-dev-6982ba144)

---

## 📝 License

This project is licensed under the [MIT License](LICENSE).

---

> Made with ❤️ in Java & Redis — To prevent API abuse and scale backend systems 🚀
