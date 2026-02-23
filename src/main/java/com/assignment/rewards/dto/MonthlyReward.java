package com.assignment.rewards.dto;

public class MonthlyReward {

    private String monthName;
    private int rewardPoints;

    public MonthlyReward(String monthName, int rewardPoints) {
        this.monthName = monthName;
        this.rewardPoints = rewardPoints;
    }

    public String getMonthName() { return monthName; }
    public int getRewardPoints() { return rewardPoints; }
}
