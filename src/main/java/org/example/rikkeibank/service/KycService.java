package org.example.rikkeibank.service;

import org.example.rikkeibank.entity.KycProfile;
import org.springframework.web.multipart.MultipartFile;

public interface KycService {
    KycProfile uploadKyc(Long userId, MultipartFile frontImage, MultipartFile backImage);
}