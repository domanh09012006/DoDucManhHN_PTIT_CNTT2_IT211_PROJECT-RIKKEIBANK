package org.example.rikkeibank.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class KycUploadRequest {

    private MultipartFile frontImage;

    private MultipartFile backImage;
}