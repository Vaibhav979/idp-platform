# Architecture

## Introduction

The Internal Developer Platform (IDP) is a Spring Boot monolithic backend application designed to streamline the deployment of applications from Git repositories. It supports project creation, automatic project type detection, Dockerfile generation, Docker image building, container deployment, and real-time deployment status tracking.

**Current Scope**: MVP with simulated deployment pipeline (thread-based status updates). Future evolution towards real CI/CD, GitOps, and Kubernetes.

**Key Goals**:

- Reduce deployment friction for developers
- Automate Dockerfile generation based on project type
- Provide deployment lifecycle visibility
- Serve as foundation for full IDP (authentication, multi-tenancy, observability)

## High-Level Architecture

**Monolithic Architecture** (single deployable JAR):

```
┌─────────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│     API Layer       │    │  Business Layer  │    │ Persistence     │
│  (Controllers)      │◄──►│   (Services)     │◄──►│   Layer         │
│                     │    │                  │    │ (Repositories)  │
│ • /projects         │    │ • RepoService    │    │ • JpaRepository │
│ • /deployments      │    │ • DetectionSvc   │    │ • PostgreSQL    │
└─────────────────────┘    │ • DockerService  │    └─────────────────┘
                           │ • DeploymentSvc  │
                           └──────────────────┘
```

**Component Interaction** (Mermaid-like):

```
graph TD
    A[User API Call] --> B[Controller]
    B --> C[Service Orchestration]
    C --> D[Repo Clone & Detect]
    C --> E[Dockerfile Gen & Build]
    C --> F[Deploy Container]
    F --> G[Status Update DB]
    G --> H[API Response]
```

## Tech Stack

| Category         | Technology                      | Version/Details                     |
| ---------------- | ------------------------------- | ----------------------------------- |
| Framework        | Spring Boot                     | 3.3.5                               |
| Web              | Spring Web MVC                  | -                                   |
| Database ORM     | Spring Data JPA/Hibernate       | ddl-auto=update                     |
| Database         | PostgreSQL                      | docker-compose                      |
| Build Tool       | Maven                           | Wrapper (mvnw)                      |
| Language         | Java                            | 17                                  |
| Utilities        | Lombok                          | Annotation processor                |
| Containerization | Docker                          | compose.yml + generated Dockerfiles |
| Git              | JGit (inferred via RepoService) | Repository cloning                  |
| Testing          | Spring Boot Test                | -                                   |

**application.properties**:

```
spring.application.name=idp
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.datasource.url=jdbc:postgresql://postgres:5432/idp
```

## Data Model

**Core Entities**:

- **User** (future): id, username, email, projects.
- **Project**: id, name, repoUrl, repoPath (local clone), projectType (SPRING_BOOT/NODE/PYTHON), userId.
- **Deployment**: id, projectId, status (PENDING/CLONING/BUILDING/DOCKERIZING/DEPLOYING/RUNNING/FAILED), logs, containerId, portMapping.
- **DetectionResult**: id, projectId, detectedType, confidence.

**Relations**:

```
Project 1 ↔ * Deployment
User 1 ↔ * Project
Project 1 ↔ 1 DetectionResult
```

**Text ER Diagram**:

```
[User] --1:N--> [Project] --1:N--> [Deployment]
                    |
                1:1 [DetectionResult]
```

**Database Schema** (auto-generated via JPA):

- Tables: users, projects, deployments, detection_results
- Indexes: On projectId, status for queries.

## API Layer

**REST Controllers** (spring-boot-starter-web):

- **ProjectController**: POST /projects (from repoUrl → clone/detect/store).
- **DeploymentController**: POST /deployments/{projectId} (trigger), GET /deployments/{id} (status).

**Example Payloads** (from API.md):

```json
POST /projects
{
  "name": "my-app",
  "repoUrl": "https://github.com/user/repo"
}
```

**Validation**: @Valid, Bean Validation (spring-boot-starter-validation).

**Error Handling**: Global @ControllerAdvice (future).

## Business Logic Layer

**Services** (@Service):

1. **RepoService**: Git clone to local repoPath.
2. **ProjectDetectionService**: Scan files (pom.xml → SPRING_BOOT, package.json → NODE, requirements.txt → PYTHON).
3. **DockerfileService**: Generate type-specific Dockerfile (e.g., Maven multi-stage for Spring Boot).
4. **DockerService**: docker build, docker run -p mapping, capture logs/containerId.
5. **ProjectService**: CRUD for projects.
6. **DeploymentService**: Orchestrate flow (async thread: PENDING → update status → RUNNING), persist logs.

**Deployment Flow** (simulated via threads):

```
User triggers → Create Deployment(PENDING)
                ↓ (DeploymentService thread)
CLONING (RepoService) → BUILDING (Docker build) → DOCKERIZING (gen) → DEPLOYING (run) → RUNNING
                                                      or → FAILED (error logs)
Polling: GET /deployments/{id} → latest status
```

## Persistence Layer

**Spring Data JPA**:

- Interfaces: ProjectRepository, DeploymentRepository extends JpaRepository.
- Queries: findByProjectId, findByStatus.
- Config: PostgreSQL via docker-compose, H2 commented out.

## Infrastructure & Deployment

**docker-compose.yml**:

```
Services:
- backend: Spring Boot JAR on :8080
- postgres: :5432, db=idp, persists /var/lib/postgresql/data
Networking: Internal (postgres hostname resolves)
```

**Run**:

```bash
mvn clean package
docker-compose up --build
```

**Local Repos**: Cloned to ./repos/ (inferred).

## Current Limitations

| Aspect        | Limitation        | Workaround                 |
| ------------- | ----------------- | -------------------------- |
| Deployment    | Thread simulation | Docker CLI calls           |
| Auth          | None              | Future Spring Security/JWT |
| Storage       | Local filesystem  | Volume mounts              |
| Scalability   | Single instance   | -                          |
| Observability | Basic logs        | DB status polling          |

## Future Evolution

| Current           | Phase 1 (Auth/Real CI) | Phase 2 (GitOps/K8s)      |
| ----------------- | ---------------------- | ------------------------- |
| Thread simulation | GitHub Actions webhook | ArgoCD sync               |
| Enum status       | WebSocket updates      | Event-driven (Kafka)      |
| Logs field        | ELK stack              | Centralized observability |
| Local Docker      | Docker Swarm           | Kubernetes + Helm         |
| Monolith          | Microservices          | Service mesh (Istio)      |

**Roadmap**:

1. Add Spring Security + JWT.
2. Real Git integration (webhooks).
3. Kubernetes manifests generation.
4. Multi-tenant support.

## Diagrams

**Deployment Flow** (Mermaid):

```
sequenceDiagram
    participant U as User
    participant C as Controller
    participant DS as DeploymentService
    participant RS as RepoService
    participant DB as PostgreSQL
    U->>C: POST /deployments/{pid}
    C->>DS: trigger()
    DS->>RS: clone()
    Note over DS: Async status updates
    DS->>DB: UPDATE status=RUNNING
    U->>C: GET /deployments/{id}
    C->>DB: status
```
