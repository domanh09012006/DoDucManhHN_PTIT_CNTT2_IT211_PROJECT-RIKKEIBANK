package org.example.rikkeibank.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    @NotBlank
    private String fromAccountNumber;

    @NotBlank
    private String toAccountNumber;

    @DecimalMin("1000")
    private BigDecimal amount;

    private String description;

    @NotBlank
    private String pinCode;
}