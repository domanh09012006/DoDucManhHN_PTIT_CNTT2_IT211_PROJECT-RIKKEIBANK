package org.example.rikkeibank.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransferResponse {

    private String fromAccount;

    private String toAccount;

    private BigDecimal amount;

    private String description;

    private String status;
}