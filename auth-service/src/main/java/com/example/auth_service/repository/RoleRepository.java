package com.example.auth_service.repository;

import com.example.auth_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public
interface RoleRepository extends JpaRepository<Role, Long> {
}
