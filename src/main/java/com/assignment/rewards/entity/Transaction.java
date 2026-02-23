package com.assignment.rewards.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Double purchaseAmount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate txnDate;

    public Transaction() {
    }

    public Transaction(Long id, Long clientId, Double purchaseAmount, LocalDate txnDate) {
        this.id = id;
        this.clientId = clientId;
        this.purchaseAmount = purchaseAmount;
        this.txnDate = txnDate;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public LocalDate getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(LocalDate txnDate) {
        this.txnDate = txnDate;
    }
}