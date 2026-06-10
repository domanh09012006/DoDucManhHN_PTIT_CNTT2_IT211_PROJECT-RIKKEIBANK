package org.example.rikkeibank.dto.request;

import lombok.Data;
import org.example.rikkeibank.enums.KycStatus;

@Data
public class KycApproveRequest {

    private KycStatus status;

    private String rejectReason;
}