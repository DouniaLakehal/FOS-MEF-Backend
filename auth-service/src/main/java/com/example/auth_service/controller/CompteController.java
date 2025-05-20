package com.example.auth_service.controller;

import com.example.auth_service.model.Compte;
import com.example.auth_service.service.CompteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compte")
public class CompteController {

    @Autowired
    CompteService compteService;

    @PostMapping("/checkPassword")
    public ResponseEntity<Boolean> checkPassword(@RequestBody Map<String, String> request) {
        Boolean isCorrect = compteService.checkPassword(request.get("password"), Long.valueOf(request.get("id")));
        return ResponseEntity.ok(isCorrect);
    }

    @PutMapping("/changePassword")
    public void changePassword(@RequestBody Map<String, String> request) {
        compteService.changePassword(request.get("password"), Long.valueOf(request.get("id")));
    }

    @PutMapping("/update")
    public void updateCompte(@RequestBody Compte compte) {
        compteService.updateCompte(compte);
    }

    @GetMapping("/getById")
    public Compte getById(@RequestParam Long id) {
        return compteService.getCompteById(id);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Compte>> getAllCompte() {
        List<Compte> compte = compteService.getAllCompte();
        return ResponseEntity.ok(compte);
    }

    @PostMapping("save")
    public ResponseEntity<?> saveCompte(@RequestBody Compte compte) {
        if (compteService.emailExists(compte.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }

        Compte savedCompte = compteService.saveCompteAdmin(compte);
        return ResponseEntity.ok(savedCompte);
    }
}
