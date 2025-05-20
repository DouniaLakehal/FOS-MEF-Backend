package com.example.auth_service.repository;

import com.example.auth_service.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public
interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("SELECT DISTINCT p FROM Profile p LEFT JOIN FETCH p.profileRoles pr LEFT JOIN FETCH pr.role")
    List<Profile> findAllProfilesWithRoles();
}
