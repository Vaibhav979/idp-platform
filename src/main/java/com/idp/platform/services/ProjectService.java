package com.idp.platform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idp.platform.entity.Project;
import com.idp.platform.repository.ProjectRepository;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository repo;

    @Autowired
    private RepoService repoService;

    @Autowired
    private ProjectDetectionService detectionService;

    public Project createProjectFromRepo(String repoUrl) {
        
        String repoPath = repoService.cloneRepo(repoUrl);
        String type = detectionService.detectProjectType(repoPath);

        Project project = new Project();
        project.setName(repoUrl.substring(repoUrl.lastIndexOf("/")+1));
        project.setRepoUrl(repoUrl);
        project.setRepoPath(repoPath);
        project.setProjectType(type);

        return repo.save(project);
    }
}
