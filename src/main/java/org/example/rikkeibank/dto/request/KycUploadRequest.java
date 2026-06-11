package org.example.rikkeibank.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class KycUploadRequest {

    @NotNull
    private MultipartFile frontImage;

    @NotNull
    private MultipartFile backImage;
}
