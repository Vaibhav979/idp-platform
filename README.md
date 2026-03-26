# Internal Developer Platform (IDP)

A backend system that allows users to deploy applications from Git repositories, similar to platforms like Heroku or Render.

This project focuses on building a developer-first deployment system where code can move from repository to running container with minimal manual effort.

## Features (MVP)

- Create and manage projects
- Clone repositories from GitHub
- Detect project type (Spring Boot)
- Auto-generate Dockerfiles
- Build Docker images
- Deploy containers with port mapping
- Track deployment status

## Architecture

### Tech Stack

- Backend: Spring Boot
- Database: PostgreSQL / H2
- Containerization: Docker
- Build Tool: Maven
- Version Control: Git

### Deployment Flow

User → API Request → IDP Backend
→ Clone Repository
→ Detect Project Type
→ Generate Dockerfile
→ Build Docker Image
→ Run Container
→ Return Deployment Info

## Core Entities

- User – Platform user (future scope)
- Project – Contains repo details
- Deployment – Tracks deployment lifecycle

## Deployment Lifecycle

PENDING → CLONING → BUILDING → DOCKERIZING → DEPLOYING → RUNNING / FAILED

## APIs

### Create Project (git clone)

POST /project/from-repo

### Request Body

{
"name": "spring-petclinic",
"repoUrl": "https://github.com/spring-projects/spring-petclinic",
"projectType": "SPRING_BOOT"
}

### Trigger Deployment

POST /deployments/projects/{projectId}

### Get Deployment

GET /deployments/{id}

## How to Run

```bash
docker compose up --build
```

## Dockerfile (Generated Example)

FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/\*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

## Project Structure

idp-backend/
│
├── controller/
├── service/
├── entity/
├── repository/
├── docker/
├── util/
└── repos/ # Cloned repositories

## Vision

This project aims to evolve into a full-fledged Internal Developer Platform (IDP) with CI/CD, GitOps, Kubernetes orchestration, and observability.
