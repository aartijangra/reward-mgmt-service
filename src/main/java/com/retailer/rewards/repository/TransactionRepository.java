package com.retailer.rewards.repository;

import com.retailer.rewards.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/** JPA repository for Transaction entity operations. */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    /** Finds all transactions for a specific customer. */
    List<Transaction> findByCustomerId(String customerId);

    /** Finds all transactions for a customer within the specified date range. */
    List<Transaction> findByCustomerIdAndTransactionDateBetween(String customerId, LocalDate startDate, LocalDate endDate);
}

