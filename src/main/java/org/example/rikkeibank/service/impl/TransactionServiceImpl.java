package org.example.rikkeibank.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.rikkeibank.dto.response.TransactionResponse;
import org.example.rikkeibank.entity.Transaction;
import org.example.rikkeibank.repository.TransactionRepository;
import org.example.rikkeibank.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Page<TransactionResponse> getTransactionHistory(String accountNumber, Pageable pageable) {
        Page<Transaction> transactions = transactionRepository
                .findTransactionHistory(accountNumber, pageable);

        return transactions.map(t -> mapToResponse(t, accountNumber));
    }

    private TransactionResponse mapToResponse(Transaction t, String currentAccountNumber) {
        String type = "CREDIT";
        if (t.getFromAccount() != null &&
                currentAccountNumber.equals(t.getFromAccount().getAccountNumber())) {
            type = "DEBIT";
        }

        return TransactionResponse.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .transactionType(type)
                .description(t.getDescription())
                .fromAccountNumber(t.getFromAccount() != null ? t.getFromAccount().getAccountNumber() : null)
                .toAccountNumber(t.getToAccount() != null ? t.getToAccount().getAccountNumber() : null)
                .createdAt(t.getCreatedAt())
                .build();
    }
}