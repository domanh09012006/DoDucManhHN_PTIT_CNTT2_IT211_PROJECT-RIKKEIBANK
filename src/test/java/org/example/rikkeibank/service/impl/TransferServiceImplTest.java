package org.example.rikkeibank.service.impl;

import org.example.rikkeibank.dto.request.TransferRequest;
import org.example.rikkeibank.dto.response.TransferResponse;
import org.example.rikkeibank.entity.Account;
import org.example.rikkeibank.entity.Transaction;
import org.example.rikkeibank.enums.AccountStatus;
import org.example.rikkeibank.exception.InsufficientBalanceException;
import org.example.rikkeibank.repository.AccountRepository;
import org.example.rikkeibank.repository.TransactionRepository;
import org.example.rikkeibank.service.impl.TransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransferServiceImpl transferService;

    private Account sender;
    private Account receiver;
    private TransferRequest request;

    @BeforeEach
    void setUp() {
        sender = new Account();
        sender.setId(1L);
        sender.setAccountNumber("ACC001");
        sender.setBalance(BigDecimal.valueOf(100000));
        sender.setPinCode("123456");
        sender.setStatus(AccountStatus.ACTIVE);

        receiver = new Account();
        receiver.setId(2L);
        receiver.setAccountNumber("ACC002");
        receiver.setBalance(BigDecimal.ZERO);
        receiver.setStatus(AccountStatus.ACTIVE);

        request = new TransferRequest();
        request.setToAccountNumber("ACC002");
        request.setAmount(BigDecimal.valueOf(50000));
        request.setDescription("Test transfer");
        request.setPinCode("123456");

        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("customer01");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void transfer_Success() {
        when(accountRepository.findByUserUsername("customer01")).thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("ACC002")).thenReturn(Optional.of(receiver));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        TransferResponse response = transferService.transfer(request);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(BigDecimal.valueOf(50000), response.getAmount());
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void transfer_InsufficientBalance_ThrowException() {
        sender.setBalance(BigDecimal.valueOf(10000));
        request.setAmount(BigDecimal.valueOf(50000));

        when(accountRepository.findByUserUsername("customer01")).thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("ACC002")).thenReturn(Optional.of(receiver));

        assertThrows(RuntimeException.class, () -> transferService.transfer(request));
    }
}