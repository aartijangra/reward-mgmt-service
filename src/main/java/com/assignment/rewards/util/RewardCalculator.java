package com.assignment.rewards.util;

public class RewardCalculator {

    private RewardCalculator() {}

    public static int calculatePoints(double purchaseAmt) {

        if (purchaseAmt <= 50) return 0;

        if (purchaseAmt <= 100) {
            return (int) (purchaseAmt - 50);
        }

        int midTierPts = 50;
        int upperTierPts = (int) ((purchaseAmt - 100) * 2);
        return midTierPts + upperTierPts;
    }
}
