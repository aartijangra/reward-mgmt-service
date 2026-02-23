package com.assignment.rewards.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateParseException() {
        return ResponseEntity
                .badRequest()
                .body("Invalid date format. Use yyyy-MM-dd");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException() {
        return ResponseEntity
                .internalServerError()
                .body("Unexpected error occurred");
    }
}