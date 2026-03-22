package com.idp.platform.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table
public class User {
    @Id @GeneratedValue
    private Long id;

    private String email;
    private String name;
}
