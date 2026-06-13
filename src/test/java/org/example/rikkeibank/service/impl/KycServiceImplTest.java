package org.example.rikkeibank.service.impl;

import org.example.rikkeibank.dto.request.KycApproveRequest;
import org.example.rikkeibank.entity.KycProfile;
import org.example.rikkeibank.entity.User;
import org.example.rikkeibank.enums.KycStatus;
import org.example.rikkeibank.repository.KycProfileRepository;
import org.example.rikkeibank.repository.UserRepository;
import org.example.rikkeibank.service.impl.KycServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KycServiceImplTest {

    @Mock
    private KycProfileRepository kycProfileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MultipartFile frontImage;

    @Mock
    private MultipartFile backImage;

    @InjectMocks
    private KycServiceImpl kycService;

    private User user;
    private KycProfile kycProfile;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("customer01");

        kycProfile = new KycProfile();
        kycProfile.setId(1L);
        kycProfile.setUser(user);
        kycProfile.setStatus(KycStatus.PENDING);
    }

    @Test
    void uploadKyc_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(kycProfileRepository.save(any(KycProfile.class))).thenAnswer(i -> i.getArgument(0));

        KycProfile result = kycService.uploadKyc(1L, frontImage, backImage);

        assertNotNull(result);
        assertEquals(KycStatus.PENDING, result.getStatus());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void approveKyc_Confirm_Success() {
        KycApproveRequest request = new KycApproveRequest();
        request.setStatus(KycStatus.CONFIRM);

        when(kycProfileRepository.findById(1L)).thenReturn(Optional.of(kycProfile));
        when(kycProfileRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        KycProfile result = kycService.approveKyc(1L, request);

        assertEquals(KycStatus.CONFIRM, result.getStatus());
        assertTrue(user.getIsKyc());
    }

    @Test
    void approveKyc_Reject_Success() {
        KycApproveRequest request = new KycApproveRequest();
        request.setStatus(KycStatus.REJECT);
        request.setRejectReason("Ảnh không rõ");

        when(kycProfileRepository.findById(1L)).thenReturn(Optional.of(kycProfile));
        when(kycProfileRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        KycProfile result = kycService.approveKyc(1L, request);

        assertEquals(KycStatus.REJECT, result.getStatus());
        assertFalse(user.getIsKyc());
        assertEquals("Ảnh không rõ", result.getRejectReason());
    }
}