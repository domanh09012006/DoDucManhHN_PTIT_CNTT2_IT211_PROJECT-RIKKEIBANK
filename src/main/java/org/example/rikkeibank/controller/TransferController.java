package org.example.rikkeibank.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rikkeibank.dto.request.TransferRequest;
import org.example.rikkeibank.dto.response.TransferResponse;
import org.example.rikkeibank.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<TransferResponse> transfer(
            @Valid @RequestBody TransferRequest request
    ) {
        return ResponseEntity.ok(transferService.transfer(request));
    }
}
