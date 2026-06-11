package org.example.rikkeibank.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private String transactionType;
    private String description;
    private String fromAccountNumber;
    private String toAccountNumber;
    private LocalDateTime createdAt;
}