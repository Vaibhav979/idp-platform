package com.idp.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.idp.platform.entity.Project;
import com.idp.platform.services.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService service;

    @PostMapping
    public Project create(@RequestBody Project project) {
        return service.createProject(project);
    }
}
