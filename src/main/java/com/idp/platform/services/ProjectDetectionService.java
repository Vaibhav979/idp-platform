package com.idp.platform.services;

import java.io.File;

import org.springframework.stereotype.Service;

import com.idp.platform.entity.DetectionResult;

@Service
public class ProjectDetectionService {

    public DetectionResult detectProject(String repoPath) {
        File root = new File(repoPath);

        return findProject(root);
    }

    private DetectionResult findProject(File dir) {
        if (dir == null || !dir.exists())
            return null;

        File[] files = dir.listFiles();
        if (files == null)
            return null;

        for (File file : files) {

            if (file.isDirectory()) {
                DetectionResult result = findProject(file);
                if (result != null)
                    return result;
            }

            if (file.getName().equals("pom.xml")) {
                return new DetectionResult("SPRING_BOOT", dir.getAbsolutePath());
            }

            if (file.getName().equals("package.json")) {
                return new DetectionResult("NODE", dir.getAbsolutePath());
            }

            if (file.getName().equals("requirements.txt")) {
                return new DetectionResult("PYTHON", dir.getAbsolutePath());
            }
        }

        return null;
    }
}
