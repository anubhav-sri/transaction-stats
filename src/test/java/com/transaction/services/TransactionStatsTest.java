package com.transaction.services;

import com.transaction.models.Transaction;
import com.transaction.models.TransactionStats;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TransactionStatsTest {

    @Test
    public void shouldAddTransactionToStatsAndCountItProperly() {
        TransactionStats transactionStats = new TransactionStats();
        transactionStats.add(new Transaction(12L,123124));
        transactionStats.add(new Transaction(13L,123124));

        assertThat(transactionStats).isEqualTo(new TransactionStats(25L,12.5,13L,2));
    }
}