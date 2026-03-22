package com.idp.platform.services;

@Service
public class DeploymentService {

    @Autowired
    private DeploymentRepository deploymentRepo;

    @Autowired
    private ProjectRepository projectRepo;

    private Deployment triggerDeployment(Long projectId) {
        Project project = projectRepo.findById(projectId).orElseThrow();
        Deployement deployment = new Deployment();
        deployment.setProject(project);
        deployment.setStatus(DeploymentStatus.PENDING);

        deployment = deploymentRepo.save(deployment);

        simulatePipeline(deployment);

        return deployment;
    }
    
}
