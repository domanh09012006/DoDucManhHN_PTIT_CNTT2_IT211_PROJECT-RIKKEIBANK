package org.example.rikkeibank.controller;

import lombok.RequiredArgsConstructor;
import org.example.rikkeibank.dto.request.CreateAccountRequest;
import org.example.rikkeibank.dto.request.UpdateAccountRequest;
import org.example.rikkeibank.dto.response.AccountResponse;
import org.example.rikkeibank.dto.response.BalanceResponse;
import org.example.rikkeibank.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public Page<AccountResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return accountService.findAll(page, size);
    }

    @GetMapping("/{id}")
    public AccountResponse findById(@PathVariable Long id) {
        return accountService.findById(id);
    }

    @GetMapping("/balance/{accountNumber}")
    public BalanceResponse getBalance(
            @PathVariable String accountNumber
    ) {
        return accountService.getBalance(accountNumber);
    }

    @PostMapping
    public AccountResponse create(
            @RequestBody CreateAccountRequest request
    ) {
        return accountService.create(request);
    }

    @PutMapping("/{id}")
    public AccountResponse update(
            @PathVariable Long id,
            @RequestBody UpdateAccountRequest request
    ) {
        return accountService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id
    ) {
        accountService.delete(id);
        return "Xóa tài khoản thành công";
    }
}