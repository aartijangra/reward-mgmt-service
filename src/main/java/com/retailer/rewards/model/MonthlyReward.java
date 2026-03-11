package com.retailer.rewards.model;

/** DTO representing reward points earned in a specific month. */
public class MonthlyReward {

    private String month;

    private long points;

    /** Default constructor. */
    public MonthlyReward() {
    }

    /** Constructs a MonthlyReward with the specified month and points. */
    public MonthlyReward(String month, long points) {
        this.month = month;
        this.points = points;
    }

    /** Returns the month label. */
    public String getMonth() {
        return month;
    }

    /** Sets the month label. */
    public void setMonth(String month) {
        this.month = month;
    }

    /** Returns the points earned in this month. */
    public long getPoints() {
        return points;
    }

    /** Sets the points earned in this month. */
    public void setPoints(long points) {
        this.points = points;
    }

    /** Returns a string representation of the monthly reward. */
    @Override
    public String toString() {
        return "MonthlyReward{" +
                "month='" + month + '\'' +
                ", points=" + points +
                '}';
    }
}
