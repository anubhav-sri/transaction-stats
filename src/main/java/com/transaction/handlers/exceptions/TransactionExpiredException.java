package com.transaction.handlers.exceptions;

import com.transaction.handlers.Transaction;

import java.time.ZoneOffset;

public class TransactionExpiredException extends Exception {
    public TransactionExpiredException(Transaction transaction) {
        super(String.format("amount:%s, time:%s", transaction.getAmount(), transaction.getTransactionTime().toEpochSecond(ZoneOffset.UTC)));
    }
}
