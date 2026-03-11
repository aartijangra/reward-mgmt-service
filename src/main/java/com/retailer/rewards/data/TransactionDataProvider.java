package com.retailer.rewards.data;

import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Data provider implementation for customer and transaction data access. */
@Component
public class TransactionDataProvider implements CustomerDataStore {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    /** Constructs a TransactionDataProvider with the specified repositories. */
    public TransactionDataProvider(CustomerRepository customerRepository,
                                   TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    /** Retrieves a paginated list of customers. */
    @Override
    public Page<Customer> getCustomersPage(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    /** Finds a customer by their unique identifier. */
    @Override
    public Optional<Customer> findCustomerById(String customerId) {
        return customerRepository.findById(customerId);
    }

    /** Finds all transactions for a customer within the specified date range. */
    @Override
    public List<Transaction> findTransactionsByCustomerIdAndDateRange(String customerId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate);
    }
}

