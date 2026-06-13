package org.example.rikkeibank.dto.response;

import lombok.*;
import org.example.rikkeibank.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private Boolean isKyc;
    private Boolean enabled;

    public UserResponse(
            Long id,
            String username,
            String fullName,
            String email,
            String phone,
            Role role,
            Boolean isKyc,
            Boolean enabled
    ) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role != null ? role.name() : null;
        this.isKyc = isKyc;
        this.enabled = enabled;
    }
}
