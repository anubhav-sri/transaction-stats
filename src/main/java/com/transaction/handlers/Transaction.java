package com.transaction.handlers;

import java.time.LocalDateTime;

public class Transaction {
    private final double amount;
    private final LocalDateTime transactionTime;

    public Transaction(double amount, LocalDateTime transactionTime) {
        this.amount = amount;
        this.transactionTime = transactionTime;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }
}
