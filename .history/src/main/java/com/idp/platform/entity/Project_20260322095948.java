package com.idp.platform.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
@Data
public class Project {
    @Id @GeneratedValue
    private Long id;

    private String name;
    private String repoUrl;

    @ManyToOne
    private User user;
}
