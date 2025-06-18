# IT ACADEMY BACKEND JAVA SPECIALIZATION

# SPRINT 5 TASK 2

# Virtual Pet API

This repository contains the backend service for the Virtual Pet application. It is a RESTful API built with Spring Boot that allows users to register, log in, and manage their own virtual pets. It also includes an administration panel for user and pet management.

## Features

-   **User Authentication:** Secure user registration and login system using JWT (JSON Web Tokens).
-   **Role-Based Access Control:**
    -   `ROLE_USER`: Can create, view, and interact with their own pets.
    -   `ROLE_ADMIN`: Has full access to manage all users and pets in the system.
-   **Pet Management:**
    -   Create new pets (Dog, Cat, Hamster).
    -   View a list of personal pets.
    -   Interact with pets by feeding them or playing with them, which affects their stats (hunger, energy, happiness).
    -   Delete pets.
-   **Admin Panel:**
    -   View all users and their associated pets.
    -   Delete users (which also deletes their pets in a cascading manner).
    -   Delete any pet in the system.
-   **API Documentation:** Interactive API documentation generated automatically with Swagger (OpenAPI 3).

## Technologies Used

-   **Java 17**
-   **Spring Boot 3.2.5**
-   **Spring Security:** For authentication and authorization.
-   **Spring Data JPA (Hibernate):** For database interaction and object-relational mapping.
-   **MySQL:** As the relational database.
-   **JWT (JSON Web Tokens):** For stateless session management.
-   **Maven:** As the build and dependency management tool.
-   **Swagger / OpenAPI 3 (springdoc-openapi):** For API documentation.

## Prerequisites

Before you begin, ensure you have the following installed on your system:
-   JDK 17 or later.
-   Maven 3.x.
-   A running MySQL server.

## Setup & Installation

1.  **Clone the repository:**
    ```bash
    git clone <your-repository-url>
    cd <repository-folder>
    ```

2.  **Create the MySQL Database:**
    Connect to your MySQL server and run the following command:
    ```sql
    CREATE DATABASE mascotes_db;
    ```

3.  **Configure Application Properties:**
    Open the `src/main/resources/application.properties` file and update the database connection settings with your MySQL credentials:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/mascotes_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password
    ```
    Also, ensure the server port is set correctly (e.g., to 8000):
    ```properties
    server.port=8000
    ```

4.  **Run the application:**
    Use Maven to build and run the project:
    ```bash
    mvn spring-boot:run
    ```
    The application will start, and Hibernate will automatically create the necessary tables in the `mascotes_db` database.

## API Documentation (Swagger)

Once the application is running, you can access the interactive API documentation through your web browser. This UI allows you to view all available endpoints and test them directly.

-   **Swagger UI URL:** [http://localhost:8000/swagger-ui.html](http://localhost:8000/swagger-ui.html)

To test the protected endpoints, first, use the `/api/auth/signin` endpoint to get a JWT token. Then, click the **"Authorize"** button on the Swagger page and paste the token in the format `Bearer <your_token>`.

## API Endpoints

The API is structured into three main categories:

-   `/api/auth`: Handles user registration and login.
-   `/api/pets`: Handles all actions related to pet management for the authenticated user.
-   `/api/admin`: Provides administration endpoints, accessible only to users with the `ROLE_ADMIN` role.
