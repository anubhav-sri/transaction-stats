package com.transaction.handlers;


import com.transaction.exceptions.TransactionExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class TransactionHandler {
    private TransactionStats currentStats;

    @Autowired
    public TransactionHandler() {
        this.currentStats = new TransactionStats();
    }

    public void saveTransaction(@RequestBody Transaction transaction) throws TransactionExpiredException {
        if (isTransactionExpired(transaction)) {
            throw new TransactionExpiredException(transaction);
        }
        updateStats(transaction);
    }

    private boolean isTransactionExpired(Transaction transaction) {
        LocalDateTime transactionTime = transaction.getTransactionDateTime();
        return ChronoUnit.SECONDS.between(transactionTime, LocalDateTime.now(Clock.systemUTC())) > 60;
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
