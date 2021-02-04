package com.web.brickbuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<com.web.brickbuddy.model.Role, Long> {
    
	com.web.brickbuddy.model.Role findByRole(String role);
	
}