package org.example.rikkeibank.controller;

import org.example.rikkeibank.dto.request.LoginRequest;
import org.example.rikkeibank.dto.request.RegisterRequest;
import org.example.rikkeibank.dto.response.LoginResponse;
import org.example.rikkeibank.dto.response.UserResponse;
import org.example.rikkeibank.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void register_Success() {
        RegisterRequest request = new RegisterRequest();
        UserResponse expected = new UserResponse();
        expected.setUsername("testuser");

        when(authService.register(any())).thenReturn(expected);

        ResponseEntity<UserResponse> response = authController.register(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    void login_Success() {
        LoginRequest request = new LoginRequest();
        LoginResponse expected = new LoginResponse();
        expected.setAccessToken("jwt.token.here");

        when(authService.login(any())).thenReturn(expected);

        ResponseEntity<LoginResponse> response = authController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().getAccessToken());
    }
}