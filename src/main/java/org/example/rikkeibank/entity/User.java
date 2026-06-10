package org.example.rikkeibank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.rikkeibank.enums.Role;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder.Default
    private Boolean isKyc = false;

    @Builder.Default
    private Boolean enabled = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Account account;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KycProfile> kycProfiles = new ArrayList<>();

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}