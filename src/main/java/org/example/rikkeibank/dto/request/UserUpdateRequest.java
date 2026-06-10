package org.example.rikkeibank.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {

    private String fullName;
    private String email;
    private String phone;
}