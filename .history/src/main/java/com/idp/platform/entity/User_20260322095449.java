package com.idp.platform.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
public class User {
    @Id @GeneratedValue
    private Long id;

    private String email;
    private String name;
}
