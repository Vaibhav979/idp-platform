# System Design

## Overview

**System**: Internal Developer Platform (IDP) backend for automated project deployment from Git repositories.

**Actors**:

- Developer/User: Submits repo, triggers deploys, monitors status.

**Boundaries**:

- Input: Git repo URLs.
- Output: Running Docker containers with port mappings.
- External: Git providers, Docker daemon (local).

**Goals**:

- End-to-end deploy in <5 mins (MVP simulated).
- Auto-detect project type for Dockerfile generation.
- Track lifecycle transparently.

## Functional Requirements

1. **Project Management**:
   - Create: POST /projects {name, repoUrl} → clone, detect type, store.
   - List/View: GET /projects/{id}.

2. **Project Detection**:
   - Scan root files: pom.xml (Spring Boot), package.json (Node), requirements.txt (Python).
   - Store DetectionResult.

3. **Deployment**:
   - Trigger: POST /deployments/{projectId}.
   - Pipeline: Clone → Detect → Gen Dockerfile → Build → Deploy → Status.
   - Poll: GET /deployments/{id}.

4. **Status Tracking**: Enum updates persisted to DB.

## Non-Functional Requirements

| Category      | Requirement                                  |
| ------------- | -------------------------------------------- |
| Performance   | Deploy <5min, API <200ms                     |
| Availability  | 99% (docker-compose)                         |
| Scalability   | 10 concurrent deploys now; horizontal future |
| Security      | Localhost only (future JWT)                  |
| Observability | DB logs, status polling                      |

**Constraints**: Local Docker/Git, no internet-restricted repos.

## Detailed Flows

### 1. Project Creation Flow

**Sequence** (Mermaid):

```
sequenceDiagram
    participant U as User
    participant PC as ProjectController
    participant PS as ProjectService
    participant RS as RepoService
    participant PDS as ProjectDetectionService
    participant DB as PostgreSQL

    U->>PC: POST /projects {repoUrl}
    PC->>PS: createProject()
    PS->>RS: cloneRepo(repoUrl)
    RS-->>PS: repoPath
    PS->>PDS: detect(repoPath)
    PDS-->>PS: projectType
    PS->>DB: save Project + DetectionResult
    DB-->>PS: entity IDs
    PS-->>PC: Project JSON
```

**Steps**:

1. Clone to local ./repos/{id}.
2. Detect type (file signatures).
3. Persist.

### 2. Deployment Flow

**Sequence**:

```
sequenceDiagram
    participant U as User
    participant DC as DeploymentController
    participant DS as DeploymentService
    participant DFS as DockerfileService
    participant DoS as DockerService
    participant DB as PostgreSQL

    U->>DC: POST /deployments/{pid}
    DC->>DS: createDeployment()
    DS->>DB: Deployment(PENDING)
    par Async Pipeline
        DS->>RS: ensureCloned()
        DS->>DFS: generateDockerfile(projectType)
        DS->>DoS: docker build
        DS->>DoS: docker run -p
    and Status Updates
        DS->>DB: UPDATE BUILDING
        DS->>DB: UPDATE RUNNING
    end
    U->>DC: GET /deployments/{id}
    DC->>DB: latest status/logs
```

**Status Transitions**:
PENDING → CLONING → BUILDING → DOCKERIZING → DEPLOYING → RUNNING | FAILED.

## Data Flows

**Inputs/Outputs**:

- **Create Project**: repoUrl → Project{id, repoPath, type}.
- **Deploy**: projectId → Deployment{id, status, logs, containerId, port}.
- **External Calls**: `git clone`, `docker build/run`.

**DB Access Patterns**:

- Write: CREATE/UPDATE (status).
- Read: By ID/project, Recent deploys.

## Components

(Reuses ARCHITECTURE.md layers):

- **Services**: RepoService (git), ProjectDetectionService (file scan), DockerfileService (templates), DockerService (CLI exec), DeploymentService (orchestrator/thread).
- **Async**: Thread per deployment (current).

## Edge Cases & Error Handling

1. **Invalid Repo**: Clone fail → FAILED, log error.
2. **Unknown Type**: Default GENERIC → basic Dockerfile.
3. **Build Fail**: Timeout/catch → FAILED, cleanup container.
4. **Concurrent Deploys**: Separate threads/containers.
5. **Large Repo**: Limit size → error.
6. **Docker Unavailable**: Status DEPLOY_FAILED.

**Rollback**: docker rm/stop on fail.

## Assumptions

- Docker/Git installed/accessible.
- Repos public/small.
- Local FS persistent (docker volumes).

This design enables extensible pipelines (future: Kubernetes manifests).
