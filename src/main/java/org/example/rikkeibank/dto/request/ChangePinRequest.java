package org.example.rikkeibank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePinRequest {

    @NotBlank
    @Size(min = 6, max = 6)
    private String oldPin;

    @NotBlank
    @Size(min = 6, max = 6)
    private String newPin;
}
