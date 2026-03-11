package com.retailer.rewards.service;

import com.retailer.rewards.model.CustomerRewardSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/** Service interface for customer reward calculations. */
public interface RewardsService {

    /** Retrieves reward summaries for all customers within the specified date range. */
    Page<CustomerRewardSummary> getAllCustomerRewards(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /** Retrieves the reward summary for a specific customer within the specified date range. */
    CustomerRewardSummary getCustomerRewards(String customerId, LocalDate startDate, LocalDate endDate);
}

