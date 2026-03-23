package com.idp.platform.services;

import java.io.File;

import org.springframework.stereotype.Service;

@Service
public class ProjectDetectionService {
    
    public String detectProjectType(String repoPath) {
        File dir = new File(repoPath);

        if (new File(dir, "pom.xml").exists()) {
            return "SPRING_BOOT";
        }

        if (new File(dir, "package.json").exists()) {
            return "NODE";
        }

        if (new File(dir, "requirements.txt").exists()) {
            return "PYTHON";
        }

        return "UNKNOWN";
    }

}
