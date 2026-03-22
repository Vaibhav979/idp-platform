package com.idp.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idp.platform.entity.Deployment;

public interface DeploymentRepository extends JpaRepository<Deployment, Long> {
    
}
