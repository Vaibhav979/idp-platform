# API Documentation (v1)

**Base URL**: `http://localhost:8080/api/v1`

**Content-Type**: `application/json`

**Authentication**: None (development). Future: Bearer JWT in `Authorization` header.

## Endpoints Summary

| Method | Endpoint            | Description                            |
| ------ | ------------------- | -------------------------------------- |
| POST   | `/projects`         | Create project from repo               |
| GET    | `/projects`         | List all projects                      |
| GET    | `/projects/{id}`    | Get project details                    |
| POST   | `/deployments`      | Trigger deployment for project         |
| GET    | `/deployments/{id}` | Get deployment status                  |
| GET    | `/deployments`      | List deployments (filter by projectId) |

## Detailed Endpoints

### Create Project

```
POST /projects
```

**Description**: Clone Git repo, detect type, create Project.

**Request Body**:

```json
{
  "name": "string (required)",
  "repoUrl": "https://github.com/user/repo (required)",
  "projectType": "SPRING_BOOT|NODE|PYTHON|GENERIC (optional)"
}
```

**Responses**:

- `201 Created`:

```json
{
  "id": 1,
  "name": "my-app",
  "repoUrl": "https://...",
  "repoPath": "/repos/1",
  "projectType": "SPRING_BOOT",
  "createdAt": "2024-01-01T00:00:00Z"
}
```

- `400 Bad Request`: Invalid repoUrl/name.
- `409 Conflict`: Repo already exists.

### List Projects

```
GET /projects
```

**Query Params**: `?page=0&size=10`

**Responses**:

- `200 OK`: Array of Projects (paginated).

### Get Project

```
GET /projects/{id}
```

**Responses**:

- `200 OK`: Project JSON.
- `404 Not Found`.

### Trigger Deployment

```
POST /deployments/{projectId}
```

**Path Param**: `projectId` (long)

**Request Body**: Empty `{}`

**Responses**:

- `201 Created`:

```json
{
  "id": 1,
  "projectId": 1,
  "status": "PENDING",
  "logs": [],
  "containerId": null,
  "portMapping": null,
  "createdAt": "2024-01-01T00:00:00Z"
}
```

- `404`: Project not found.

### Get Deployment

```
GET /deployments/{id}
```

**Responses**:

- `200 OK`: Deployment JSON (status updates real-time via poll).
- `404 Not Found`.

### List Deployments

```
GET /deployments?projectId=1
```

**Query**: `projectId` (optional, filter).

**Responses**:

- `200 OK`: Array of Deployments (latest first).

## Schemas

### Project

```json
{
  "id": "integer",
  "name": "string",
  "repoUrl": "string",
  "repoPath": "string",
  "projectType": "enum[SPRING_BOOT, NODE, PYTHON, GENERIC]",
  "createdAt": "string(ISO)"
}
```

### Deployment

```json
{
  "id": "integer",
  "projectId": "integer",
  "status": "enum[PENDING,CLONING,BUILDING,DOCKERIZING,DEPLOYING,RUNNING,FAILED]",
  "logs": ["array of strings"],
  "containerId": "string",
  "portMapping": "string (e.g., '0.0.0.0:3000->8080/tcp')",
  "createdAt": "string(ISO)"
}
```

## Error Responses

**Standard**:

```json
{
  "timestamp": "2024-01-01T00:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/projects"
}
```

**Codes**:

- 400: Invalid input.
- 404: Not found.
- 409: Duplicate.
- 500: Internal (Docker fail).

## Usage Example (curl)

```bash
# Create project
curl -X POST http://localhost:8080/api/v1/projects \
  -H "Content-Type: application/json" \
  -d '{"name":"test","repoUrl":"https://github.com/spring-projects/spring-petclinic"}'

# Trigger deploy
curl -X POST http://localhost:8080/api/v1/deployments/1

# Poll status
curl http://localhost:8080/api/v1/deployments/1
```

**Notes**: Poll GET every 5s for status. Future: WebSockets.
