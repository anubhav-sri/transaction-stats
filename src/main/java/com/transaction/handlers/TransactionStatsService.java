package com.transaction.handlers;


import com.transaction.exceptions.TransactionExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class TransactionStatsService {
    private TransactionStats currentStats;

    @Autowired
    public TransactionStatsService() {
        this.currentStats = new TransactionStats();
    }

    public void saveTransaction(Transaction transaction) throws TransactionExpiredException {
        if (isTransactionExpired(transaction)) {
            throw new TransactionExpiredException(transaction);
        }
        updateStats(transaction);
    }

    public TransactionStats getCurrentTransactionStats() {
        return currentStats;
    }

    @Scheduled(fixedDelayString = "${transaction.stats.interval.millis}")
    public void resetStats() {
        this.currentStats = new TransactionStats();
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

        currentStats = new TransactionStats(updatedSum, updateAvg, updatedMax, updatedCount);
    }
}
