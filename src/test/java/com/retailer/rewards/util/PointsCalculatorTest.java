package com.retailer.rewards.util;

import com.retailer.rewards.exception.InvalidTransactionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointsCalculatorTest {


    @Test
    @DisplayName("$120 → 90 pts")
    void computePoints_WhenAmountOver100_ReturnsCorrectPoints() {
        assertEquals(90L, PointsCalculator.computePoints(120.0));
    }


    @Test
    @DisplayName("$200 → 250 pts")
    void computePoints_WhenAmountFarOver100_ReturnsCorrectPoints() {
        assertEquals(250L, PointsCalculator.computePoints(200.0));
    }


    @Test
    @DisplayName("$101 (just above upper boundary) → 52 pts")
    void computePoints_WhenAmountJustAbove100_Returns52Points() {
        assertEquals(52L, PointsCalculator.computePoints(101.0));
    }


    @Test
    @DisplayName("$75 → 25 pts")
    void computePoints_WhenAmountBetween50And100_ReturnsCorrectPoints() {
        assertEquals(25L, PointsCalculator.computePoints(75.0));
    }


    @Test
    @DisplayName("$100 (upper boundary of 1-pt tier) → 50 pts")
    void computePoints_WhenAmountExactly100_Returns50Points() {
        assertEquals(50L, PointsCalculator.computePoints(100.0));
    }


    @Test
    @DisplayName("$51 (just above lower boundary) → 1 pt")
    void computePoints_WhenAmountJustAbove50_Returns1Point() {
        assertEquals(1L, PointsCalculator.computePoints(51.0));
    }


    @Test
    @DisplayName("$50 (lower boundary) → 0 pts")
    void computePoints_WhenAmountExactly50_Returns0Points() {
        assertEquals(0L, PointsCalculator.computePoints(50.0));
    }


    @Test
    @DisplayName("$30 → 0 pts")
    void computePoints_WhenAmountBelow50_Returns0Points() {
        assertEquals(0L, PointsCalculator.computePoints(30.0));
    }


    @Test
    @DisplayName("$0 → 0 pts")
    void computePoints_WhenAmountIsZero_Returns0Points() {
        assertEquals(0L, PointsCalculator.computePoints(0.0));
    }


    @Test
    @DisplayName("$120.99 (fraction truncated to $120) → 90 pts")
    void computePoints_WhenAmountHasFraction_TruncatesCorrectly() {
        assertEquals(90L, PointsCalculator.computePoints(120.99));
    }


    @Test
    @DisplayName("negative amount → InvalidTransactionException")
    void computePoints_WhenAmountIsNegative_ThrowsInvalidTransactionException() {
        InvalidTransactionException ex = assertThrows(
                InvalidTransactionException.class,
                () -> PointsCalculator.computePoints(-10.0));
        assertTrue(ex.getMessage().contains("-10.0"));
    }
}
