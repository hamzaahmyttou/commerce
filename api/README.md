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

```bash
docker-compose up --build
```

## Authentication

Login to receive JWT
Send token in Authorization: Bearer <token>

## API Endpoints

| Method | Endpoint            | Description                      |
|--------|---------------------|----------------------------------|
| POST   | /api/users/register | Register user                    |
| POST   | /api/users/login    | Login                            |
| GET    | /api/products       | List products                    |
| GET    | /api/products/{id}  | Read the product n°=id           |
| POST   | /api/products       | Create product (auth)            |
| PUT    | /api/products/{id}  | Update the product n°=id (owner) |
| DELETE | /api/products/{id}  | Delete the product n°=id (ADMIN) |
