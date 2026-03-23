# Deployment (Docker + PostgreSQL)

## Running the System

### Build and start services

```bash
mvn clean package
docker-compose up --build
```

---

## Services

### Backend

- Runs on port 8080

### PostgreSQL

- Database: idp
- Port: 5432

---

## Connection

Backend connects to PostgreSQL via:
jdbc:postgresql://postgres:5432/idp

---

## Notes

- Uses Docker internal networking (service name = hostname)
- Data persists across container restarts
