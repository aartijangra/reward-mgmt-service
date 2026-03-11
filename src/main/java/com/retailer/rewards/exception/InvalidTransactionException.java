package com.retailer.rewards.exception;

/** Exception thrown when a transaction contains invalid data. */
public class InvalidTransactionException extends RuntimeException {

    /** Constructs an InvalidTransactionException with the specified detail message. */
    public InvalidTransactionException(String message) {
        super(message);
    }
}
