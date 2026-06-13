package org.example.rikkeibank.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.rikkeibank.dto.request.*;
import org.example.rikkeibank.dto.response.LoginResponse;
import org.example.rikkeibank.dto.response.RefreshTokenResponse;
import org.example.rikkeibank.dto.response.UserResponse;
import org.example.rikkeibank.entity.Account;
import org.example.rikkeibank.entity.RefreshToken;
import org.example.rikkeibank.entity.RevokedToken;
import org.example.rikkeibank.entity.User;
import org.example.rikkeibank.enums.AccountStatus;
import org.example.rikkeibank.enums.Role;
import org.example.rikkeibank.exception.RefreshTokenExpiredException;
import org.example.rikkeibank.exception.RefreshTokenRevokedException;
import org.example.rikkeibank.exception.ResourceNotFoundException;
import org.example.rikkeibank.repository.AccountRepository;
import org.example.rikkeibank.repository.RefreshTokenRepository;
import org.example.rikkeibank.repository.RevokedTokenRepository;
import org.example.rikkeibank.repository.UserRepository;
import org.example.rikkeibank.security.JwtService;
import org.example.rikkeibank.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RevokedTokenRepository revokedTokenRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone đã tồn tại");
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

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String accessToken = jwtService.generateAccessToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(1))
                .revoked(false)
                .build();

        refreshTokenRepository.save(token);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken oldToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token không tồn tại"));

        if (oldToken.getRevoked()) {
            throw new RefreshTokenRevokedException("Refresh token đã bị thu hồi");
        }

        if (oldToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenExpiredException("Refresh token đã hết hạn");
        }

        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        String username = jwtService.extractUsername(request.getRefreshToken());
        String newAccessToken = jwtService.generateAccessToken(username);
        String newRefreshToken = jwtService.generateRefreshToken(username);

        RefreshToken token = RefreshToken.builder()
                .token(newRefreshToken)
                .user(oldToken.getUser())
                .expiryDate(LocalDateTime.now().plusDays(1))
                .revoked(false)
                .build();

        refreshTokenRepository.save(token);

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    @Transactional
    public void logout(String accessToken, LogoutRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token không tồn tại"));

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        String token = extractBearerToken(accessToken);

        if (!revokedTokenRepository.existsByToken(token)) {
            revokedTokenRepository.save(
                    RevokedToken.builder()
                            .token(token)
                            .build()
            );
        }
    }

    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Thiếu hoặc sai Authorization header");
        }
        return authorizationHeader.substring(7);
    }
    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

}
