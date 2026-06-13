package org.example.rikkeibank.service.impl;

import org.example.rikkeibank.dto.request.ChangePinRequest;
import org.example.rikkeibank.dto.request.CreateAccountRequest;
import org.example.rikkeibank.dto.response.AccountResponse;
import org.example.rikkeibank.dto.response.BalanceResponse;
import org.example.rikkeibank.entity.Account;
import org.example.rikkeibank.entity.User;
import org.example.rikkeibank.enums.AccountStatus;
import org.example.rikkeibank.repository.AccountRepository;
import org.example.rikkeibank.repository.UserRepository;
import org.example.rikkeibank.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User user;
    private Account account;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("customer01");

        account = new Account();
        account.setId(1L);
        account.setAccountNumber("ACC123456");
        account.setBalance(BigDecimal.valueOf(100000));
        account.setPinCode("000000");
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);
    }

    @Test
    void getBalance_Success() {
        when(accountRepository.findByAccountNumber("ACC123456"))
                .thenReturn(Optional.of(account));

        BalanceResponse response = accountService.getBalance("ACC123456");

        assertNotNull(response);
        assertEquals("ACC123456", response.getAccountNumber());
        assertEquals(BigDecimal.valueOf(100000), response.getBalance());
    }

    @Test
    void changePin_Success() {
        ChangePinRequest request = new ChangePinRequest();
        request.setOldPin("000000");
        request.setNewPin("123456");

        when(accountRepository.findByAccountNumber("ACC123456"))
                .thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        AccountResponse response = accountService.changePin("ACC123456", request);

        assertNotNull(response);
        assertEquals("ACC123456", response.getAccountNumber());
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void changePin_WrongOldPin_ThrowException() {
        ChangePinRequest request = new ChangePinRequest();
        request.setOldPin("999999");
        request.setNewPin("123456");

        when(accountRepository.findByAccountNumber("ACC123456"))
                .thenReturn(Optional.of(account));

        assertThrows(RuntimeException.class,
                () -> accountService.changePin("ACC123456", request));
    }
}