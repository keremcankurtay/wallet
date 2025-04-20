# Wallet Application

[![Java Version](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
[![Spring Boot Version](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven Central](https://img.shields.io/maven-central/v/org.springframework.boot/spring-boot-starter-parent.svg?label=Maven%20Central)](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-parent/3.4.4)

## Overview

This project is built using Java 17 and leverages the power of Spring Boot version 3.4.4 for rapid application development. Maven is used as the build
automation tool.

## Technologies Used

* **Java:**
  17 ([https://www.oracle.com/java/technologies/javase-jdk17-downloads.html](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html))
* **Spring Boot:** 3.4.4 ([https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot))
* **Spring Data JPA:** ([https://spring.io/projects/spring-data-jpa](https://spring.io/projects/spring-data-jpa))
* **Spring Web:** ([https://spring.io/projects/spring-framework](https://spring.io/projects/spring-framework))
* **Spring Security:** ([https://spring.io/projects/spring-security](https://spring.io/projects/spring-security))
* **h2 Database:** ([https://www.h2database.com/](https://www.h2database.com/))
* **lombok:** ([https://projectlombok.org/](https://projectlombok.org/))
* **JUnit:** ([https://junit.org/junit5/](https://junit.org/junit5/))
* **Mockito:** ([https://site.mockito.org/](https://site.mockito.org/))
* **Maven:** ([https://maven.apache.org/](https://maven.apache.org/))

## Getting Started

### Prerequisites

Make sure you have the following installed on your system:

* **Java Development Kit (JDK):** Version 17 or higher. You can download it
  from [https://www.oracle.com/java/technologies/javase-jdk17-downloads.html](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).
* **Maven:** Version [Specify your minimum Maven version if necessary, otherwise just mention Maven]. You can download it
  from [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi).

### Building the Project

1. Navigate to the root directory of your project in your terminal.
2. Run the following Maven command to build the project and generate the executable JAR file:

   ```bash
   mvn clean package
   ```

   This command will:
    * `clean`: Remove any previously built artifacts.
    * `package`: Compile the code, run tests, and package the application into a JAR file.

3. Once the build is successful, the JAR file will be located in the `target` directory. The filename will typically be
   `your-project-name-0.0.1-SNAPSHOT.jar` (the version might vary).

### Running the Application

You can run the Spring Boot application from the command line using the generated JAR file:

1. Navigate to the `target` directory within your project's root directory:

   ```bash
   cd target
   ```

2. Execute the following command to start the application:

   ```bash
   java -jar your-project-name-0.0.1-SNAPSHOT.jar
   ```

   Replace `your-project-name-0.0.1-SNAPSHOT.jar` with the actual name of your JAR file.

3. The Spring Boot application will start, and by default, it will be accessible at:

   ```
   http://localhost:8080
   ```

## Usage

When application starts, it will create employee and customers for testing purposes in UserCreator.class. APIs secured with basic authentication.
Check UserCreator.class for credentials.

### API Endpoints

The API endpoints are defined in the `src/main/java/com/kurtay/wallet/controller` package. While all APIs are accessible for employees, only some of
them are accessible for customers.
Postman can be used to test the API endpoints. The following endpoints are available:

* `GET /v1/wallets`: Retrieve all wallets with given filters
* `POST /v1/wallets`: Create a new wallet.
* `POST /v1/transactions/deposit`: Deposit an amount into a wallet.
* `POST /v1/transactions/withdraw`: Withdraw an amount from a wallet.
* `POST /v1/transactions/deposits/status`: Update the status of a deposit transaction.
* `POST /v1/transactions/withdraw/status`: Update the status of a withdrawal transaction.
* `GET /v1/transactions/wallets/{id}`: Retrieve all transactions for a specific wallet.

## TODOs

* Add more unit tests to increase coverage.
* Implement integration tests for the APIs.
* Add auto-generated Swagger documentation for the APIs.
* Implement error handling and logging with enhanced exceptions and their handling.
* Use jacoco for code coverage and SonarQube for code quality analysis.
* Use a real database
* Use a script versioning tool like Flyway or Liquibase for database migrations.
* Add i18n support for multiple language support.

