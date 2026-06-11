package org.example.rikkeibank.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.rikkeibank.dto.request.KycApproveRequest;
import org.example.rikkeibank.entity.KycProfile;
import org.example.rikkeibank.entity.User;
import org.example.rikkeibank.enums.KycStatus;
import org.example.rikkeibank.repository.KycProfileRepository;
import org.example.rikkeibank.repository.UserRepository;
import org.example.rikkeibank.service.CloudinaryService;
import org.example.rikkeibank.service.KycService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class KycServiceImpl implements KycService {

    private final KycProfileRepository kycProfileRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public KycProfile uploadKyc(Long userId, MultipartFile frontImage, MultipartFile backImage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        String frontUrl = cloudinaryService.upload(frontImage, "rikkebank/kyc/front");
        String backUrl = cloudinaryService.upload(backImage, "rikkebank/kyc/back");

        KycProfile kyc = KycProfile.builder()
                .frontImageUrl(frontUrl)
                .backImageUrl(backUrl)
                .status(KycStatus.PENDING)
                .user(user)
                .build();

        kyc = kycProfileRepository.save(kyc);

        user.setIsKyc(false);
        userRepository.save(user);

        log.info("User {} uploaded KYC profile {}", userId, kyc.getId());
        return kyc;
    }

    @Override
    @Transactional
    public KycProfile approveKyc(Long kycId, KycApproveRequest request) {
        KycProfile kyc = kycProfileRepository.findById(kycId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ KYC với id: " + kycId));

        if (request.getStatus() == null) {
            throw new RuntimeException("Trạng thái KYC không được để trống");
        }

        User user = kyc.getUser();

        kyc.setStatus(request.getStatus());
        kyc.setReviewedAt(LocalDateTime.now());

        if (request.getStatus() == KycStatus.CONFIRM) {
            kyc.setRejectReason(null);
            user.setIsKyc(true);
        } else if (request.getStatus() == KycStatus.REJECT) {
            if (request.getRejectReason() == null || request.getRejectReason().isBlank()) {
                throw new RuntimeException("Vui lòng nhập lý do từ chối");
            }
            kyc.setRejectReason(request.getRejectReason());
            user.setIsKyc(false);
        }

        kycProfileRepository.save(kyc);
        userRepository.save(user);

        log.info("KYC {} reviewed with status {}", kycId, request.getStatus());
        return kyc;
    }
}
