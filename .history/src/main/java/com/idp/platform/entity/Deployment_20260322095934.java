package com.idp.platform.entity;



@Entity
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
