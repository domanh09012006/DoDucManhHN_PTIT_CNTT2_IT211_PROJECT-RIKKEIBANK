package org.example.rikkeibank.service;

import org.example.rikkeibank.dto.request.UserCreateRequest;
import org.example.rikkeibank.dto.request.UserUpdateRequest;
import org.example.rikkeibank.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {

    UserResponse create(UserCreateRequest request);

    UserResponse update(Long id, UserUpdateRequest request);

    UserResponse findById(Long id);

    Page<UserResponse> findAll(int page, int size);

    void delete(Long id);
}