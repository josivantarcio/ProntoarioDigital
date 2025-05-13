package com.prontoariodigital.auth.controller;

import com.prontoariodigital.auth.dto.LoginRequest;
import com.prontoariodigital.auth.dto.LoginResponse;
import com.prontoariodigital.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs de autenticação")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Realiza login no sistema", description = "Retorna um token JWT para autenticação")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "Realiza logout do sistema", description = "Invalida o token JWT atual")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authenticationService.logout(token.replace("Bearer ", ""));
        return ResponseEntity.ok().build();
    }
} 