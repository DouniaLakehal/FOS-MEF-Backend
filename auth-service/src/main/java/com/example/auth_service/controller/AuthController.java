package com.example.auth_service.controller;

import com.example.auth_service.model.Compte;
import com.example.auth_service.security.JwtUtil;
import com.example.auth_service.service.CompteService;
import com.example.auth_service.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public
class AuthController {

    @Autowired
    CompteService compteService;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtUtil jwtService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        return compteService.login(email, password);
    }

    @PostMapping("/admin/login")
    public
    ResponseEntity<Map<String, Object>> login_admin(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        return compteService.login_admin(email, password);
    }

    @PostMapping("/register")
    public
    ResponseEntity<?> registerCompte(@RequestBody Compte compte) {
        // Check if the email already exists
        if (compteService.emailExists(compte.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }

        Compte savedCompte = compteService.saveCompte(compte);
        return ResponseEntity.ok(savedCompte);
    }

    /*@GetMapping("/active")
    public String activateAccount() {
        emailService.otpCode(compteRepository.getById(5l));
        return "Email envoyed with success";
    }*/

    @PutMapping("/activeAccount")
    public
    String activeAccount(@RequestBody Map<String, String> request) {
        Compte compte = compteService.findByUuid(request.get("uuid"))
                .orElseThrow(() -> new RuntimeException("Compte not found by this uuid: " + request.get("uuid")));

        compteService.activeAccount(compte);

        return "Account activated successfully";
    }

    @PostMapping("/checkOpt")
    public
    ResponseEntity<Map<String, Object>> checkOtpCode(@RequestBody Map<String, String> request) {
        Long id = Long.valueOf(request.get("id"));
        String otp = request.get("otp");
        return compteService.checkOtp(id, otp);
    }

    @PutMapping("/resendCode")
    public
    void resendCode(@RequestBody Map<String, Long> request) {
        compteService.resendCodeOtp(request.get("id"));
    }

    @GetMapping("/validateToken")
    public ResponseEntity<Compte> validateToken(@RequestParam String token) {
        if (jwtService.validateToken(token)) {
            String email = jwtService.extractUsername(token);
            Optional<Compte> compte = compteService.findByEmail(email);
            if (compte.isPresent()) {
                return ResponseEntity.ok(compte.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/compte")
    public ResponseEntity<Compte> getCompteById(@RequestParam Long id) {
        Compte compte = compteService.getCompteById(id);
        return ResponseEntity.ok(compte);
    }

    @GetMapping("/compte/all")
    public ResponseEntity<List<Compte>> getCompteAll() {
        List<Compte> comptes = compteService.getCompteAll();
        return ResponseEntity.ok(comptes);
    }


    /*@GetMapping("/activeEmail")
    public void activeEmail() {
        Compte compte = compteService.getById(1L);
        emailService.activateAccount(compte);
    }*/

}
