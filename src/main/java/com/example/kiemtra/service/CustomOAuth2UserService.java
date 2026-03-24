package com.example.kiemtra.service;

import com.example.kiemtra.entity.Patient;
import com.example.kiemtra.entity.Role;
import com.example.kiemtra.repository.PatientRepository;
import com.example.kiemtra.repository.RoleRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(
            PatientRepository patientRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.patientRepository = patientRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        String email = Optional.ofNullable(oauth2User.<String>getAttribute("email"))
                .orElseThrow(() -> new OAuth2AuthenticationException("Email not found in OAuth2 profile"));

        Role patientRole = roleRepository.findByName("PATIENT")
                .orElseGet(() -> roleRepository.save(Role.builder().name("PATIENT").build()));

        patientRepository.findByEmail(email).orElseGet(() -> patientRepository.save(Patient.builder()
                .username(email)
                .email(email)
                .password(passwordEncoder.encode("oauth2-login"))
                .roles(Set.of(patientRole))
                .build()));

        Set<GrantedAuthority> authorities = new HashSet<>(oauth2User.getAuthorities());
        authorities.add(new SimpleGrantedAuthority("ROLE_PATIENT"));

        String nameAttributeKey = oauth2User.getAttributes().containsKey("email") ? "email" : "sub";
        return new DefaultOAuth2User(authorities, oauth2User.getAttributes(), nameAttributeKey);
    }
}
