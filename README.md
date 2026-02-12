# E-commerce API

Spring Boot REST API for a simple e-commerce platform.

## Tech Stack
- Java 21
- Spring Boot
- Spring Security (JWT)
- PostgreSQL
- Docker & Docker Compose

## Features
- User registration and login (JWT)
- Role-based authorization (USER / ADMIN)
- Product CRUD with ownership checks
- Global exception handling

## Run locally
mvn clean package
docker-compose up --build

## Authentication
Login to receive JWT
Send token in Authorization: Bearer <token>

## API Endpoints
| Method | Endpoint       | Description                      |
|--------|----------------|----------------------------------|
| POST   | /auth/register | Register user                    |
| POST   | /auth/login    | Login                            |
| GET    | /products      | List products                    |
| GET    | /products/{id} | Read the product n°=id           |
| POST   | /products      | Create product (auth)            |
| PUT    | /products/{id} | Update the product n°=id (owner) |
| DELETE | /products/{id} | Delete the product n°=id (ADMIN) |
