package com.retailer.rewards.exception;

import java.time.LocalDateTime;

/** Standardized error response structure for API exceptions. */
public class ErrorResponse {

    private int status;

    private String error;

    private String message;

    private LocalDateTime timestamp;

    /** Constructs an ErrorResponse with the specified details. */
    public ErrorResponse(int status, String error, String message, LocalDateTime timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }

    /** Returns the HTTP status code. */
    public int getStatus() {
        return status;
    }

    /** Sets the HTTP status code. */
    public void setStatus(int status) {
        this.status = status;
    }

    /** Returns the error type. */
    public String getError() {
        return error;
    }

    /** Sets the error type. */
    public void setError(String error) {
        this.error = error;
    }

    /** Returns the error message. */
    public String getMessage() {
        return message;
    }

    /** Sets the error message. */
    public void setMessage(String message) {
        this.message = message;
    }

    /** Returns the timestamp when the error occurred. */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /** Sets the timestamp when the error occurred. */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
