package com.prontoariodigital.auth.service.impl;

import com.prontoariodigital.auth.domain.User;
import com.prontoariodigital.auth.dto.LoginRequest;
import com.prontoariodigital.auth.dto.LoginResponse;
import com.prontoariodigital.auth.repository.UserRepository;
import com.prontoariodigital.auth.service.AuthenticationService;
import com.prontoariodigital.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

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
} 