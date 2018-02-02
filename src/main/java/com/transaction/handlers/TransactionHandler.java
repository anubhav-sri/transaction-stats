package com.transaction.handlers;


import com.transaction.exceptions.TransactionExpiredException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TransactionHandler {
    private TransactionStats currentStats;

    public TransactionHandler() {
        this.currentStats = new TransactionStats();
    }

    public void saveTransaction(Transaction transaction) throws TransactionExpiredException {
        if (isTransactionExpired(transaction)) {
            throw new TransactionExpiredException(transaction);
        }
        updateStats(transaction);
    }

    private boolean isTransactionExpired(Transaction transaction) {
        return ChronoUnit.SECONDS.between(transaction.getTransactionTime(), LocalDateTime.now()) > 60;
    }

    private void updateStats(Transaction transaction) {
        double updatedSum = currentStats.getSum() + transaction.getAmount();
        int updatedCount = currentStats.getCount() + 1;
        double updateAvg = (updatedSum) / updatedCount;
        double updatedMax = transaction.getAmount() > currentStats.getMax() ? transaction.getAmount() : currentStats.getMax();

        currentStats = new TransactionStats(updateAvg, updatedMax, updatedSum, updatedCount);
    }

    public TransactionStats getCurrentTransactionStats() {
        return currentStats;
    }
}
