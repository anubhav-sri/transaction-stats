package com.transaction.exceptions;

import com.transaction.handlers.Transaction;

public class TransactionExpiredException extends Exception {
    public TransactionExpiredException(Transaction transaction) {
        super(String.format("amount:%s, time:%s", transaction.getAmount(), transaction.getTimestamp()));
    }
}
