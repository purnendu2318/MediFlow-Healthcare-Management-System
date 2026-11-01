<div align="center">

# MediFlow — Healthcare Management System

**A Java Swing desktop application backed by MySQL, built as a learning project for core hospital workflows.**

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/UI-Java%20Swing-1F6FEB)
![MySQL](https://img.shields.io/badge/Database-MySQL%208-4479A1?logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)
[![Build](https://github.com/purnendu2318/MediFlow-Healthcare-Management-System/actions/workflows/build.yml/badge.svg)](https://github.com/purnendu2318/MediFlow-Healthcare-Management-System/actions/workflows/build.yml)

</div>

---

## Overview

MediFlow is a desktop-based Hospital Management System prototype developed with **Java 21, Swing, JDBC and MySQL**. The current starter release focuses on sign-in and patient registration/search, while laying out a relational schema for additional hospital workflows.

The project is intentionally documented according to its current implementation: several dashboard areas and database entities are foundations for future work, not completed end-to-end modules.

## Screenshots

| Sign-in | Dashboard |
|:---:|:---:|
| ![MediFlow login screen](screenshots/login.png) | ![MediFlow dashboard](screenshots/dashboard.png) |

## Current implementation

- **Authentication:** sign-in against MySQL user records, with BCrypt password-hash verification.
- **Dashboard shell:** desktop navigation, signed-in user/role display, and placeholder views for planned modules.
- **Patient management:** register a patient, search active records, refresh the list, and deactivate a selected record.
- **Input handling:** basic validation for required patient name, phone number, and date-of-birth format.
- **Database foundation:** MySQL schema covering users, departments, patients, doctors, availability, appointments, queue tokens, medical records, prescriptions, invoices, payments, notifications and audit logs.
- **Build automation:** GitHub Actions workflow to compile and verify the Maven project with Java 21.

## Planned work

The following areas are represented in the dashboard or database design but are **not implemented as complete workflows yet**:

- Doctor and department management
- Appointment scheduling and conflict checks
- Queue and check-in management
- Electronic medical records and prescriptions
- Invoicing and payment processing
- Notifications, reports, and audit-log viewer
- Account administration and password recovery

## Technology stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Desktop UI | Java Swing |
| Database | MySQL 8+ |
| Database access | JDBC / MySQL Connector/J |
| Password hashing | jBCrypt |
| Build | Apache Maven |
| IDE | Visual Studio Code (or any Java IDE) |

## Run locally

### Prerequisites

- JDK 21
- Apache Maven 3.9+
- MySQL Server 8+
- MySQL Workbench (optional, for running the schema visually)

### 1. Get the source

```bash
git clone https://github.com/purnendu2318/MediFlow-Healthcare-Management-System.git
cd MediFlow-Healthcare-Management-System
```

### 2. Create the database

Run `database/schema.sql` in MySQL Workbench or the MySQL command-line client. The script creates the `mediflow_hms` database and its tables.

### 3. Configure the database connection

Copy the example properties file:

**Windows PowerShell**

```powershell
Copy-Item src/main/resources/db.properties.example src/main/resources/db.properties
```

**macOS / Linux**

```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

Edit `src/main/resources/db.properties` and set your local MySQL username and password. This local file is ignored by Git; never commit real credentials.

### 4. Build and launch

```bash
mvn clean verify
mvn exec:java
```

The application checks the database connection before opening the sign-in window.

## Development login

On first launch, if the `users` table is empty, the application creates a local development administrator:

| Field | Value |
|---|---|
| Email | `admin@mediflow.local` |
| Password | `Admin@123` |
| Role | `SUPER_ADMIN` |

This is a **demo-only bootstrap credential** for a local database with synthetic data. Change or remove it before sharing the application with anyone else. Do not use it in a production environment.

## Project structure

```text
.
├── .github/workflows/build.yml
├── database/schema.sql
├── screenshots/
│   ├── dashboard.png
│   └── login.png
├── src/main/java/com/mediflow/
│   ├── config/       # MySQL connection configuration
│   ├── model/        # User and patient records
│   ├── repository/   # JDBC data access
│   ├── security/     # BCrypt password utilities
│   ├── service/      # Authentication and patient services
│   ├── ui/           # Swing login, dashboard and patient views
│   └── util/         # Shared UI helpers
├── src/main/resources/
│   └── db.properties.example
├── .gitignore
├── pom.xml
└── README.md
```

## Security and data-use notice

This is an educational portfolio prototype, **not a certified clinical application**. Use synthetic patient information only. The development login is predictable by design, the application has not undergone a production security review, and the planned modules are not yet available. Do not store real patient records or use this application for diagnosis, treatment, billing, or operational hospital decisions.

## Author

**Purnendu Roy** · [GitHub](https://github.com/purnendu2318) · [Portfolio](https://purnendu-portfolio-mu.vercel.app/) · [LinkedIn](https://www.linkedin.com/in/purnendu-roy-591000314/)

---