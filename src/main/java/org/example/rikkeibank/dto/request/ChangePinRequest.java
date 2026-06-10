package org.example.rikkeibank.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePinRequest {

    @NotBlank
    private String oldPin;

    @NotBlank
    private String newPin;
}