package org.example.rikkeibank.controller;

import lombok.RequiredArgsConstructor;
import org.example.rikkeibank.dto.request.RegisterRequest;
import org.example.rikkeibank.dto.response.UserResponse;
import org.example.rikkeibank.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }
}