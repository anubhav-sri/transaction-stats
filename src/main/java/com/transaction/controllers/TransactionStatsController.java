package com.transaction.controllers;

import com.transaction.exceptions.TransactionExpiredException;
import com.transaction.handlers.Transaction;
import com.transaction.handlers.TransactionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TransactionStatsController {
    private TransactionHandler transactionHandler;

    public TransactionStatsController(TransactionHandler transactionHandler) {

        this.transactionHandler = transactionHandler;
    }


    public ResponseEntity saveTransactions(Transaction transaction) {
        try {
            transactionHandler.saveTransaction(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body("");
        } catch (TransactionExpiredException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        }
    }
}
