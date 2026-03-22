package com.idp.platform.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Project {
    @Id @GeneratedValue
    private Long id;

    private String name;
    private String description;

}
