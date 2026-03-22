package com.idp.platform.services;

import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepo;

    public Project createProject(Project project) {
        return projectRepo.save(project)
    }
}
