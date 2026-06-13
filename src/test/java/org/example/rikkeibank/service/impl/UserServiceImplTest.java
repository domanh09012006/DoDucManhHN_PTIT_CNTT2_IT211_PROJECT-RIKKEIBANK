package org.example.rikkeibank.service.impl;

import org.example.rikkeibank.dto.request.UserCreateRequest;
import org.example.rikkeibank.dto.request.UserUpdateRequest;
import org.example.rikkeibank.dto.response.UserResponse;
import org.example.rikkeibank.entity.User;
import org.example.rikkeibank.repository.UserRepository;
import org.example.rikkeibank.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = new UserCreateRequest();
        createRequest.setUsername("testuser");
        createRequest.setPassword("123456");
        createRequest.setFullName("Test User");
        createRequest.setEmail("test@gmail.com");
        createRequest.setPhone("0987654321");
    }

    @Test
    void createUser_Success() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserResponse response = userService.create(createRequest);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("Test User", response.getFullName());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void createUser_UsernameExists_ThrowException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.create(createRequest));
    }

    @Test
    void updateUser_Success() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("testuser");

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setFullName("Updated Name");
        updateRequest.setEmail("new@gmail.com");
        updateRequest.setPhone("0912345678");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UserResponse response = userService.update(1L, updateRequest);

        assertEquals("Updated Name", response.getFullName());
        assertEquals("new@gmail.com", response.getEmail());
    }
}