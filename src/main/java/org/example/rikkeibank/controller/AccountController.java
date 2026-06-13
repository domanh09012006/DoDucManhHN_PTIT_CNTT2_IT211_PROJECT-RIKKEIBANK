package org.example.rikkeibank.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rikkeibank.dto.request.ChangePinRequest;
import org.example.rikkeibank.dto.request.CreateAccountRequest;
import org.example.rikkeibank.dto.request.UpdateAccountRequest;
import org.example.rikkeibank.dto.response.AccountResponse;
import org.example.rikkeibank.dto.response.BalanceResponse;
import org.example.rikkeibank.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public Page<AccountResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return accountService.findAll(page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public AccountResponse findById(@PathVariable Long id) {
        return accountService.findById(id);
    }

    @GetMapping("/balance/{accountNumber}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public BalanceResponse getBalance(@PathVariable String accountNumber) {
        return accountService.getBalance(accountNumber);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<AccountResponse> create(
            @Valid @RequestBody CreateAccountRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<AccountResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAccountRequest request
    ) {
        return ResponseEntity.ok(accountService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/change-pin/{accountNumber}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AccountResponse> changePin(
            @PathVariable String accountNumber,
            @Valid @RequestBody ChangePinRequest request
    ) {
        return ResponseEntity.ok(accountService.changePin(accountNumber, request));
    }
}
