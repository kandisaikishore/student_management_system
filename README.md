# Student Management System

[![Java](https://img.shields.io/badge/Java-Servlets-orange?logo=openjdk)](https://www.java.com/) [![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1?logo=mysql)](https://www.mysql.com/) [![Tomcat](https://img.shields.io/badge/Server-Apache%20Tomcat%209-F8DC75?logo=apachetomcat)](https://tomcat.apache.org/) [![GitHub](https://img.shields.io/badge/Source-GitHub-181717?logo=github)](https://github.com/kandisaikishore/student_management_system)

A beginner-friendly **Java web application for managing student/user records** using Java Servlets, JDBC, and MySQL. It demonstrates browser forms, session-based profile workflows, and CRUD-style record operations in an Eclipse Dynamic Web Project. It uses Java Servlets to handle browser requests, JDBC to communicate with MySQL, and HTML pages for the user interface. The code is organized as an Eclipse Dynamic Web Project.

The goal of this project is to demonstrate how a traditional Java web application connects a browser form to a servlet and then to a relational database.

## Table of contents

- [Features](#features)
- [How it works](#how-it-works)
- [Technology stack](#technology-stack)
- [Project structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Database setup](#database-setup)
- [Run the project](#run-the-project)
- [Application routes](#application-routes)
- [Troubleshooting](#troubleshooting)
- [Security and known limitations](#security-and-known-limitations)
- [Ideas for improvement](#ideas-for-improvement)

## Features

Based on the checked-in source, the application includes these workflows:

- **Sign up:** submit a user record with an ID, name, email, password, phone number, and role.
- **Log in:** look up a user by email and compare the submitted password with the stored value.
- **Home page:** navigate to the available account and student-record actions.
- **Profile:** display the logged-in user's profile fields and submit profile updates.
- **Add student:** insert a record into the `users` table.
- **Find student:** look up a record by ID.
- **Update student:** update a record's name, email, password, phone number, and role.
- **Delete student:** intentionally unavailable in the UI because the repository has no matching delete servlet.

> **Important:** The repository contains a legacy `deleteStudent.html` page, but no matching delete servlet. The home/profile navigation no longer exposes delete actions until a handler is implemented and tested.

## How it works

1. A visitor opens an HTML page such as `login.html` or `signup.html`.
2. The form sends its fields to a Java Servlet mapped with `@WebServlet`.
3. The servlet reads request parameters and creates a JDBC connection to MySQL.
4. SQL statements query or modify the `users` table. The project uses `PreparedStatement` for these operations.
5. The servlet returns HTML or redirects the browser to another route.
6. The login servlet stores profile fields in an HTTP session, which the home and profile servlets read.

This is a classic **Servlet + JDBC** application rather than a Spring Boot application.

## Technology stack

| Technology | Purpose |
|---|---|
| Java | Application logic |
| Java Servlets (`javax.servlet`) | Handle HTTP requests |
| JDBC | Connect Java code to MySQL |
| MySQL | Store user/student records |
| HTML | Login, signup, add, find, delete, and update forms |
| Apache Tomcat 9 | Servlet container |
| Eclipse IDE | Project metadata and local development configuration |

The project includes a MySQL Connector/J JAR under `src/main/webapp/WEB-INF/lib` and has Eclipse project metadata. The checked-in classpath references a Tomcat 9 runtime and JavaSE-25; see [Run the project](#run-the-project) for compatibility notes.

## Project structure

```text
student_management_system/
├── src/
│   └── main/
│       ├── java/
│       │   └── student_management_system/
│       │       ├── HomePage.java       # Home page and navigation
│       │       ├── Login.java          # Login and session creation
│       │       ├── Signup.java         # User registration
│       │       ├── Profile.java        # Display profile and profile form
│       │       ├── AddStudent.java     # Insert a user/student
│       │       ├── FindStudent.java    # Search a record by ID
│       │       ├── UpdateStudent.java  # Update a record by ID
│       │       └── Update.java         # Update the signed-in profile
│       └── webapp/
│           ├── login.html
│           ├── signup.html
│           ├── addStudent.html
│           ├── findStudent.html
│           ├── deleteStudent.html
│           └── updateStudent.html
├── src/test/java/student_management_system/PasswordUtilSmokeTest.java # Password regression checks\n├── build/classes/                      # Generated class files (if retained)
├── .classpath                          # Eclipse classpath/runtime settings
└── .project                             # Eclipse project metadata
```

## Prerequisites

Install the following before running the application:

- **JDK:** use a JDK compatible with your chosen Eclipse/Tomcat setup. The checked-in Eclipse classpath references JavaSE-25, so adjust the project settings if you use a different JDK.
- **Apache Tomcat 9:** the source imports `javax.servlet.*`; Tomcat 9 matches that API namespace. Tomcat 10+ uses `jakarta.servlet.*` and requires code/dependency migration.
- **MySQL Server:** running locally or reachable from your machine.
- **Eclipse IDE for Enterprise Java and Web Developers** (recommended) or another IDE that supports Dynamic Web Projects.

## Database setup

The current servlet source uses the following local connection settings:

```text
Host:     localhost
Port:     3306
Database: student_management_system
Table:    users
```

The code expects a `users` table with columns in this order: `id`, `name`, `email`, `password`, `number`, and `role`. One compatible starting schema is:

```sql
CREATE DATABASE student_management_system;
USE student_management_system;

CREATE TABLE users (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    number BIGINT,
    role VARCHAR(50)
);
```

Use this schema as a development example and confirm it against each SQL statement in the source before running. Because some statements use `INSERT INTO users VALUES (...)` without a column list, the column order must match.

### Configure database credentials

The servlets read database settings from environment variables:

- `DB_URL` (optional; defaults to `jdbc:mysql://localhost:3306/student_management_system`)
- `DB_USER` (required)
- `DB_PASSWORD` (required)

Configure these variables in the environment used by Tomcat/Eclipse. Use a dedicated MySQL account with only the privileges the application needs; do not use the root account or commit real credentials. If either credential variable is missing, database-backed requests return a configuration error rather than using hard-coded credentials.

## Run the project

### Option A — Eclipse + Tomcat (recommended)

1. Clone the repository:

   ```bash
   git clone https://github.com/kandisaikishore/student_management_system.git
   ```

2. Open Eclipse and import the folder as an **Existing Project**.
3. Configure a JDK supported by your environment and a **Tomcat 9** server runtime.
4. Make sure MySQL is running, create the database/table, and configure the database credentials used by the servlets.
5. In Eclipse, choose **Run As → Run on Server**, select Tomcat 9, and add the project to the server.
6. Open the deployed context path in your browser, then navigate to `login.html` or `signup.html`.

The exact context path is determined by the Eclipse/Tomcat deployment configuration. For example, if the context path is `student_management_system`, the login page may be at:

```text
http://localhost:8080/student_management_system/login.html
```

If Eclipse deploys the application under a different context path, use that path instead.

### Option B — Package and deploy to Tomcat

This repository currently has Eclipse Dynamic Web Project metadata but no Maven or Gradle build file. To deploy outside Eclipse, package the web application as a WAR using a compatible Java web project setup, include the compiled servlet classes and required MySQL driver, and deploy the WAR to Tomcat 9's `webapps` directory. Confirm the final artifact contains the HTML files and servlet classes in the expected locations.

## Application routes

| Page / route | Purpose |
|---|---|
| `login.html` → `/login` | Submit login credentials |
| `signup.html` → `/signup` | Register a user |
| `/homepage` | Show navigation after login |
| `/profile` | Display the current session profile |
| `addStudent.html` → `/addStudent` | Add a record |
| `findStudent.html` → `/find` | Find a record by ID |
| `updateStudent.html` → `/updateStudent` | Update a record by ID |
| `deleteStudent.html` | Legacy form; not linked until a delete handler is implemented |

The servlet mappings are declared with `@WebServlet` annotations in the Java classes.

## Troubleshooting

**MySQL connection refused**
- Check that MySQL is running and listening on port `3306`.
- Confirm the database name, username, password, and privileges.
- Verify the JDBC driver is available to the deployed application.

**404 for a servlet or HTML page**
- Confirm the project was successfully deployed to Tomcat.
- Check the context path and the exact URL.
- Confirm the compiled class is under the expected package and that the `@WebServlet` mapping matches the requested route.

**Servlet classes fail to compile on Tomcat 10+**
- The source uses `javax.servlet`; run with Tomcat 9 or migrate the imports and dependencies to the `jakarta.servlet` API.

**Login or signup fails**
- Check that the `users` table exists and has the expected columns in the expected order.
- Check Tomcat logs for JDBC or SQL exceptions.
- Confirm email uniqueness and required fields according to your database schema.

## Security and known limitations

This project is useful for learning, but it should **not be deployed publicly or used with real student data as-is**. The reviewed source has several security and reliability risks that should be addressed before broader use:

- New passwords are stored using salted PBKDF2-HMAC-SHA256 hashes. Existing legacy plain-text passwords are verified only during login and migrated to a hash after a successful login; accounts that have not logged in again may still have legacy values in the database. Never store raw passwords in session state.
- Database credentials are read from environment variables; keep them out of source control and use a least-privilege database account.
- Login, student creation, and update operations use POST. Add CSRF protection to state-changing actions before exposing the application publicly.
- Server-side validation and consistent error handling are limited; avoid showing stack traces or internal details to users.
- Session lifecycle and authorization checks should be strengthened, especially for add/update/delete actions.
- Dynamic HTML output should be safely escaped to prevent cross-site scripting (XSS).
- The delete form's matching servlet should be verified or implemented before the feature is considered complete.

## Ideas for improvement

- Centralize database connectivity and configuration
- Extend authorization checks to every data-changing route and add role-based authorization
- Separate persistence logic into DAO/repository classes
- Use POST/redirect/GET patterns for state-changing actions
- Expand automated tests beyond password hashing to authentication and CRUD workflows
- Add consistent UI styling, accessible labels, and informative messages
- Add a reproducible WAR build, deployment guide, and CI checks

## Contributing

Contributions that improve clarity, correctness, maintainability, or security are welcome. Please include the issue being addressed, steps to reproduce it, and the validation performed.

## License

No license file is currently included. Add a `LICENSE` file if you intend to specify how others may use, modify, or redistribute this project.
