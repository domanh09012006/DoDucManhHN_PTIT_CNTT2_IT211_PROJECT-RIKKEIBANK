package org.example.rikkeibank.controller;

import org.example.rikkeibank.dto.response.TransactionResponse;
import org.example.rikkeibank.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void getHistory_Success() {
        TransactionResponse tx = TransactionResponse.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(50000))
                .transactionType("DEBIT")
                .build();

        Page<TransactionResponse> page = new PageImpl<>(List.of(tx));

        when(transactionService.getTransactionHistory(any(), any())).thenReturn(page);

        ResponseEntity<Page<TransactionResponse>> response =
                transactionController.getHistory("ACC123", 0, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }
}