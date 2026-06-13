package org.example.rikkeibank.controller;

import org.example.rikkeibank.dto.request.UserCreateRequest;
import org.example.rikkeibank.dto.response.UserResponse;
import org.example.rikkeibank.service.UserService;
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
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void createUser_Success() {
        UserCreateRequest request = new UserCreateRequest();
        UserResponse expected = new UserResponse();
        expected.setId(1L);

        when(userService.create(any())).thenReturn(expected);

        ResponseEntity<UserResponse> response = userController.create(request);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}