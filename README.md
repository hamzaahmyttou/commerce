# E-commerce Platform (Full Stack)

This project is a full-stack e-commerce application composed of:

* **Backend**: Spring Boot REST API (JWT-secured)
* **Frontend**: Angular UI (Product listing)

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

* Angular (CLI)
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
    * UI authentication flow
    * Admin dashboard

---

## Author

Full-stack e-commerce project built with Spring Boot and Angular.
