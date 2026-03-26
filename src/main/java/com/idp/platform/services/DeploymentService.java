package com.idp.platform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idp.platform.entity.Deployment;
import com.idp.platform.entity.Project;
import com.idp.platform.enums.DeploymentStatus;
import com.idp.platform.repository.DeploymentRepository;
import com.idp.platform.repository.ProjectRepository;
import com.idp.platform.services.DeploymentService;

@Service
public class DeploymentService {

    @Autowired
    private DeploymentRepository deploymentRepo;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private DockerfileService dockerfileService;

    @Autowired
    private DockerService dockerService;

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

        trigger(deployment, project);

        return deployment;
    }

    private void trigger(Deployment deployment, Project project) {
        new Thread(() -> {
            try {
                System.out.println("Generating Dockerfile...");
                updateStatus(deployment, DeploymentStatus.BUILDING);

                dockerfileService.generateDockerfile(
                        project.getProjectType(),
                        project.getActualRepoPath());

                System.out.println("Building Docker image...");
                String imageName = "app-" + project.getId();
                dockerService.buildImage(project.getActualRepoPath(), imageName);

                System.out.println("Running container...");
                updateStatus(deployment, DeploymentStatus.DEPLOYING);

                int port = 3000 + project.getId().intValue();
                dockerService.runContainer(imageName, port);

                updateStatus(deployment, DeploymentStatus.RUNNING);

                System.out.println("Deployment successful!");

            } catch (Exception e) {
                e.printStackTrace();
                updateStatus(deployment, DeploymentStatus.FAILED);
            }
        }).start();
    }

    private void updateStatus(Deployment deployment, DeploymentStatus status) {
        deployment.setStatus(status);
        deploymentRepo.save(deployment);
    }
}
