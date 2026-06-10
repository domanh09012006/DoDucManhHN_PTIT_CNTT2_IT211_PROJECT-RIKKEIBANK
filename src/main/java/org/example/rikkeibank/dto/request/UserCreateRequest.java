package org.example.rikkeibank.dto.request;

import lombok.Data;

@Data
public class UserCreateRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
}