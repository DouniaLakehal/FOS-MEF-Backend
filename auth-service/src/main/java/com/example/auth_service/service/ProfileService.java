package com.example.auth_service.service;

import com.example.auth_service.dto.ProfileRoleDTO;
import com.example.auth_service.model.Profile;
import com.example.auth_service.model.ProfileRole;
import com.example.auth_service.model.Role;
import com.example.auth_service.repository.ProfileRepository;
import com.example.auth_service.repository.ProfileRoleRepository;
import com.example.auth_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ProfileRoleRepository profileRoleRepository;

    public List<Profile> getAllProfilesWithRoles() {
        return profileRepository.findAllProfilesWithRoles(); // or just findAll() if using @EntityGraph
    }

    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Transactional
    public Profile saveProfile(Profile profile) {
        boolean isUpdate = profile.getId() != null;

        if (isUpdate) {
            profileRoleRepository.deleteByProfileId(profile.getId());
        }

        Profile savedProfile = profileRepository.save(profile);

        if (profile.getProfileRoles() != null) {
            for (ProfileRole pr : profile.getProfileRoles()) {
                pr.setProfile(savedProfile);
                Role role = roleRepository.findById(pr.getRole().getId())
                        .orElseThrow(() -> new RuntimeException("Role not found"));
                pr.setRole(role);
                profileRoleRepository.save(pr);
            }
        }

        return savedProfile;
    }


    public Profile getProfileById(Long id) {
        return profileRepository.findById(id).orElse(null);
    }

    public Profile updateProfile(Long id, Profile profile) {
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        existingProfile.setName_en(profile.getName_en());
        existingProfile.setName_ar(profile.getName_ar());
        return profileRepository.save(existingProfile);
    }
}
