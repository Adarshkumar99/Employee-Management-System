# Employee Management System (Java + JDBC + PostgreSQL)

A simple console-based CRUD project to practice **OOP, JDBC, SQL, and Git**.

## Features
- Add Employee
- Update Employee
- Delete Employee
- Search Employee (by name, partial match)
- List all Employees

## Project Structure
```
EmployeeManagementSystem/
├── src/
│   ├── Employee.java       # Model class (OOP: fields, constructors, getters/setters)
│   ├── DBConnection.java   # Opens the JDBC connection to PostgreSQL
│   ├── EmployeeDAO.java    # All SQL queries (INSERT/UPDATE/DELETE/SELECT) live here
│   └── Main.java           # Console menu that ties everything together
├── sql/
│   └── schema.sql          # Creates the database table + sample rows
└── README.md
```

## Prerequisites
1. **Java JDK 17+** installed
2. **PostgreSQL** installed and running
3. **PostgreSQL JDBC driver** (`postgresql-<version>.jar`) — download from
   https://jdbc.postgresql.org/download/ and add it to your classpath

## Setup

### 1. Create the database
```bash
createdb employee_management
psql -d employee_management -f sql/schema.sql
```

### 2. Configure the connection
Open `src/DBConnection.java` and update these three lines with your local
PostgreSQL details:
```java
private static final String URL = "jdbc:postgresql://localhost:5432/employee_management";
private static final String USER = "postgres";
private static final String PASSWORD = "your_password_here";
```

### 3. Compile and run
```bash
# Compile (adjust the jar path to wherever you downloaded the driver)
javac -cp .:postgresql-42.7.3.jar -d out src/*.java

# Run
java -cp out:postgresql-42.7.3.jar Main
```
On Windows, replace `:` with `;` in the classpath.

## What each class teaches you
| Class              | Concept practiced                                   |
|---------------------|------------------------------------------------------|
| `Employee.java`      | OOP — encapsulation, constructors, getters/setters   |
| `DBConnection.java`  | JDBC — establishing a database connection            |
| `EmployeeDAO.java`   | SQL + JDBC — `PreparedStatement`, CRUD queries        |
| `Main.java`          | Application flow, `Scanner` input, control structures |

## Git
This project is meant to be pushed to GitHub as a portfolio piece. Suggested first commit:
```bash
git init
git add .
git commit -m "Initial commit: Employee Management System CRUD app"
git remote add origin <your-repo-url>
git push -u origin main
```

## Possible next steps (once comfortable with the basics)
- Add input validation (e.g. reject invalid emails, negative salaries)
- Switch raw JDBC to a connection pool (HikariCP)
- Add a simple JUnit test for `EmployeeDAO`
- Wrap this in a small Spring Boot REST API instead of a console menu
