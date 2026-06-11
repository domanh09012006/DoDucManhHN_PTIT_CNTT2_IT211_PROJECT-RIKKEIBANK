package org.example.rikkeibank.service;

import org.example.rikkeibank.dto.request.ChangePinRequest;
import org.example.rikkeibank.dto.request.CreateAccountRequest;
import org.example.rikkeibank.dto.request.UpdateAccountRequest;
import org.example.rikkeibank.dto.response.AccountResponse;
import org.example.rikkeibank.dto.response.BalanceResponse;
import org.springframework.data.domain.Page;

public interface AccountService {

    Page<AccountResponse> findAll(int page, int size);

    AccountResponse findById(Long id);

    BalanceResponse getBalance(String accountNumber);

    AccountResponse create(CreateAccountRequest request);

    AccountResponse update(Long id, UpdateAccountRequest request);

    void delete(Long id);
    AccountResponse changePin(String accountNumber, ChangePinRequest request);
}