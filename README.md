# Inventory Management Microservice

## Overview
This is a Spring Boot microservice for managing an inventory system. It includes CRUD operations for products and categories, along with Kafka integration for messaging. The service communicates with a PostgreSQL database for data persistence and uses Kafka for event-driven communication.

## Features
- Create, Read, Update, Delete operations for products and categories.
- Kafka-based event publishing and consuming for inventory events.
- PostgreSQL for data persistence.
- Global error handling for REST endpoints.
- Eureka client for service registration and discovery(NOT READY).

## Prerequisites
- Java 17
- Maven
- PostgreSQL (Docker image : https://github.com/onjin/docker-alpine-postgres)
- Kafka (Added docker-compose) ,You can view messages via kafkadrop : http://localhost:19000/topic/inventory-events
- Eureka (if using service discovery)

## Running the Application

### Step 1: Set up PostgreSQL
1. Create a PostgreSQL database named `inventory`.
2. CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

3. Update the `application.yml` file with your PostgreSQL credentials.

### Step 2: Set up Kafka
1. Ensure that Kafka is running on `localhost:9092` (or update the configuration if necessary).
2. Create a Kafka topic named `inventory-events`.

### Step 3: Run the Application
1. Navigate to the project directory.
2. Use Maven to build the project:
   ```bash
   mvn clean install

### API Endpoints

#### Products:
- `GET /inventory/products` - Get all products.
- `GET /inventory/products/{id}` - Get a product by ID.
- `POST /inventory/products` - Create a new product.
- `PUT /inventory/products/{id}` - Update an existing product.
- `DELETE /inventory/products/{id}` - Delete a product by ID.
-  PUT http://localhost:9191/products/discount/4?discountPercentage=10

#### Categories:
- `GET /inventory/categories` - Get all categories.
- `GET /inventory/categories/{id}` - Get a category by ID.
- `POST /inventory/categories` - Create a new category.
- `PUT /inventory/categories/{id}` - Update an existing category.
- `DELETE /inventory/categories/{id}` - Delete a category by ID
