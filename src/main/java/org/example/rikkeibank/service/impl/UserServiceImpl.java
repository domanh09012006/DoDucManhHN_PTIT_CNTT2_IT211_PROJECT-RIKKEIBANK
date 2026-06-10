package org.example.rikkeibank.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.rikkeibank.dto.request.UserCreateRequest;
import org.example.rikkeibank.dto.request.UserUpdateRequest;
import org.example.rikkeibank.dto.response.UserResponse;
import org.example.rikkeibank.entity.User;
import org.example.rikkeibank.enums.Role;
import org.example.rikkeibank.repository.UserRepository;
import org.example.rikkeibank.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse create(UserCreateRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username da ton tai");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(Role.CUSTOMER)
                .build();

        user = userRepository.save(user);

        return mapToResponse(user);
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User khong tim thay"));

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        user = userRepository.save(user);

        return mapToResponse(user);
    }

    @Override
    public UserResponse findById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User khong tim thay"));

        return mapToResponse(user);
    }

    @Override
    public Page<UserResponse> findAll(int page, int size) {

        return userRepository.findAll(PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    @Override
    public void delete(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User khong tim thay"));

        userRepository.delete(user);
    }

    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .isKyc(user.getIsKyc())
                .enabled(user.getEnabled())
                .build();
    }
}