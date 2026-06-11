package org.example.rikkeibank.service;

import org.example.rikkeibank.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    Page<TransactionResponse> getTransactionHistory(String accountNumber, Pageable pageable);
}