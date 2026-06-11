package org.example.rikkeibank.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.rikkeibank.enums.KycStatus;

@Data
public class KycApproveRequest {

    @NotNull
    private KycStatus status;

    private String rejectReason;
}
