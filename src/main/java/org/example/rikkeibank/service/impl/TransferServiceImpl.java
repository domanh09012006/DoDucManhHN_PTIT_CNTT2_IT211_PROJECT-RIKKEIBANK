package org.example.rikkeibank.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.rikkeibank.dto.request.TransferRequest;
import org.example.rikkeibank.dto.response.TransferResponse;
import org.example.rikkeibank.entity.Account;
import org.example.rikkeibank.entity.Transaction;
import org.example.rikkeibank.enums.AccountStatus;
import org.example.rikkeibank.enums.TransactionType;
import org.example.rikkeibank.repository.AccountRepository;
import org.example.rikkeibank.repository.TransactionRepository;
import org.example.rikkeibank.service.TransferService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TransferResponse transfer(TransferRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Chưa đăng nhập");
        }

        String username = authentication.getName();

        Account sender = accountRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản nguồn"));

        if (!sender.getPinCode().equals(request.getPinCode())) {
            throw new RuntimeException("Mã PIN không đúng");
        }

        Account receiver = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("Tài khoản nhận không tồn tại"));

        if (sender.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Tài khoản gửi đang bị khóa");
        }

        if (receiver.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Tài khoản nhận đang bị khóa");
        }

        if (request.getAmount() == null || request.getAmount().signum() <= 0) {
            throw new RuntimeException("Số tiền phải lớn hơn 0");
        }

        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Số dư không đủ");
        }

        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException("Không thể chuyển cho chính mình");
        }

        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .transactionType(TransactionType.TRANSFER)
                .description(request.getDescription())
                .fromAccount(sender)
                .toAccount(receiver)
                .build();

        transactionRepository.save(transaction);

        log.info("Transfer success: {} -> {}, amount={}", sender.getAccountNumber(), receiver.getAccountNumber(), request.getAmount());

        return TransferResponse.builder()
                .fromAccount(sender.getAccountNumber())
                .toAccount(receiver.getAccountNumber())
                .amount(request.getAmount())
                .description(request.getDescription())
                .status("SUCCESS")
                .build();
    }
}
