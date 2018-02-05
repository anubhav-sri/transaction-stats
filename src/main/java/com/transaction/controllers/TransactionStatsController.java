package com.transaction.controllers;

import com.transaction.exceptions.TransactionExpiredException;
import com.transaction.models.Transaction;
import com.transaction.models.TransactionStats;
import com.transaction.services.TransactionStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionStatsController {
    private TransactionStatsService transactionStatsService;

    @Autowired
    public TransactionStatsController(TransactionStatsService transactionStatsService) {
        this.transactionStatsService = transactionStatsService;
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity saveTransactions(@RequestBody Transaction transaction) {
        try {
            transactionStatsService.saveTransaction(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body("");
        } catch (TransactionExpiredException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        }
    }

    @RequestMapping(value = "/statistics")
    public TransactionStats getStatistics() {
        return transactionStatsService.getCurrentStats();
    }
}
