# FluxKart Identity Reconciliation

A Spring Boot application that provides identity reconciliation services for FluxKart e-commerce platform. This service helps link and manage customer contact information (email and phone numbers) to prevent duplicate customer records.

## Overview

FluxKart Identity Reconciliation is designed to identify and consolidate customer contacts across multiple interactions. When a customer makes a purchase using different combinations of email addresses and phone numbers, this service intelligently links these contacts together, maintaining a primary contact and associating related secondary contacts.

## Features

- **Contact Identification**: Automatically identifies and links customer contacts based on email and phone number
- **Primary/Secondary Contact Management**: Maintains a hierarchical structure with one primary contact and multiple secondary contacts
- **Duplicate Prevention**: Prevents duplicate customer records by consolidating contact information
- **RESTful API**: Simple POST endpoint for contact identification
- **PostgreSQL Integration**: Robust data persistence with JPA/Hibernate
- **Dockerized Deployment**: Ready-to-deploy Docker container

## Tech Stack

- **Java 21**: Modern Java development
- **Spring Boot 4.0.3**: Application framework
- **Spring Data JPA**: Data persistence layer
- **PostgreSQL**: Relational database
- **Maven**: Dependency management and build tool
- **Docker**: Containerization

## Project Structure

```
fluxkart-identity-reconciliation/
├── src/
│   ├── main/
│   │   ├── java/com/bitespeed/fluxkart/
│   │   │   ├── controllers/          # REST controllers
│   │   │   │   └── ContactController.java
│   │   │   ├── entity/               # JPA entities
│   │   │   │   └── Contact.java
│   │   │   ├── enums/                # Enums
│   │   │   │   └── LinkedPrecedence.java
│   │   │   ├── exceptions/           # Custom exceptions
│   │   │   │   └── ContactAlreadyExistsException.java
│   │   │   ├── repositories/         # Data repositories
│   │   │   │   └── ContactRepository.java
│   │   │   ├── requests/             # Request DTOs
│   │   │   │   └── ContactRequest.java
│   │   │   ├── responses/            # Response DTOs
│   │   │   │   └── ContactResponse.java
│   │   │   ├── services/             # Business logic
│   │   │   │   └── ContactService.java
│   │   │   └── FluxkartApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                         # Test files
├── Dockerfile
├── pom.xml
└── README.md
```

## Prerequisites

- Java 21 or higher
- Maven 3.9+
- PostgreSQL database
- Docker (optional, for containerized deployment)

## Configuration

### Database Configuration

Update `src/main/resources/application.properties` with your PostgreSQL credentials:

```properties
spring.application.name=fluxkart

spring.datasource.url={YOUR_DATABASE_URL}
spring.datasource.username={YOUR_DATABASE_USERNAME}
spring.datasource.password={YOUR_DATABASE_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Replace the placeholders:
- `{YOUR_DATABASE_URL}`: e.g., `jdbc:postgresql://localhost:5432/fluxkart`
- `{YOUR_DATABASE_USERNAME}`: Your PostgreSQL username
- `{YOUR_DATABASE_PASSWORD}`: Your PostgreSQL password

## Getting Started

### Running Locally

1. **Clone the repository**
   ```bash
   git clone https://github.com/rishabhrawat05/fluxkart-identity-reconciliation.git
   cd fluxkart-identity-reconciliation
   ```

2. **Configure the database**
   - Update `application.properties` with your database credentials

3. **Build the project**
   ```bash
   ./mvnw clean package
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on `http://localhost:8080`

### Running with Docker

1. **Build the Docker image**
   ```bash
   docker build -t fluxkart-identity-reconciliation .
   ```

2. **Run the container**
   ```bash
   docker run -p 8080:8080 \
     -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/fluxkart \
     -e SPRING_DATASOURCE_USERNAME=your_username \
     -e SPRING_DATASOURCE_PASSWORD=your_password \
     fluxkart-identity-reconciliation
   ```

## 📡 API Documentation

### Identify Contact

**Endpoint:** `POST /identify`

**Description:** Identifies and links customer contacts based on email and phone number.

**Request Body:**
```json
{
  "email": "customer@example.com",
  "phoneNumber": "1234567890"
}
```

**Response:**
```json
{
  "primaryContactId": 1,
  "emails": ["customer@example.com", "customer2@example.com"],
  "phoneNumbers": ["1234567890", "0987654321"],
  "secondaryContactIds": [2, 3]
}
```

**Example using cURL:**
```bash
curl -X POST http://localhost:8080/identify \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@example.com",
    "phoneNumber": "1234567890"
  }'
```

## How It Works

1. **First Contact**: When a new contact is created with unique email/phone, it becomes a PRIMARY contact
2. **Matching Contacts**: If a contact matches an existing email OR phone number, it links to the existing PRIMARY contact
3. **Multiple Primaries**: If two PRIMARY contacts need to be linked, the older one remains PRIMARY and the newer becomes SECONDARY
4. **Contact Consolidation**: All secondary contacts are linked to a single primary contact, creating a unified customer identity

## Database Schema

### Contact Table

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key (auto-generated) |
| email | VARCHAR | Customer email address |
| phoneNumber | VARCHAR | Customer phone number |
| linkedId | BIGINT | Reference to primary contact ID |
| linkedPrecedence | ENUM | PRIMARY or SECONDARY |
| createdAt | TIMESTAMP | Record creation timestamp |
| updatedAt | TIMESTAMP | Record update timestamp |
| deletedAt | TIMESTAMP | Soft delete timestamp (nullable) |

### Screenshots
<img width="2037" height="1168" alt="Screenshot 2026-03-06 012243" src="https://github.com/user-attachments/assets/04a73dd1-8461-43a0-b882-6e20208987c0" />
<img width="2027" height="1158" alt="Screenshot 2026-03-06 012300" src="https://github.com/user-attachments/assets/a97be0f6-37e3-414a-a262-ebc51998a89e" />
<img width="2036" height="1186" alt="Screenshot 2026-03-06 012407" src="https://github.com/user-attachments/assets/9ddd0bad-88ed-45dd-b22f-7ed53a63aa84" />
<img width="2023" height="1165" alt="Screenshot 2026-03-06 012434" src="https://github.com/user-attachments/assets/ca17bd7f-e10e-408e-9981-7bc663163df8" />
<img width="2015" height="1203" alt="Screenshot 2026-03-06 014055" src="https://github.com/user-attachments/assets/100a44bf-d431-4f45-a375-e3cf659780cb" />
<img width="2017" height="1184" alt="Screenshot 2026-03-06 014207" src="https://github.com/user-attachments/assets/416137a1-5ac6-41e3-b647-e9a2dc16be86" />
<img width="2015" height="1218" alt="Screenshot 2026-03-06 014238" src="https://github.com/user-attachments/assets/e548ab25-001f-43a0-9067-da5c638eca47" />
<img width="1995" height="1169" alt="Screenshot 2026-03-06 014332" src="https://github.com/user-attachments/assets/2ba05f3e-9da5-4856-8bc0-a6db684bdbcf" />


## Author

**Rishabh Rawat** - [@rishabhrawat05](https://github.com/rishabhrawat05)
