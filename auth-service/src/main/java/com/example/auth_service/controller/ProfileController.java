package com.example.auth_service.controller;

import com.example.auth_service.model.Profile;
import com.example.auth_service.model.Role;
import com.example.auth_service.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @GetMapping("/list")
    public ResponseEntity<List<Profile>> getAllProfile() {
        List<Profile> profile = profileService.getAllProfilesWithRoles();
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRole() {
        List<Role> roles = profileService.getAllRole();
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/save")
    public Profile addProfile(@RequestBody Profile profile) {
        System.out.println("Received profile: " + profile);
        return profileService.saveProfile(profile);
    }

    @GetMapping("/profile_id")
    public ResponseEntity<Profile> getProfileById(@RequestParam Long id) {
        Profile profile = profileService.getProfileById(id);

        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(profile);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Profile> updateProfile(@PathVariable Long id, @RequestBody Profile profile) {
        Profile updatedProfile = profileService.updateProfile(id, profile);
        return ResponseEntity.ok(updatedProfile);
    }
}
