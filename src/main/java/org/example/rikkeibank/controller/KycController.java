package org.example.rikkeibank.controller;

import lombok.RequiredArgsConstructor;
import org.example.rikkeibank.dto.request.KycApproveRequest;
import org.example.rikkeibank.dto.request.KycUploadRequest;
import org.example.rikkeibank.entity.KycProfile;
import org.example.rikkeibank.service.KycService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kyc")
@RequiredArgsConstructor
public class KycController {

    private final KycService kycService;

    @PostMapping("/upload")
    public ResponseEntity<KycProfile> uploadKyc(
            @RequestParam Long userId,
            @ModelAttribute KycUploadRequest request) {

        KycProfile kycProfile = kycService.uploadKyc(userId,
                request.getFrontImage(),
                request.getBackImage());
        return ResponseEntity.ok(kycProfile);
    }

    @PutMapping("/approve/{kycId}")
    public ResponseEntity<KycProfile> approveKyc(
            @PathVariable Long kycId,
            @RequestBody KycApproveRequest request) {

        KycProfile kycProfile = kycService.approveKyc(kycId, request);
        return ResponseEntity.ok(kycProfile);
    }
}