package com.assignment.rewards.dto;

import java.util.List;

public class RewardResponse {

    private Long clientId;
    private List<MonthlyReward> monthlyBreakdown;
    private int totalRewardPoints;

    public RewardResponse(Long clientId,
                          List<MonthlyReward> monthlyBreakdown,
                          int totalRewardPoints) {
        this.clientId = clientId;
        this.monthlyBreakdown = monthlyBreakdown;
        this.totalRewardPoints = totalRewardPoints;
    }

    public Long getClientId() { return clientId; }
    public List<MonthlyReward> getMonthlyBreakdown() { return monthlyBreakdown; }
    public int getTotalRewardPoints() { return totalRewardPoints; }
}
