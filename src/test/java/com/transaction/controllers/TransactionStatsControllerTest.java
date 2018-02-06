package com.transaction.controllers;

import com.transaction.exceptions.TransactionExpiredException;
import com.transaction.models.Transaction;
import com.transaction.services.TransactionsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TransactionStatsControllerTest {

    @Mock
    private TransactionsService transactionsService;
    private TransactionStatsController transactionStatsController;

    @Before
    public void setUp() throws Exception {
        transactionStatsController = new TransactionStatsController(transactionsService);
    }

    @Test
    public void shouldSaveTheTransactionAndReturnCreatedResponse() throws TransactionExpiredException {
        Transaction transaction = new Transaction(123.0, Clock.systemUTC().millis());

        ResponseEntity responseEntity = transactionStatsController.saveTransactions(transaction);

        verify(transactionsService).saveTransaction(transaction);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void shouldReturnNonContentResponseIfTransactionExpired() throws TransactionExpiredException {
        Transaction transaction = new Transaction(123.0, LocalDateTime.now().minusSeconds(61).toInstant(ZoneOffset.UTC).toEpochMilli());

        doThrow(new TransactionExpiredException(transaction))
                .when(transactionsService)
                .saveTransaction(transaction);

        ResponseEntity responseEntity = transactionStatsController.saveTransactions(transaction);

        verify(transactionsService).saveTransaction(transaction);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}