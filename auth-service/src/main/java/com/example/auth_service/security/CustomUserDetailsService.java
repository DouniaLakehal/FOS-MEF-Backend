package com.example.auth_service.security;

import com.example.auth_service.model.Compte;
import com.example.auth_service.model.Profile;
import com.example.auth_service.model.Role;
import com.example.auth_service.repository.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    CompteRepository compteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Compte compte = compteRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        /*List<Role> roles = compte.getProfile().getRoles();
        for(Role r : roles){
            grantedAuthorities.add(new SimpleGrantedAuthority(r.getName_en()));
        }*/

        return new org.springframework.security.core.userdetails.User(compte.getEmail(), compte.getPassword(),
                grantedAuthorities);


    }
}
