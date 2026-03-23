package com.idp.platform.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idp.platform.entity.Project;
import com.idp.platform.services.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService service;

    @PostMapping("/from-repo")
    public Project createFromRepo(@RequestBody Map<String, String> body) {
        String repoUrl = body.get("repoUrl");
        return service.createProjectFromRepo(repoUrl);
    }
}
