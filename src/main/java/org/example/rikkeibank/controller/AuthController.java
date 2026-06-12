package org.example.rikkeibank.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rikkeibank.dto.request.LoginRequest;
import org.example.rikkeibank.dto.request.LogoutRequest;
import org.example.rikkeibank.dto.request.RefreshTokenRequest;
import org.example.rikkeibank.dto.request.RegisterRequest;
import org.example.rikkeibank.dto.response.LoginResponse;
import org.example.rikkeibank.dto.response.RefreshTokenResponse;
import org.example.rikkeibank.dto.response.UserResponse;
import org.example.rikkeibank.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> logout(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody LogoutRequest request
    ) {
        authService.logout(authorizationHeader, request);
        return ResponseEntity.ok("Đăng xuất thành công");
    }
}
