package com.idp.platform.entity;

import com.idp.platform.enums.DeploymentStatus;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Deployment {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Project project;

    @Enumerated(EnumType.STRING)
    private DeploymentStatus status;

    private String commitId;
    private String logs;
}
