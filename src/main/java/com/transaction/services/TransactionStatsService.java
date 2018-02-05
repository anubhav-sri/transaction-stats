package com.transaction.services;


import com.transaction.exceptions.TransactionExpiredException;
import com.transaction.models.Transaction;
import com.transaction.models.TransactionStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class TransactionStatsService {
    private static final int TIME_TO_CONSIDER_FOR_STATS = 60000;
    private TransactionDatabase transactionMap;
    private Clock clock;

    @Autowired
    public TransactionStatsService(TransactionDatabase transactionMap, Clock clock) {
        this.transactionMap = transactionMap;
        this.clock = clock;
    }

    public void saveTransaction(Transaction transaction) throws TransactionExpiredException {
        if (isTransactionExpired(transaction)) {
            throw new TransactionExpiredException(transaction);
        }
        updateStats(transaction);
    }

    public TransactionStats getCurrentStats() {
        long currentMillis = clock.millis();
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
        return clock.millis() - transaction.getTimestamp() > TIME_TO_CONSIDER_FOR_STATS;
    }

    private synchronized void updateStats(Transaction transaction) {
        transactionMap.put(transaction.getTimestamp(), transaction);
    }
}
