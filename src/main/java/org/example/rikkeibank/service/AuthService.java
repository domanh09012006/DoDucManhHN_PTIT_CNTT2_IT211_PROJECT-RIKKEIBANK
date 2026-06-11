package org.example.rikkeibank.service;

import org.example.rikkeibank.dto.request.LoginRequest;
import org.example.rikkeibank.dto.request.LogoutRequest;
import org.example.rikkeibank.dto.request.RefreshTokenRequest;
import org.example.rikkeibank.dto.request.RegisterRequest;
import org.example.rikkeibank.dto.response.LoginResponse;
import org.example.rikkeibank.dto.response.RefreshTokenResponse;
import org.example.rikkeibank.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken( RefreshTokenRequest request);

    void logout(LogoutRequest request);
}