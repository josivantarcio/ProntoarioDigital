package com.prontoariodigital.auth.service;

import com.prontoariodigital.auth.dto.LoginRequest;
import com.prontoariodigital.auth.dto.LoginResponse;
import com.prontoariodigital.auth.dto.SignupRequest;
import com.prontoariodigital.auth.dto.SignupResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request);
    void logout(String token);
    SignupResponse signup(SignupRequest request);
} 