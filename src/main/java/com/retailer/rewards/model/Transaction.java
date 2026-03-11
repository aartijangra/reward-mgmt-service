package com.retailer.rewards.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

/** Entity representing a customer transaction. */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(nullable = false)
    private double amount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    /** Default constructor for JPA. */
    public Transaction() {
    }

    /** Constructs a Transaction with the specified details. */
    public Transaction(String transactionId, String customerId, double amount, LocalDate transactionDate) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    /** Returns the transaction ID. */
    public String getTransactionId() {
        return transactionId;
    }

    /** Sets the transaction ID. */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /** Returns the customer ID. */
    public String getCustomerId() {
        return customerId;
    }

    /** Sets the customer ID. */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /** Returns the transaction amount. */
    public double getAmount() {
        return amount;
    }

    /** Sets the transaction amount. */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /** Returns the transaction date. */
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    /** Sets the transaction date. */
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    /** Returns a string representation of the transaction. */
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", amount=" + amount +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
