package com.transaction.handlers;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Transaction {
    private double amount;
    private long timestamp;

    public Transaction(double amount, long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Transaction() {
    }

    public double getAmount() {
        return amount;
    }

    @JsonIgnore
    public LocalDateTime getTransactionDateTime() {
        return Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    public long getTimestamp() {
        return timestamp;
    }
}
