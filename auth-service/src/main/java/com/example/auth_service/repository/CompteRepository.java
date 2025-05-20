package com.example.auth_service.repository;

import com.example.auth_service.model.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public
interface CompteRepository extends JpaRepository<Compte, Long> {
    Optional<Compte> findByEmail(String email);

    Optional<Compte> findByUuid(String uuid);

    Optional<Compte> findByIdAndOtp(Long id, String otp);

    @Query("select a from Compte a where a.isAdmin=true ")
    List<Compte> getAllCompteAdmin();
}
