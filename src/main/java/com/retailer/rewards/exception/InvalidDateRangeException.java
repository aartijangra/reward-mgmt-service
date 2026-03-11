package com.retailer.rewards.exception;

/** Exception thrown when an invalid date range is provided. */
public class InvalidDateRangeException extends RuntimeException {

    /** Constructs an InvalidDateRangeException with the specified detail message. */
    public InvalidDateRangeException(String message) {
        super(message);
    }
}
