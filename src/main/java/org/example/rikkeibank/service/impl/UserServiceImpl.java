package org.example.rikkeibank.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.rikkeibank.dto.request.UserCreateRequest;
import org.example.rikkeibank.dto.request.UserUpdateRequest;
import org.example.rikkeibank.dto.response.UserResponse;
import org.example.rikkeibank.entity.Account;
import org.example.rikkeibank.entity.User;
import org.example.rikkeibank.enums.AccountStatus;
import org.example.rikkeibank.enums.Role;
import org.example.rikkeibank.repository.AccountRepository;
import org.example.rikkeibank.repository.UserRepository;
import org.example.rikkeibank.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(UserCreateRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username da ton tai");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email da ton tai");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone da ton tai");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(Role.CUSTOMER)
                .isKyc(false)
                .enabled(true)
                .build();

        user = userRepository.save(user);

        Account account = Account.builder()
                .accountNumber("ACC" + System.currentTimeMillis())
                .balance(BigDecimal.ZERO)
                .pinCode("000000")
                .status(AccountStatus.ACTIVE)
                .user(user)
                .build();

        accountRepository.save(account);

        return mapToResponse(user);
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User khong tim thay"));

        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email da ton tai");
        }
        if (!user.getPhone().equals(request.getPhone()) && userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone da ton tai");
        }

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
        return userRepository.findAllActiveUsers(PageRequest.of(page, size));
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User khong tim thay"));

        user.setEnabled(false);
        userRepository.save(user);
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
