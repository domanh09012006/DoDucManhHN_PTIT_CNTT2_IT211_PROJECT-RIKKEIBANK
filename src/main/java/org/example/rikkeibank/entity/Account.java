package org.example.rikkeibank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.rikkeibank.enums.AccountStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false)
    private String pinCode;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "fromAccount")
    private List<Transaction> sentTransactions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "toAccount")
    private List<Transaction> receivedTransactions = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}