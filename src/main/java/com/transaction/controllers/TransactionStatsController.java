package com.transaction.controllers;

import com.transaction.exceptions.TransactionExpiredException;
import com.transaction.handlers.Transaction;
import com.transaction.handlers.TransactionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionStatsController {
    private TransactionHandler transactionHandler;

    @Autowired
    public TransactionStatsController(TransactionHandler transactionHandler) {
        this.transactionHandler = transactionHandler;
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity saveTransactions(@RequestBody Transaction transaction) {
        try {
            transactionHandler.saveTransaction(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body("");
        } catch (TransactionExpiredException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        }
    }
}
