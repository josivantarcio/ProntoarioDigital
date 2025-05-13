package com.prontoariodigital.auth.service;

import com.prontoariodigital.auth.domain.User;

public interface JwtService {
    String generateToken(User user);
    String extractUsername(String token);
    boolean isTokenValid(String token);
} 