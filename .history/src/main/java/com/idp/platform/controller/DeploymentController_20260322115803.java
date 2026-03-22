package com.idp.platform.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deployments")
public class DeploymentController {
    
    @Autowired
    private DeploymentService service;

    @Autowired
    private DeploymentRepository repo;

    @PostMapping("/projects/{projectID")
}
