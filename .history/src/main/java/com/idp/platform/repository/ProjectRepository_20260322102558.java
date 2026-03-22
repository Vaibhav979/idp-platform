package com.idp.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idp.platform.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long>{
    
}
