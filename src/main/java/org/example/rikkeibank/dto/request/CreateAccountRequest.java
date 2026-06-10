package org.example.rikkeibank.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CreateAccountRequest {
    private String accountNumber;
    private String pinCode;
    private Long userId;
}
