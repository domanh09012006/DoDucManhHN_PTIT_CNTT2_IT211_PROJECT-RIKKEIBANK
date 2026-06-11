package org.example.rikkeibank.repository;

import org.example.rikkeibank.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.fromAccount.accountNumber = :accountNumber " +
            "OR t.toAccount.accountNumber = :accountNumber " +
            "ORDER BY t.createdAt DESC")
    Page<Transaction> findTransactionHistory(
            @Param("accountNumber") String accountNumber,
            Pageable pageable);
}