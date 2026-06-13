package org.example.rikkeibank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ForgotPasswordRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6, max = 100)
    private String newPassword;
}
