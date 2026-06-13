package org.example.rikkeibank.controller;

import org.example.rikkeibank.dto.request.KycApproveRequest;
import org.example.rikkeibank.dto.request.KycUploadRequest;
import org.example.rikkeibank.entity.KycProfile;
import org.example.rikkeibank.enums.KycStatus;
import org.example.rikkeibank.service.KycService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KycControllerTest {

    @Mock
    private KycService kycService;

    @Mock
    private MultipartFile frontImage;

    @Mock
    private MultipartFile backImage;

    @InjectMocks
    private KycController kycController;

    @Test
    void uploadKyc_Success() {
        KycProfile expected = new KycProfile();
        expected.setId(1L);
        expected.setStatus(KycStatus.PENDING);

        when(kycService.uploadKyc(any(), any(), any())).thenReturn(expected);

        ResponseEntity<KycProfile> response = kycController.uploadKyc(1L, new KycUploadRequest());

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void approveKyc_Success() {
        KycApproveRequest request = new KycApproveRequest();
        request.setStatus(KycStatus.CONFIRM);

        KycProfile expected = new KycProfile();
        expected.setId(1L);
        expected.setStatus(KycStatus.CONFIRM);

        when(kycService.approveKyc(any(), any())).thenReturn(expected);

        ResponseEntity<KycProfile> response = kycController.approveKyc(1L, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(KycStatus.CONFIRM, response.getBody().getStatus());
    }
}