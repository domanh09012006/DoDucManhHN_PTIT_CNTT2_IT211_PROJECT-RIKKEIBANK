package org.example.rikkeibank.repository;

import org.example.rikkeibank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {
}