package com.example.auth_service.repository;

import com.example.auth_service.model.ProfileRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProfileRoleRepository extends JpaRepository<ProfileRole, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ProfileRole pr WHERE pr.profile.id = :profileId")
    void deleteByProfileId(@Param("profileId") Long profileId);

}
