package org.example.rikkeibank.service.impl;

import org.example.rikkeibank.dto.request.LoginRequest;
import org.example.rikkeibank.dto.request.RegisterRequest;
import org.example.rikkeibank.dto.response.LoginResponse;
import org.example.rikkeibank.dto.response.UserResponse;
import org.example.rikkeibank.entity.User;
import org.example.rikkeibank.repository.UserRepository;
import org.example.rikkeibank.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks private AuthServiceImpl authService;

    @Test
    void register_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("123456");
        request.setFullName("Test User");
        request.setEmail("test@gmail.com");
        request.setPhone("0987654321");

        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UserResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void login_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("customer01");
        request.setPassword("123456");

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
    }
}