package org.example.rikkeibank.repository;

import org.example.rikkeibank.entity.KycProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KycProfileRepository
        extends JpaRepository<KycProfile, Long> {
}