package com.idp.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idp.platform.entity.Deployment;
import com.idp.platform.repository.DeploymentRepository;

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

    @GetMapping("/{id}")
    public Deployment get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}
