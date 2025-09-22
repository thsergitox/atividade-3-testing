# Sample App

This is a web application generated using [JHipster](https://www.jhipster.tech/).

## Overview

This application is an online shop where users can browse products, add them to a wish list, and place orders. It includes features for managing customers, products, categories, addresses, and orders.

## Technologies Used

This project uses a combination of modern technologies for both the frontend and backend.

### Backend

*   **[Spring Boot](https://spring.io/projects/spring-boot):** A framework for creating stand-alone, production-grade Spring-based applications.
*   **[Java](https://www.java.com/):** The primary programming language for the backend.
*   **[Maven](https://maven.apache.org/):** A build automation tool used for managing the project's build, reporting, and documentation.
*   **[Spring Data JPA](https://spring.io/projects/spring-data-jpa):** For persisting data in SQL stores with Java Persistence API (JPA) using Spring Data and Hibernate.
*   **[PostgreSQL](https://www.postgresql.org/):** A powerful, open-source object-relational database system.
*   **[Liquibase](https://www.liquibase.org/):** For tracking, managing, and applying database schema changes.
*   **[Spring Security](https://spring.io/projects/spring-security):** For authentication and access-control.
*   **[REST APIs](https://en.wikipedia.org/wiki/REST):** The application exposes a set of RESTful APIs for the frontend to consume.
*   **[OpenAPI (Swagger)](https://www.openapis.org/):** The APIs are documented using the OpenAPI specification, and you can explore them using Swagger UI.

### Frontend

*   **[Angular](https://angular.io/):** A platform for building mobile and desktop web applications.
*   **[TypeScript](https://www.typescriptlang.org/):** A strict syntactical superset of JavaScript that adds optional static typing.
*   **[Bootstrap](https://getbootstrap.com/):** A popular CSS framework for developing responsive and mobile-first websites.
*   **[HTML5](https://en.wikipedia.org/wiki/HTML5) & [CSS3](https://en.wikipedia.org/wiki/CSS#CSS3):** The standard markup language and stylesheet language for creating web pages.
*   **[Jest](https://jestjs.io/) & [Cypress](https://www.cypress.io/):** For testing the frontend code.


## How to Run

To run this application, you will need to have Node.js, npm, and Docker installed.

1.  **Build the project:**
    ```bash
    mvn clean installl
    ```
2.  **Run application:**
    ```bash
    mvn
    


The application will be available at `http://localhost:9000`.

## Project Structure

The project is organized as follows:

*   `src/main/java`: Contains the backend Java source code.
*   `src/main/resources`: Contains the backend configuration files.
*   `src/main/webapp`: Contains the frontend Angular application.
*   `pom.xml`: The Maven project configuration file.
*   `package.json`: The npm package configuration file.

### `src/main/java` Folder Organization

The `src/main/java` folder is organized into the following packages:

*   `myapp.aop`: Contains the Aspect-Oriented Programming (AOP) configurations, such as logging.
*   `myapp.config`: Contains the application's configuration classes, such as database configuration, security configuration, and web configuration.
*   `myapp.domain`: Contains the application's domain objects (entities), such as `Product`, `Category`, and `Order`.
*   `myapp.repository`: Contains the Spring Data JPA repositories for accessing the database.
*   `myapp.security`: Contains the security-related classes, such as the user details service and security constants.
*   `myapp.service`: Contains the application's business logic (services).
*   `myapp.web`: Contains the REST controllers that expose the application's APIs.
*   `myapp.management`: Contains the application's management-related classes, such as the security meters service.
