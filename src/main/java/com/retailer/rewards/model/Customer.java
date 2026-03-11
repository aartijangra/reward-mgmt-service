package com.retailer.rewards.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entity representing a customer in the rewards system. */
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    /** Default constructor for JPA. */
    public Customer() {
    }

    /** Constructs a Customer with the specified ID and name. */
    public Customer(String customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }

    /** Returns the customer ID. */
    public String getCustomerId() {
        return customerId;
    }

    /** Sets the customer ID. */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /** Returns the customer name. */
    public String getCustomerName() {
        return customerName;
    }

    /** Sets the customer name. */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /** Returns a string representation of the customer. */
    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
