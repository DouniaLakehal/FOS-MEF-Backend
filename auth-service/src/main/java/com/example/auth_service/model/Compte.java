package com.example.auth_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Compte implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom_ar;
    private String nom_fr;
    private long tel;
    private long fax;
    private String email;
    private String password;
    private String uuid;
    private String otp;
    private Boolean isActive=false;
    private Boolean isAdmin=false;


    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public void update(Compte compte) {
        this.nom_ar=compte.getNom_ar();
        this.nom_fr=compte.getNom_fr();
        this.tel=compte.getTel();
        this.fax=compte.getFax();
    }
}

