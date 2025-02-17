# Product Inventory Manager

## Description
Inventory Manager is a Spring Boot application that manages product inventory. It provides RESTful APIs to create, read, update, and delete products.

## Prerequisites
- Java 17 
- Maven 3.6.0 
- Springboot 3.2.2
- H2 Database

## Installation
1. Clone the repository:
    ```
    git clone https://github.com/KOKILAIT/ProductInventoryManager.git
    ```
2. Navigate to the project directory:
    ```
    cd inventory-manager
    ```
3. Build the project using Maven:
    ```
    mvn clean install
    ```

## Configuration
Update the `application.properties` or `application-test.properties` file with your database configuration if needed.

## Running the Application
To run the application, use the following command:
```
mvn spring-boot:run
```

The application exposes the following endpoints:  

- GET /products - Retrieve all products
- GET /products/{id} - Retrieve a product by ID
- POST /products - Create a new product
- PUT /products/{id} - Update an existing product
- DELETE /products/{id} - Delete a product by ID
- GET /products/search?productName={name} - Search products by name

## Running Tests
```
mvn test
```

## Logging 
Logs are stored in the logs/application.log file. You can configure the logging level and file location in the application.properties file.
