package org.example.rikkeibank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAccountRequest {

    @NotBlank
    private String accountNumber;

    @NotBlank
    private String pinCode;

    @NotNull
    private Long userId;
}
