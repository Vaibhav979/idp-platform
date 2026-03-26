package com.idp.platform.services;

import java.io.File;
import java.io.FileWriter;

import org.springframework.stereotype.Service;

@Service
public class DockerfileService {

    public String generateDockerfile(String projectType, String repoPath) {

        String dockerfileContent = "";

        switch (projectType) {

            case "SPRING_BOOT":
                dockerfileContent = """
                        FROM maven:3.9.9-eclipse-temurin-17 AS builder

                        WORKDIR /app
                        COPY . .

                        RUN mvn clean package -DskipTests

                        FROM eclipse-temurin:17-jre-alpine

                        WORKDIR /app

                        COPY --from=builder /app/target/*.jar app.jar

                        EXPOSE 8080

                        ENTRYPOINT ["java","-jar","app.jar"]
                        """;
                break;

            case "NODE":
                dockerfileContent = """
                        FROM node:18
                        WORKDIR /app
                        COPY . .
                        RUN npm install
                        CMD ["npm","start"]
                        """;
                break;

            case "PYTHON":
                dockerfileContent = """
                        FROM python:3.10
                        WORKDIR /app
                        COPY . .
                        RUN pip install -r requirements.txt
                        CMD ["python","app.py"]
                        """;
                break;

            default:
                throw new RuntimeException("Unsupported project type");
        }

        writeDockerfile(repoPath, dockerfileContent);

        return repoPath + "/Dockerfile";
    }

    private void writeDockerfile(String repoPath, String content) {
        try {
            File file = new File(repoPath + "/Dockerfile");
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to write Dockerfile", e);
        }
    }
}
