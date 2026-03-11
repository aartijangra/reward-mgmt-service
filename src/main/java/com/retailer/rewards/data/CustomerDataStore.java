package com.retailer.rewards.data;

import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Data access interface for customer and transaction operations. */
public interface CustomerDataStore {

    /** Retrieves a paginated list of customers. */
    Page<Customer> getCustomersPage(Pageable pageable);

    /** Finds a customer by their unique identifier. */
    Optional<Customer> findCustomerById(String customerId);

    /** Finds all transactions for a customer within the specified date range. */
    List<Transaction> findTransactionsByCustomerIdAndDateRange(String customerId, LocalDate startDate, LocalDate endDate);
}

