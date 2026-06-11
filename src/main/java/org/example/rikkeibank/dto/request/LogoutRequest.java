package org.example.rikkeibank.dto.request;

import lombok.Data;

@Data
public class LogoutRequest {

    private String refreshToken;
}