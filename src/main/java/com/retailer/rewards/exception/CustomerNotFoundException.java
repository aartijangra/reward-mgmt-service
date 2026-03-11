package com.retailer.rewards.exception;

/** Exception thrown when a customer is not found in the system. */
public class CustomerNotFoundException extends RuntimeException {

    /** Constructs a CustomerNotFoundException with the specified detail message. */
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
