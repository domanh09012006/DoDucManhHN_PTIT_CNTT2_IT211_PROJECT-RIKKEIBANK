package org.example.rikkeibank.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.rikkeibank.dto.request.KycApproveRequest;
import org.example.rikkeibank.entity.KycProfile;
import org.example.rikkeibank.entity.User;
import org.example.rikkeibank.enums.KycStatus;
import org.example.rikkeibank.repository.KycProfileRepository;
import org.example.rikkeibank.repository.UserRepository;
import org.example.rikkeibank.service.KycService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KycServiceImpl implements KycService {

    private final KycProfileRepository kycProfileRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public KycProfile uploadKyc(Long userId, MultipartFile frontImage, MultipartFile backImage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        String frontUrl = "https://example.com/front-" + System.currentTimeMillis() + "-" + frontImage.getOriginalFilename();
        String backUrl = "https://example.com/back-" + System.currentTimeMillis() + "-" + backImage.getOriginalFilename();

        KycProfile kyc = KycProfile.builder()
                .frontImageUrl(frontUrl)
                .backImageUrl(backUrl)
                .status(KycStatus.PENDING)
                .user(user)
                .build();

        kyc = kycProfileRepository.save(kyc);

        user.setIsKyc(false);
        userRepository.save(user);

        return kyc;
    }

    @Override
    @Transactional
    public KycProfile approveKyc(Long kycId, KycApproveRequest request) {
        KycProfile kyc = kycProfileRepository.findById(kycId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ KYC với id: " + kycId));

        User user = kyc.getUser();

        kyc.setStatus(request.getStatus());
        kyc.setRejectReason(request.getRejectReason());
        kyc.setReviewedAt(LocalDateTime.now());

        if (request.getStatus() == KycStatus.CONFIRM) {
            user.setIsKyc(true);
        } else if (request.getStatus() == KycStatus.REJECT) {
            user.setIsKyc(false);
        }

        kycProfileRepository.save(kyc);
        userRepository.save(user);

        return kyc;
    }
}