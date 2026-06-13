package org.example.rikkeibank.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.rikkeibank.dto.request.ChangePinRequest;
import org.example.rikkeibank.dto.request.CreateAccountRequest;
import org.example.rikkeibank.dto.request.UpdateAccountRequest;
import org.example.rikkeibank.dto.response.AccountResponse;
import org.example.rikkeibank.dto.response.BalanceResponse;
import org.example.rikkeibank.entity.Account;
import org.example.rikkeibank.entity.User;
import org.example.rikkeibank.enums.AccountStatus;
import org.example.rikkeibank.exception.ResourceNotFoundException;
import org.example.rikkeibank.repository.AccountRepository;
import org.example.rikkeibank.repository.UserRepository;
import org.example.rikkeibank.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public Page<AccountResponse> findAll(int page, int size) {
        return accountRepository.findAll(PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    @Override
    public AccountResponse findById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản"));
        return mapToResponse(account);
    }

    @Override
    public BalanceResponse getBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản"));

        return BalanceResponse.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }

    @Override
    public AccountResponse create(CreateAccountRequest request) {
        if (accountRepository.existsByAccountNumber(request.getAccountNumber())) {
            throw new RuntimeException("Số tài khoản đã tồn tại");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new RuntimeException("Không thể tạo tài khoản cho user đã bị khóa");
        }

        Account account = Account.builder()
                .accountNumber(request.getAccountNumber())
                .pinCode(request.getPinCode())
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .user(user)
                .build();

        account = accountRepository.save(account);
        return mapToResponse(account);
    }

    @Override
    public AccountResponse update(Long id, UpdateAccountRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản"));

        account.setPinCode(request.getPinCode());
        account.setStatus(request.getStatus());

        account = accountRepository.save(account);
        return mapToResponse(account);
    }

    @Override
    public void delete(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản"));

        account.setStatus(AccountStatus.BLOCKED);
        accountRepository.save(account);
    }

    private AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .status(account.getStatus().name())
                .userId(account.getUser() != null ? account.getUser().getId() : null)
                .build();
    }

    @Override
    @Transactional
    public AccountResponse changePin(String accountNumber, ChangePinRequest request) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản"));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Tài khoản đang bị khóa");
        }

        if (!account.getPinCode().equals(request.getOldPin())) {
            throw new RuntimeException("Mã PIN cũ không đúng");
        }

        account.setPinCode(request.getNewPin());
        account = accountRepository.save(account);

        return mapToResponse(account);
    }
}
