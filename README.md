# ⚡ Task Manager Workspace

https://taask-manager-production.up.railway.app/
A modern, full-stack Task Management application built using **Spring Boot**, **Spring Security**, **JWT Authentication**, and **Vanilla JavaScript**. 

The system provides a decoupled architecture with strict **Role-Based Access Control (RBAC)**, separating System Administration from Standard User task management. It features a two-column Kanban board workspace, live priority/status patching, and cascade handling for user deletion safeguards.

---

## ✨ Features

### 👤 Standard User Workspace
* **Interactive Kanban Board:** Separate boards for **Active Tasks** vs. **Completed Tasks**.
* **Real-time Task Controls:** Change status (`PENDING`, `IN_PROGRESS`, `COMPLETED`) or priority (`LOW`, `MEDIUM`, `HIGH`) on the fly via inline dropdowns.
* **Auto-Archiving:** Marking a task as `COMPLETED` automatically locks editing and moves it to the Completed column with a visual strikethrough.
* **Re-open Capabilities:** Move completed items back to active workflows with a single click.
* **Secure Task Ownership:** Users can only view, edit, or delete their own tasks.

### 👑 Admin Control Panel
* **System Monitoring:** View all registered users across the platform.
* **User Inspection Modal:** View tasks belonging to any specific user ID.
* **Role Management:** Dynamically elevate standard users to `ROLE_ADMIN` or downgrade to `ROLE_USER`.
* **User Management:** Delete user accounts with automated cascade cleanup of their associated tasks.
* **Self-Deletion Safeguard:** Built-in protection preventing admins from deleting their own active account.

### 🔐 Security & Architecture
* **Stateless Authentication:** Secured via JSON Web Tokens (JWT).
* **Cascade Delete Rules:** Clean removal of orphaned tasks using JPA `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)`.
* **CORS & CSRF:** Pre-configured security filter chain allowing stateless API communication.

---

## 🛠️ Tech Stack

### Backend
* **Language:** Java 17+
* **Framework:** Spring Boot 3.x
* **Security:** Spring Security, JWT (io.jsonwebtoken)
* **ORM / Database:** Spring Data JPA, Hibernate, MySQL / H2
* **Tooling:** Lombok, Jakarta Validation

### Frontend
* **UI:** HTML5, Modern CSS3 (CSS Variables, Flexbox, Grid)
* **Logic:** Vanilla JavaScript (Fetch API, Async/Await, JWT Parsing)
* **Design:** Dark-mode Glassmorphism Workspace Layout

---

## 🚀 Getting Started

### Prerequisites
* **JDK 17** or higher
* **Maven 3.8+**
* **MySQL Database** (or configured H2 in-memory DB)

### 1. Database Setup
Configure your MySQL database settings in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskmanager_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Secret Key
jwt.secret=YourSuperSecretKeyForJWTTokenGeneration32BytesLong!
jwt.expiration=86400000
