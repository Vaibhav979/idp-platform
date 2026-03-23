package com.idp.platform.services;

import java.io.File;

import org.springframework.stereotype.Service;

@Service
public class RepoService {
    
    private static final String BASE_DIR = "repos/";

    public String cloneRepo(String repoUrl){
        try {
            validateRepoUrl(repoUrl);

            String repoName = repoUrl.substring(repoUrl.lastIndexOf("/") + 1).replace(".git","");
            String targetDir = BASE_DIR + repoName;

            File baseDir = new File(BASE_DIR);
            if(!baseDir.exists()) {
                baseDir.mkdirs();
            }

            File dir = new File(targetDir);
            if (dir.exists()) {
                return targetDir;
            }

            ProcessBuilder builder = new ProcessBuilder("git", "clone", repoUrl, targetDir);

            Process process = builder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("Git clone failed");
            }

            return targetDir;
        } catch (Exception e) {
            throw new RuntimeException("Error cloning repo", e);
        }
    }

    private void validateRepoUrl(String repoUrl) {
        if (repoUrl == null || !repoUrl.startsWith("https://") || !repoUrl.endsWith(".git")) {
            throw new IllegalArgumentException("Invalid repo URL");
        }
    }

}
