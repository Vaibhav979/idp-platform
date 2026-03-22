package com.idp.platform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idp.platform.entity.Deployment;
import com.idp.platform.entity.Project;
import com.idp.platform.enums.DeploymentStatus;
import com.idp.platform.repository.DeploymentRepository;
import com.idp.platform.repository.ProjectRepository;

@Service
public class DeploymentService {

    @Autowired
    private DeploymentRepository deploymentRepo;

    @Autowired
    private ProjectRepository projectRepo;

    private Deployment triggerDeployment(Long projectId) {
        Project project = projectRepo.findById(projectId).orElseThrow();
        Deployment deployment = new Deployment();
        deployment.setProject(project);
        deployment.setStatus(DeploymentStatus.PENDING);

        deployment = deploymentRepo.save(deployment);

        simulatePipeline(deployment);

        return deployment;
    }
    
}
