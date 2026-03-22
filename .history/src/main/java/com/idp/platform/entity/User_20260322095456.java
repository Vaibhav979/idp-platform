package com.idp.platform.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class User {
    @Id @GeneratedValue
    private Long id;

    private String email;
    private String name;
}
