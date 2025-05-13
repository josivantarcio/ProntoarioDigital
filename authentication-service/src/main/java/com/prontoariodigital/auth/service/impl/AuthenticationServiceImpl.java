package com.prontoariodigital.auth.service.impl;

import com.prontoariodigital.auth.domain.User;
import com.prontoariodigital.auth.dto.LoginRequest;
import com.prontoariodigital.auth.dto.LoginResponse;
import com.prontoariodigital.auth.dto.SignupRequest;
import com.prontoariodigital.auth.dto.SignupResponse;
import com.prontoariodigital.auth.repository.UserRepository;
import com.prontoariodigital.auth.service.AuthenticationService;
import com.prontoariodigital.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtService.generateToken(user);

        return LoginResponse.builder()
            .token(token)
            .type("Bearer")
            .email(user.getEmail())
            .name(user.getName())
            .roles(user.getRoles())
            .build();
    }

    @Override
    public void logout(String token) {
        // Implementar lógica de logout (ex: adicionar token à blacklist)
    }

    @Override
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Set<String> roles = new HashSet<>();
        roles.add("USER"); // Papel padrão para novos usuários

        User user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .roles(roles)
            .enabled(true)
            .build();

        user = userRepository.save(user);

        return SignupResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .build();
    }
} 