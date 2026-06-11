package org.example.rikkeibank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.rikkeibank.enums.AccountStatus;

@Data
public class UpdateAccountRequest {

    @NotBlank
    private String pinCode;

    @NotNull
    private AccountStatus status;
}
