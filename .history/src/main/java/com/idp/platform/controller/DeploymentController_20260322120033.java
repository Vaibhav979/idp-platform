package com.idp.platform.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deployments")
public class DeploymentController {
    
    @Autowired
    private DeploymentService service;

    @Autowired
    private DeploymentRepository repo;

    @PostMapping("/projects/{projectId}")
    public Deployment trigger(@PathVariable Long projectId) {
        return service.triggerDeployment(projectId);
    }

    @GetMapping("/{id")
    public Deployment get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow()
    }
}
