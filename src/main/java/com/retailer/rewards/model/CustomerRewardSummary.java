package com.retailer.rewards.model;

import java.util.List;

/** DTO representing a customer's complete reward summary. */
public class CustomerRewardSummary {

    private String customerId;

    private String customerName;

    private List<MonthlyReward> monthlyRewards;

    private long totalPoints;

    /** Default constructor. */
    public CustomerRewardSummary() {
    }

    /** Constructs a CustomerRewardSummary with the specified details. */
    public CustomerRewardSummary(String customerId, String customerName,
                                 List<MonthlyReward> monthlyRewards, long totalPoints) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.monthlyRewards = monthlyRewards;
        this.totalPoints = totalPoints;
    }

    /** Returns the customer ID. */
    public String getCustomerId() {
        return customerId;
    }

    /** Sets the customer ID. */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /** Returns the customer name. */
    public String getCustomerName() {
        return customerName;
    }

    /** Sets the customer name. */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /** Returns the list of monthly rewards. */
    public List<MonthlyReward> getMonthlyRewards() {
        return monthlyRewards;
    }

    /** Sets the list of monthly rewards. */
    public void setMonthlyRewards(List<MonthlyReward> monthlyRewards) {
        this.monthlyRewards = monthlyRewards;
    }

    /** Returns the total points earned across all months. */
    public long getTotalPoints() {
        return totalPoints;
    }

    /** Sets the total points earned across all months. */
    public void setTotalPoints(long totalPoints) {
        this.totalPoints = totalPoints;
    }

    /** Returns a string representation of the customer reward summary. */
    @Override
    public String toString() {
        return "CustomerRewardSummary{" +
                "customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", monthlyRewards=" + monthlyRewards +
                ", totalPoints=" + totalPoints +
                '}';
    }
}
