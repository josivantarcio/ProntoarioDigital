package com.prontoariodigital.auth.service;

import com.prontoariodigital.auth.dto.LoginRequest;
import com.prontoariodigital.auth.dto.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request);
    void logout(String token);
} 