# 🏆 Eureka

> A competitive web platform where participants race through challenges and accumulate points.

---

## 📋 Table of Contents

- [About](#about)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Contributing](#contributing)

---

## About

**Questions Project** is a Spring Boot web application designed for competitive learning. Participants join task "races" — fast-paced, challenge-style events — and earn points based on their performance. The platform tracks scores and rankings, making it ideal for educational competitions or team-based quiz sessions.

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| **Java** | 25 | Core language |
| **Spring Boot** | 4.0.3 | Application framework |
| **Spring Web MVC** | — | REST & web layer |
| **Spring Data JPA** | — | ORM & data access |
| **Thymeleaf** | — | Server-side HTML templating |
| **Flyway** | — | Database schema migrations |
| **PostgreSQL** | — | Relational database |
| **Lombok** | — | Boilerplate reduction |
| **Maven** | — | Build & dependency management |

---

## Prerequisites

Before running the project, make sure you have the following installed:

- [JDK 25+](https://openjdk.org/)
- [Maven 3.9+](https://maven.apache.org/)
- [PostgreSQL 14+](https://www.postgresql.org/)

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/araujofs/questions-project.git
cd questions-project
```

### 2. Configure the database

Create a PostgreSQL database and update `src/main/resources/application.yml` with your credentials:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/questions
    username: your_username
    password: your_password
```

### 3. Build and run

```bash
./mvnw spring-boot:run
```

The application will start on [http://localhost:8080](http://localhost:8080).

### 4. Running tests

```bash
./mvnw test
```

---

## Project Structure

```
src/
├── main/
│   ├── java/br/com/pweb2/questions/
│   │   ├── QuestionsApplication.java   # Application entry point
│   │   ├── controller/                 # Web & REST controllers
│   │   ├── model/                      # JPA entities
│   │   ├── repository/                 # Data access layer
│   │   └── service/                    # Business logic
│   └── resources/
│       ├── db/migration/               # Flyway SQL migrations
│       └── templates/                  # Thymeleaf HTML templates
└── test/
    └── java/br/com/pweb2/questions/    # Unit & integration tests
```

---

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/my-feature`)
3. Commit your changes (`git commit -m 'Add my feature'`)
4. Push to the branch (`git push origin feature/my-feature`)
5. Open a Pull Request
