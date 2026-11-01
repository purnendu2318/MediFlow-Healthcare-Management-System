# MediFlow — Healthcare Management System

![Java 21](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Java Swing](https://img.shields.io/badge/UI-Java%20Swing-blue)
![MySQL](https://img.shields.io/badge/Database-MySQL%208-4479A1?logo=mysql)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)
[![Build](https://github.com/purnendu2318/MediFlow-Healthcare-Management-System/actions/workflows/build.yml/badge.svg)](https://github.com/purnendu2318/MediFlow-Healthcare-Management-System/actions/workflows/build.yml)

A Java desktop application prototype for hospital workflows, built with **Java 21, Swing, JDBC and MySQL**.

## Project overview

MediFlow currently focuses on an authenticated desktop experience and patient record workflows. It uses a layered Java structure with UI, service, repository and database configuration packages.

### Implemented

- User sign-in using BCrypt password-hash verification.
- First-run bootstrap of a development administrator account when the users table is empty.
- Swing dashboard shell with signed-in user and role information.
- Patient registration with basic name, phone and date validation.
- Patient search/listing and soft deactivation.
- MySQL relational schema for users, departments, patients, doctors, availability, appointments, queue tokens, medical records, prescriptions, invoices, payments, notifications and audit logs.
- Maven build and GitHub Actions CI using Java 21.

### Planned (not complete workflows yet)

Doctor and department CRUD, appointment scheduling/conflict checks, queue operations, electronic medical records, prescriptions, billing/payment screens, notifications, reporting, audit-log viewer, and production-grade account administration.

> The dashboard includes placeholder navigation for several planned areas. A database table or navigation item does not mean that the corresponding workflow is implemented.

## Technology stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Desktop UI | Java Swing |
| Database | MySQL 8+ |
| Database access | JDBC / MySQL Connector/J |
| Password hashing | jBCrypt (BCrypt) |
| Build | Apache Maven |
| IDE | Visual Studio Code or any Java IDE |

## Run locally

### Requirements

- JDK 21
- Apache Maven 3.9+
- MySQL Server 8+

### 1. Clone the repository

```bash
git clone https://github.com/purnendu2318/MediFlow-Healthcare-Management-System.git
cd MediFlow-Healthcare-Management-System
```

### 2. Create the database

Run `database/schema.sql` using MySQL Workbench or the MySQL command-line client. It creates the `mediflow_hms` database and the initial set of tables.

### 3. Configure your local database connection

**Windows PowerShell**
```powershell
Copy-Item src/main/resources/db.properties.example src/main/resources/db.properties
```

**macOS / Linux**
```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

Edit `src/main/resources/db.properties` and set your local MySQL username and password. The local file is excluded by `.gitignore`; never commit real credentials.

### 4. Build and launch

```bash
mvn clean verify
mvn exec:java
```

The app checks the database connection before showing the sign-in window.

## First-run demo login

When the `users` table is empty, the application creates a local development administrator:

| Field | Value |
|---|---|
| Email | `admin@mediflow.local` |
| Password | `Admin@123` |
| Role | `SUPER_ADMIN` |

This predictable credential is for local demonstrations with synthetic data only. Change or remove it before sharing the application with anyone else. Do not use it in a production environment.

## Project structure

```text
.
├── .github/workflows/build.yml
├── database/schema.sql
├── src/main/java/com/mediflow/
│   ├── App.java
│   ├── config/Database.java
│   ├── model/
│   ├── repository/
│   ├── security/PasswordUtil.java
│   ├── service/
│   ├── ui/
│   └── util/Ui.java
├── src/main/resources/db.properties.example
├── .editorconfig
├── .gitignore
└── pom.xml
```

## Security and data-use notice

MediFlow is an **educational portfolio prototype**, not a certified clinical system. Use synthetic patient data only. It has not undergone a production security review and must not be used to store real patient records or support diagnosis, treatment, billing, or operational hospital decisions.

## Author

**Purnendu Roy**

- GitHub: [@purnendu2318](https://github.com/purnendu2318)
- Portfolio: [purnendu-portfolio-mu.vercel.app](https://purnendu-portfolio-mu.vercel.app/)
- LinkedIn: [Purnendu Roy](https://www.linkedin.com/in/purnendu-roy-591000314/)

---

*Built as a learning project to practise Java OOP, Swing UI development, JDBC, relational database design and application structure.*
