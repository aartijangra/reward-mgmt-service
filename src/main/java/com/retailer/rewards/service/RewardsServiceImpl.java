package com.retailer.rewards.service;

import com.retailer.rewards.data.CustomerDataStore;
import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.exception.InvalidDateRangeException;
import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.CustomerRewardSummary;
import com.retailer.rewards.model.MonthlyReward;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.util.PointsCalculator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Service implementation for customer reward calculations. */
@Service
public class RewardsServiceImpl implements RewardsService {

    private static final DateTimeFormatter MONTH_LABEL_FORMATTER =
            DateTimeFormatter.ofPattern("MMMM yyyy");

    private final CustomerDataStore dataProvider;

    /** Constructs a RewardsServiceImpl with the specified data provider. */
    public RewardsServiceImpl(CustomerDataStore dataProvider) {
        this.dataProvider = dataProvider;
    }

    /** Retrieves reward summaries for all customers within the specified date range. */
    @Override
    public Page<CustomerRewardSummary> getAllCustomerRewards(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        validateDateRange(startDate, endDate);
        Page<Customer> customerPage = dataProvider.getCustomersPage(pageable);
        List<CustomerRewardSummary> summaries = customerPage.getContent().stream()
                .map(customer -> buildCustomerRewardSummary(
                        customer,
                        dataProvider.findTransactionsByCustomerIdAndDateRange(
                                customer.getCustomerId(), startDate, endDate)))
                .collect(Collectors.toList());
        return new PageImpl<>(summaries, pageable, customerPage.getTotalElements());
    }

    /** Retrieves the reward summary for a specific customer within the specified date range. */
    @Override
    public CustomerRewardSummary getCustomerRewards(String customerId, LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        Customer customer = dataProvider.findCustomerById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found with ID: " + customerId));
        List<Transaction> transactions = dataProvider.findTransactionsByCustomerIdAndDateRange(
                customerId, startDate, endDate);
        return buildCustomerRewardSummary(customer, transactions);
    }

    /** Validates that the start date is not after the end date. */
    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException(
                    "Start date must not be after end date: " + startDate + " > " + endDate);
        }
    }

    /** Builds a customer reward summary from customer data and transactions. */
    private CustomerRewardSummary buildCustomerRewardSummary(Customer customer,
                                                              List<Transaction> transactions) {
        Map<YearMonth, Long> pointsByYearMonth = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(t.getTransactionDate()),
                        Collectors.summingLong(t -> PointsCalculator.computePoints(t.getAmount()))
                ));

        List<MonthlyReward> monthlyRewards = pointsByYearMonth.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new MonthlyReward(
                        entry.getKey().format(MONTH_LABEL_FORMATTER),
                        entry.getValue()))
                .collect(Collectors.toList());

        long totalPoints = monthlyRewards.stream()
                .mapToLong(MonthlyReward::getPoints)
                .sum();

        return new CustomerRewardSummary(
                customer.getCustomerId(),
                customer.getCustomerName(),
                monthlyRewards,
                totalPoints);
    }
}

