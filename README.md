# Support Ticket System

A RESTful backend API for managing technical support tickets built with Spring Boot, Spring Security (JWT), and MySQL.

---

## Project Description

The Support Ticket System allows users to submit support tickets, managers to handle and respond to them, and admins to manage the entire system. Authentication is handled via JWT tokens with role-based access control.


---

## Tech Stack

| Technology | Version |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.3.5 |
| Spring Security | 6.x |
| Spring Data JPA | 3.x |
| MySQL | 8.x |
| JWT (jjwt) | 0.11.5 |
| JUnit 5 + Mockito | (via spring-boot-starter-test) |

---

## Database Tables

| Table | Description |
|-------|-------------|
| `users` | Registered users with roles (ADMIN, MANAGER, USER) |
| `ticket` | Support tickets created by users |
| `response` | Manager responses linked to tickets |

### Relationships
- `ticket.user_id` → `users.id` (Many-to-One)
- `response.ticket_id` → `ticket.id` (Many-to-One)
- `response.manager_id` → `users.id` (Many-to-One)

---

## Roles & Permissions

| Endpoint | ANONYMOUS | USER | MANAGER | ADMIN |
|----------|-----------|------|---------|-------|
| POST /auth/register | ✅ | ✅ | ✅ | ✅ |
| POST /auth/login | ✅ | ✅ | ✅ | ✅ |
| POST /tickets | ❌ | ✅ | ✅ | ✅ |
| GET /tickets | ❌ | ✅ | ✅ | ✅ |
| GET /tickets/my | ❌ | ✅ | ✅ | ✅ |
| GET /tickets/search | ❌ | ✅ | ✅ | ✅ |
| PUT /tickets/{id}/status | ❌ | ❌ | ✅ | ✅ |
| DELETE /tickets/{id} | ❌ | ❌ | ❌ | ✅ |
| POST /responses/{ticketId} | ❌ | ❌ | ✅ | ✅ |
| GET /responses/{ticketId} | ❌ | ✅ | ✅ | ✅ |
| GET /admin/users | ❌ | ❌ | ❌ | ✅ |
| PUT /admin/users/{id}/role | ❌ | ❌ | ❌ | ✅ |
| DELETE /admin/users/{id} | ❌ | ❌ | ❌ | ✅ |

---

## API List

### Auth
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | /auth/register | Register new user | Public |
| POST | /auth/login | Login, returns JWT token | Public |

### Tickets
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | /tickets | Create new ticket | USER+ |
| GET | /tickets | Get all tickets | USER+ |
| GET | /tickets/my | Get current user's tickets | USER+ |
| GET | /tickets/search?keyword= | Search tickets by title | USER+ |
| PUT | /tickets/{id}/status | Update ticket status | MANAGER+ |
| DELETE | /tickets/{id} | Delete ticket | ADMIN |

### Responses
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | /responses/{ticketId} | Add response to ticket | MANAGER+ |
| GET | /responses/{ticketId} | Get responses for ticket | USER+ |

### Admin
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | /admin/users | Get all users | ADMIN |
| PUT | /admin/users/{id}/role | Change user role | ADMIN |
| DELETE | /admin/users/{id} | Delete user | ADMIN |

---

## Project Structure

```
src/
├── main/java/com/example/SupportTicketSystem/
│   ├── config/
│   │   └── SecurityConfig.java          # JWT filter chain, role-based rules
│   ├── controller/
│   │   ├── AuthController.java          # Register & Login
│   │   ├── TicketController.java        # Ticket CRUD
│   │   ├── ResponseController.java      # Ticket responses
│   │   └── AdminController.java         # User management
│   ├── service/
│   │   ├── UserService.java
│   │   ├── TicketService.java
│   │   └── ResponseService.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── TicketRepository.java
│   │   └── ResponseRepository.java
│   ├── entity/
│   │   ├── User.java
│   │   ├── Ticket.java
│   │   ├── Response.java
│   │   ├── Role.java                    
│   │   └── Status.java                  
│   ├── dto/
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   └── UpdateStatusRequest.java
│   └── security/
│       ├── jwt/
│       │   ├── JwtUtil.java           
│       │   └── JwtFilter.java        
│       └── userdetails/
│           └── CustomUserDetails.java
└── test/java/com/example/SupportTicketSystem/
    └── service/
        └── TicketServiceTest.java      
```

---

## Setup Instructions

### Prerequisites
- Java 21+
- Maven 3.8+
- MySQL 8.x

### 1. Clone the project
```bash
git clone <repository-url>
cd SupportTicketSystem
```

### 2. Create MySQL database
```sql
CREATE DATABASE support_ticket_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configure application.properties
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/support_ticket_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### 4. Run the application
```bash
mvn spring-boot:run
```
The server starts at `http://localhost:8080`

### 5. Create test users
Register via API, then set roles in MySQL:
```bash
# Register users via POST /auth/register first, then:
```
```sql
UPDATE users SET role = 'ROLE_ADMIN'   WHERE username = 'admin';
UPDATE users SET role = 'ROLE_MANAGER' WHERE username = 'manager';
-- user role is set automatically on register
```

### 6. Run tests
```bash
mvn test
```

---

## Usage Example

**Register:**
```http
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "username": "alice",
  "password": "pass123"
}
```

**Login:**
```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "username": "alice",
  "password": "pass123"
}
```
Response: `{ "token": "eyJ..." }`

**Create ticket (with token):**
```http
POST http://localhost:8080/tickets
Authorization: Bearer eyJ...
Content-Type: application/json

{
  "title": "Login button not working",
  "description": "Clicking the button does nothing"
}
```

**Update status (manager):**
```http
PUT http://localhost:8080/tickets/1/status
Authorization: Bearer eyJ...
Content-Type: application/json

{
  "status": "IN_PROGRESS"
}
```
