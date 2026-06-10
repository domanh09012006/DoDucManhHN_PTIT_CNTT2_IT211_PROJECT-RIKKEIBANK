package org.example.rikkeibank.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.rikkeibank.enums.AccountStatus;

@Getter
@Setter
@Data
public class UpdateAccountRequest {
    private String pinCode;
    private AccountStatus status;
}
