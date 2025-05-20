package com.example.auth_service.service;

import com.example.auth_service.model.Compte;
import com.example.auth_service.repository.CompteRepository;
import com.example.auth_service.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CompteService {

    @Autowired
    CompteRepository compteRepository;
    @Autowired
    JwtUtil jwtService;

    @Autowired
    EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    public CompteService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public static
    String generateOTP() {
        int otp = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.format("%06d", otp);
    }

    public Optional<Compte> findByEmail(String email) {
        return compteRepository.findByEmail(email);
    }

    public
    boolean emailExists(String email) {
        return compteRepository.findByEmail(email).isPresent();
    }

    public
    Compte saveCompte(Compte compte) {
        compte.setPassword(passwordEncoder.encode(compte.getPassword()));
        compte.setUuid(String.valueOf(UUID.randomUUID()));
        compte.setOtp(generateOTP());
        compteRepository.save(compte);
        emailService.activateAccount(compte);
        return compte;
    }

    public
    Compte saveCompteAdmin(Compte compte) {
        compte.setPassword(passwordEncoder.encode(compte.getPassword()));
        compte.setUuid(String.valueOf(UUID.randomUUID()));
        compte.setOtp(generateOTP());
        compte.setIsActive(true);
        compte.setIsAdmin(true);
        compteRepository.save(compte);
        //emailService.activateAccount(compte);
        return compte;
    }

    public ResponseEntity<Map<String, Object>> login(String email, String password) {
        Optional<Compte> optionalCompte = findByEmail(email);
        if (optionalCompte.isPresent()) {
            Compte compte = optionalCompte.get();

            if (passwordEncoder.matches(password, compte.getPassword()) && !compte.getIsAdmin()) {
                if (!compte.getIsActive()) {
                    return ResponseEntity.status(HttpStatus.LOCKED).body(Map.of("error", "Active your account"));
                }

                /*CompteDTO compteDTO = new CompteDTO(compte);*/

                //generate and send otp code
                compte.setOtp(generateOTP());
                compteRepository.save(compte);
                emailService.otpCode(compte);
                // Return the token and compte in the response
                Map<String, Object> response = new HashMap<>();
                response.put("compte", compte);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid password"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User  not found"));
        }
    }

    public
    ResponseEntity<Map<String, Object>> login_admin(String email, String password) {
        Optional<Compte> optionalCompte = findByEmail(email);
        if (optionalCompte.isPresent()) {
            Compte compte = optionalCompte.get();

            if (passwordEncoder.matches(password, compte.getPassword()) && compte.getIsAdmin()) {
                if (!compte.getIsActive()) {
                    return ResponseEntity.status(HttpStatus.LOCKED).body(Map.of("error", "Active your account"));
                }

                // Convert Compte to CompteDTO
                /* CompteDTO compteDTO = new CompteDTO(compte);*/

                //generate and send otp code
                compte.setOtp(generateOTP());
                compteRepository.save(compte);
                emailService.otpCode(compte);

                // Return the token and compte in the response
                Map<String, Object> response = new HashMap<>();
                response.put("compte", compte);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid password"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User  not found"));
        }
    }

    public
    ResponseEntity<Map<String, Object>> checkOtp(Long compteId, String otp) {
        Compte compte = compteRepository.findByIdAndOtp(compteId, otp)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Return the token in the response
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwtService.generateToken(compte.getEmail()));
        return ResponseEntity.ok(response);
    }

    public
    void activeAccount(Compte compte) {
        compte.setIsActive(true);
        compteRepository.save(compte);
    }

    public
    Optional<Compte> findByUuid(String uuid) {
        return compteRepository.findByUuid(uuid);
    }

    public
    void resendCodeOtp(Long id) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        compte.setOtp(generateOTP());
        compteRepository.save(compte);
        emailService.otpCode(compte);
        //whatsAppService.sendOtp(compte);
    }

    public
    Compte getCompteById(Long id) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte not found"));
        return compte;
    }

    public List<Compte> getCompteAll() {
        return compteRepository.getAllCompteAdmin();
    }

    public
    boolean checkPassword(String password, Long id) {
        return passwordEncoder.matches(password, compteRepository.getReferenceById(id).getPassword());
    }

    public
    void changePassword(String password, Long id) {
        String newPassword = passwordEncoder.encode(password);
        Compte compte = compteRepository.getReferenceById(id);
        compte.setPassword(newPassword);
        compteRepository.save(compte);
    }

    public
    void updateCompte(Compte compte) {
        Compte c = compteRepository.getReferenceById(compte.getId());
        c.setNom_fr(compte.getNom_fr());
        c.setNom_ar(compte.getNom_ar());
        c.setTel(compte.getTel());
        c.setFax(compte.getFax());
        c.setEmail(compte.getEmail());
        c.setPassword(passwordEncoder.encode(compte.getPassword()));
        //c.setProfile(compte.getProfile());
        //c.update(compte);
        compteRepository.save(c);
    }

    public List<Compte> getAllCompte() {
        return compteRepository.getAllCompteAdmin();
    }

    public Compte getById(long id) {
        return compteRepository.getReferenceById(id);
    }
}
