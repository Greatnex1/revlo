
# Revlo using Spring Boot

Revlo is a comprehensive web application developed using Spring Boot, aimed at providing users with a feature-rich platform to efficiently manage and organize their inventory, customer details and sales. This README provides an overview of the system, installation instructions and other details about the project.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
    - [Prerequisites Requirement](#prerequisites-requirement)
    - [Installation](#installation)
- [API Endpoints](#api-endpoints)


## Features

Revlo comes equipped with robust reporting features can provide valuable insights into sales performance, inventory management, customer behavior, and more:

- **Customer Management**: Store and manage customer information, including contact details, purchase history, preferences, and interactions.
- **Product Management**: Maintain a catalog of products or services offered by the business.
- **Inventory Management**: Monitor and manage inventory levels in real-time.
- **Sales Order Processing**: Create and manage sales orders for products or services.
- **Sales Tracking and Reporting**: Monitor sales performance and track key metrics such as revenue, profit margins, and sales growth.
- **Quoting and Pricing**: Create and manage price quotes for products or services.
- **User Access and Security**: Control user access levels and permissions to ensure data security and confidentiality.

## Technologies Used

The Invoice and Payment Link System leverages modern technologies to deliver a robust and efficient experience:

- **Java**: The core programming language for developing the application logic.
- **Spring Boot**: A powerful framework for building robust and scalable applications.
- **Spring Data JPA**: Provides data access and manipulation capabilities using the Java Persistence API.
- **Spring Web**: Facilitates the creation of web APIs and interfaces.
- **Spring Security**: Ensures secure authentication and authorization within the application, guarding against unauthorized access and protecting sensitive data.
- **JSON Web Token (JWT)**: Enables stateless authentication by securely transmitting user information as JSON objects, facilitating secure communication between the client and server.
- **Auditor Aware**: Automatically tracks and records metadata about the user who created or modified entities, such as creation and modification timestamps, enhancing data auditing and accountability.
- **MSQL**: A popular relational database management system used for storing and retrieving structured data efficiently.
- **Maven**: Manages project dependencies and provides a structured build process.
- **Git**: Version control for collaborative development.

## Getting Started

### Prerequisites Requirement

Before getting started, ensure you have the following components installed:

1. **This project was built using JDK 17, you would need JDK 17 installed on you local machine.**

- [Java Development Kit (JDK 17)](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)


## Build and Run the Application:

Execute the following command to build and run the application:

````bash
mvn clean package install
mvn spring-boot:run
````


### Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/Greatnex1/revlo.git
   cd SalesManagementSystem
   ```

2. **Configure the Database:**

   Modify the `src/main/resources/application.properties` file to include your database connection details:

   ```properties
   spring.datasource.driver-class-name=com.postgresql.jdbc.Driver
   spring.datasource.url=your_database_jdbc_url
   spring.datasource.username=your_database_username
   spring.datasource.password=your_database_password
   ```

3. **Token Requirements:**

   Modify the `src/main/resources/application.properties` file to include your JWT token:

    ```properties
   sales.app.SecretKey=your_JWT_token
    ```


## API Endpoints

Revlo offers the following API endpoints:

- User's Registration: `POST /api/v1/users/create`
- User's login: `POST /api/v1/auth/login`
- Product creation: `POST /api/v1/products/create`
- Get a product: `GET /api/v1/products/product?productId={productId}`
- Update a product: `PUT /api/v1/products/update?productId={productId}`
- Get all products: `GET /api/v1/products/allProduct`
- Delete a product: `DELETE /api/v1/products/delete?productId={productId}`
- Get product report: `GET /api/v1/products/reports/product`
- Sales creation: `POST /api/v1/sales/create?userId={userId}`
- Get a sales: `GET /api/v1/sales/sales?salesId={salesId}`
- Update a sales: `PUT /api/v1/sales/update?salesId={salesId}&?userId={userId}`
- Get all sales: `GET /api/v1/sales/allTransactions`
- Get sales report: `GET /api/v1/sales/reports/sales?startDate={startDate}&?endDate={endDate}`
- Client creation: `POST /api/v1/clients/create?userId={userId}`
- Get a client: `GET /api/v1/clients/client?phoneNumber={phoneNumber}`
- Update a client: `PUT /api/v1/clients/update?phoneNumber={phoneNumber}`
- Get all clients: `GET /api/v1/clients/allClients`
- Delete a client: `DELETE /api/v1/clients/delete?phoneNumber={phoneNumber}`
- Get client report: `GET /api/v1/clients/reports/client`
- Generate sales report: `GET /api/v1/sales/reports/pdf`
- Generate product report: `GET /api/v1/products/reports/pdf`
