package org.example.rikkeibank.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.rikkeibank.dto.request.RegisterRequest;
import org.example.rikkeibank.dto.response.UserResponse;
import org.example.rikkeibank.entity.Account;
import org.example.rikkeibank.entity.User;
import org.example.rikkeibank.enums.AccountStatus;
import org.example.rikkeibank.enums.Role;
import org.example.rikkeibank.repository.AccountRepository;
import org.example.rikkeibank.repository.UserRepository;
import org.example.rikkeibank.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
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
                .balance(java.math.BigDecimal.ZERO)
                .pinCode("000000")
                .status(AccountStatus.ACTIVE)
                .user(user)
                .build();

        accountRepository.save(account);

        return mapToResponse(user);
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