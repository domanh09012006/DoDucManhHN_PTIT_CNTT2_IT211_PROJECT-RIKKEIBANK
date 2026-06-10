package org.example.rikkeibank.service;

import org.example.rikkeibank.dto.request.RegisterRequest;
import org.example.rikkeibank.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);
}