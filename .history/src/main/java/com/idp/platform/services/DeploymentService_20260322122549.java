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
        Deployment deployment = new Deployment();
        deployment.setProject(project);
        deployment.setStatus(DeploymentStatus.PENDING);

        deployment = deploymentRepo.save(deployment);

        simulatePipeline(deployment);

        return deployment;
    }

    private void simulatePipeline(Deployment deployment) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                updateStatus(deployment, DeploymentStatus.BUILDING);

                Thread.sleep(2000);
                updateStatus(deployment, DeploymentStatus.DEPLOYING);

                Thread.sleep(2000);
                updateStatus(deployment, DeploymentStatus.RUNNING);

            } catch (Exception e) {
                updateStatus(deployment, DeploymentStatus.FAILED);
            }
        }).start();
    }

    private void updateStatus(Deployment deployment, DeploymentStatus status) {
            
    }
    
}
