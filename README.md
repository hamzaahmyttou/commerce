# E-commerce Platform (API + minimal UI)

[![CI](https://github.com/hamzaahmyttou/commerce/actions/workflows/ci.yml/badge.svg)](https://github.com/hamzaahmyttou/commerce/actions/workflows/ci.yml)

This project is an e-commerce application composed of:

* **Backend**: Spring Boot REST API (JWT-secured)
* **Frontend**: Angular UI (Product endpoints visualization)

Minimal Angular frontend used only for testing and visualizing the API Product endpoints.
The core of the project is the Java Spring Boot backend.

---

## Project Structure

```
commerce/
├── api/      # Spring Boot API
└── ui/       # Angular UI
```

---

## Tech Stack

### Backend

* Java 21
* Spring Boot
* Spring Security (JWT)
* PostgreSQL
* Docker & Docker Compose

### Frontend

* Angular
* TypeScript
* HTML/CSS

---

## Features

### Backend API

* User registration & login (JWT authentication)
* Role-based authorization (USER / ADMIN)
* Product CRUD operations
* Ownership checks for updates
* Global exception handling

### Frontend UI

* Product listing page
* API integration with backend
* Basic Angular component structure

---

## Run the Project

### Option : Run manually

#### Backend

```bash
cd api
docker-compose up --build
```

API available at:

```
http://localhost:8080
```

#### Frontend

```bash
cd ui
ng serve
```

UI available at:

```
http://localhost:4200
```

---

## GitHub Actions (CI)

This project uses **GitHub Actions** to automate Continuous Integration (CI).

On every push and pull request, the workflow automatically:

* Starts a PostgreSQL service for backend integration tests
* Builds and tests the Spring Boot backend using Maven
* Builds the backend Docker image
* Builds the Angular frontend
* Verifies that all steps complete successfully

### Required GitHub Secrets

Before running the workflow, add the following repository secrets under:

**Settings → Secrets and variables → Actions**

| Secret                       | Description                                     |
| ---------------------------- | ----------------------------------------------- |
| `SPRING_DATASOURCE_URL`      | `jdbc:postgresql://localhost:5432/ecommerce_db` |
| `SPRING_DATASOURCE_USERNAME` | PostgreSQL username                             |
| `SPRING_DATASOURCE_PASSWORD` | PostgreSQL password                             |
| `JWT_SECRET`                 | Secret key used to sign JWT tokens              |

> **Note**
>
> The GitHub Actions workflow connects to PostgreSQL using `localhost`, while the Docker Compose configuration uses the service name `db`. This is expected because GitHub Actions runs the database as a service container.

### Workflow

The CI workflow is located at:

```text
.github/workflows/ci.yml
```

It is triggered automatically on:

* Push to the main branches
* Pull requests

---

## Authentication

1. Login via:

```
POST /api/users/login
```

2. Receive JWT token

3. Use it in requests:

```
Authorization: Bearer <token>
```

---

## API Endpoints

| Method | Endpoint            | Description                    |
| ------ | ------------------- | ------------------------------ |
| POST   | /api/users/register | Register user                  |
| POST   | /api/users/login    | Login                          |
| GET    | /api/products       | List products                  |
| GET    | /api/products/{id}  | Get product by ID              |
| POST   | /api/products       | Create product (auth required) |
| PUT    | /api/products/{id}  | Update product (owner only)    |
| DELETE | /api/products/{id}  | Delete product (ADMIN only)    |

---

## Development Notes

* Frontend currently includes **product listing UI only**
* Backend is fully functional for authentication and product management
* Future improvements:

    * Cart & checkout
    * Order management

---

## Author

E-commerce project built with Spring Boot.
