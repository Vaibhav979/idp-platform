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

    public Deployment triggerDeployment(Long projectId) {
        Project project = projectRepo.findById(projectId).orElseThrow();
        System.out.println("=== DEPLOYMENT STARTED ===");
        System.out.println("Project: " + project.getName());
        System.out.println("Type: " + project.getProjectType());
        System.out.println("Repo Path: " + project.getRepoPath());
        Deployment deployment = new Deployment();
        deployment.setProject(project);
        deployment.setStatus(DeploymentStatus.PENDING);

        deployment = deploymentRepo.save(deployment);

        simulatePipeline(deployment, project);

        return deployment;
    }

    private void simulatePipeline(Deployment deployment, Project project) {
    new Thread(() -> {
        try {
            Thread.sleep(2000);
            updateStatus(deployment, DeploymentStatus.BUILDING);
            
            System.out.println("Building " + project.getProjectType() + " app...");

            Thread.sleep(2000);
            updateStatus(deployment, DeploymentStatus.DEPLOYING);

            System.out.println("Deploying from: " + project.getRepoPath());

            Thread.sleep(2000);
            updateStatus(deployment, DeploymentStatus.RUNNING);

            System.out.println("Deployment successful!");

        } catch (Exception e) {
            updateStatus(deployment, DeploymentStatus.FAILED);
        }
        }).start();
    }

    private void updateStatus(Deployment deployment, DeploymentStatus status) {
        deployment.setStatus(status);
        deploymentRepo.save(deployment);
    }
}
