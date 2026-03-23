# System Design

## Project Creation Flow

1. User submits Git repository URL
2. Backend clones repository
3. System detects project type:

   * pom.xml → Spring Boot
   * package.json → Node.js
   * requirements.txt → Python

4. Project is stored with:

   * repoPath
   * projectType

---

## Deployment Flow

1. User triggers deployment
2. Deployment linked to project
3. Project type determines deployment strategy (future)

---

## Purpose

This enables dynamic build and deployment pipelines based on project structure.
