package com.idp.platform.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

@Service
public class DockerService {

    public String buildImage(String repoPath, String imageName) {
        try {
            ProcessBuilder builder = new ProcessBuilder("docker", "build", "-t", imageName, repoPath);

            builder.inheritIO();

            Process process = builder.start();
            int exit = process.waitFor();

            if (exit != 0) {
                throw new RuntimeException("Docker Build Failed");
            }
            return imageName;
        } catch (Exception e) {
            throw new RuntimeException("Error building Docker image", e);
        }
    }

    public String runContainer(String imageName, int port) {
        try {
            ProcessBuilder builder = new ProcessBuilder("docker", "run", "-d", "-p", port + ":8080", imageName);

            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String containerId = reader.readLine();
            process.waitFor();

            return containerId;
        } catch (Exception e) {
            throw new RuntimeException("Error running Docker container", e);
        }
    }
}
