package com.transaction.handlers;


import com.transaction.exceptions.TransactionExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class TransactionStatsService {
    private static final int TIME_TO_CONSIDER_FOR_STATS = 60000;
    private TransactionDatabase transactionMap;

    @Autowired
    public TransactionStatsService(TransactionDatabase transactionMap) {
        this.transactionMap = transactionMap;
    }

    public void saveTransaction(Transaction transaction) throws TransactionExpiredException {
        if (isTransactionExpired(transaction)) {
            throw new TransactionExpiredException(transaction);
        }
        updateStats(transaction);
    }

    public TransactionStats getCurrentStats() {
        long currentMillis = Instant.now(Clock.systemUTC()).toEpochMilli();
        long lastMillisToConsider = currentMillis - TIME_TO_CONSIDER_FOR_STATS;
        TransactionStats transactionStats = new TransactionStats();

        for (long millisToStartCount = lastMillisToConsider; millisToStartCount <= currentMillis; millisToStartCount++) {
            if (transactionMap.containsKey(millisToStartCount)) {
                transactionStats.add(transactionMap.get(millisToStartCount));
            }
        }
        return transactionStats;
    }

    private boolean isTransactionExpired(Transaction transaction) {
        LocalDateTime transactionTime = transaction.getTransactionDateTime();
        return ChronoUnit.SECONDS.between(transactionTime, LocalDateTime.now(Clock.systemUTC())) > 60;
    }

    private synchronized void updateStats(Transaction transaction) {
        transactionMap.put(transaction.getTimestamp(), transaction);
    }
}
