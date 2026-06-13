package org.example.rikkeibank.controller;

import org.example.rikkeibank.dto.request.ChangePinRequest;
import org.example.rikkeibank.dto.request.CreateAccountRequest;
import org.example.rikkeibank.dto.request.UpdateAccountRequest;
import org.example.rikkeibank.dto.response.AccountResponse;
import org.example.rikkeibank.dto.response.BalanceResponse;
import org.example.rikkeibank.enums.AccountStatus;
import org.example.rikkeibank.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Test
    void findAll_shouldReturnPage() {
        AccountResponse acc = AccountResponse.builder()
                .id(10L)
                .accountNumber("ACC001")
                .balance(new BigDecimal("5000"))
                .status("ACTIVE")
                .userId(1L)
                .build();

        Page<AccountResponse> page = new PageImpl<>(List.of(acc));

        when(accountService.findAll(0, 5)).thenReturn(page);

        Page<AccountResponse> result = accountController.findAll(0, 5);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("ACC001", result.getContent().get(0).getAccountNumber());
    }

    @Test
    void getBalance_shouldReturnBalance() {
        BalanceResponse expected = BalanceResponse.builder()
                .accountNumber("ACC001")
                .balance(new BigDecimal("5000"))
                .build();

        when(accountService.getBalance("ACC001")).thenReturn(expected);

        BalanceResponse response = accountController.getBalance("ACC001");

        assertEquals("ACC001", response.getAccountNumber());
        assertEquals(new BigDecimal("5000"), response.getBalance());
    }

    @Test
    void changePin_shouldReturnUpdatedAccount() {
        ChangePinRequest request = new ChangePinRequest();
        request.setOldPin("000000");
        request.setNewPin("123456");

        AccountResponse expected = AccountResponse.builder()
                .id(10L)
                .accountNumber("ACC001")
                .status("ACTIVE")
                .build();

        when(accountService.changePin("ACC001", request)).thenReturn(expected);

        AccountResponse response = accountController.changePin("ACC001", request).getBody();

        assertNotNull(response);
        assertEquals("ACC001", response.getAccountNumber());
    }
}