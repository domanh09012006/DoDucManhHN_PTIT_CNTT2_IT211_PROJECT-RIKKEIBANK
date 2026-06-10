package org.example.rikkeibank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.rikkeibank.enums.KycStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "kyc_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String frontImageUrl;

    private String backImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KycStatus status;

    private String rejectReason;

    private LocalDateTime submittedAt;

    private LocalDateTime reviewedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        submittedAt = LocalDateTime.now();
    }
}