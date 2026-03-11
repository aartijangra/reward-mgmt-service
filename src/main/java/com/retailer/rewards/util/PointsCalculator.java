package com.retailer.rewards.util;

import com.retailer.rewards.exception.InvalidTransactionException;

/** Utility class for calculating reward points based on transaction amounts. */
public final class PointsCalculator {

    /** Private constructor to prevent instantiation. */
    private PointsCalculator() {
        throw new UnsupportedOperationException("Utility class");
    }

    /** Computes reward points for a given transaction amount. */
    public static long computePoints(double amount) {
        if (amount < 0) {
            throw new InvalidTransactionException(
                    "Transaction amount cannot be negative: " + amount);
        }

        long wholeDollars = (long) amount;
        long points = 0;

        if (wholeDollars > 100) {
            points += 2L * (wholeDollars - 100);
            points += 50L;
        } else if (wholeDollars > 50) {
            points += wholeDollars - 50;
        }

        return points;
    }
}
