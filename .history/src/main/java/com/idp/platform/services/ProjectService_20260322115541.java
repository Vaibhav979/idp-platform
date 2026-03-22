package com.idp.platform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idp.platform.entity.Project;
import com.idp.platform.repository.ProjectRepository;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepo;

    public Project createProject(Project project) {
        return projectRepo.save(project);
    }
}
