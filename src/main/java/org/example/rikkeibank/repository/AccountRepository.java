package org.example.rikkeibank.repository;

import org.example.rikkeibank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByUserUsername(String username);

    boolean existsByAccountNumber(String accountNumber);
}