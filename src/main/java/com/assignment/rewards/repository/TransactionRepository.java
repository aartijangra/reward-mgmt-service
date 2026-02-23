package com.assignment.rewards.repository;

import com.assignment.rewards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByTxnDateBetween(
            LocalDate fromDate,
            LocalDate toDate);
}
