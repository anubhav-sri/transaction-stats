package com.transaction.controllers;

import com.transaction.Application;
import com.transaction.persistence.TransactionDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TransactionStatsControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TransactionDatabase transactionDatabase;

    @Before
    public void setUp() throws Exception {
        transactionDatabase.clear();
    }

    @Test
    public void ShouldReturnHttpStatusCreatedOnSuccessfulSave() throws Exception {
        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"amount\":12.3,\"timestamp\":%s}", Instant.now().toEpochMilli())))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));
    }

    @Test
    public void ShouldReturnHttpStatusNoContentTransactionISExpired() throws Exception {
        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"amount\":12.3,\"timestamp\":%s}", Instant.now().minusSeconds(61).toEpochMilli())))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    public void ShouldGetTheTransactionStats() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"amount\":12.3,\"timestamp\":%s}", Instant.now(Clock.systemUTC()).toEpochMilli())))
                .andExpect(status().isCreated());

        this.mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"sum\":12.3,\"avg\":12.3,\"max\":12.3,\"count\":1}"));
    }

    @Test
    public void ShouldOnlyConsiderTheTransactionsHappenedInLast60Sec() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"amount\":12.3,\"timestamp\":%s}", Instant.now().minusSeconds(30).toEpochMilli())))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"amount\":12.3,\"timestamp\":%s}", Instant.now().minusSeconds(12).toEpochMilli())))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"amount\":14.3,\"timestamp\":%s}", Instant.now().minusSeconds(59).toEpochMilli())))
                .andExpect(status().isCreated());

        Thread.sleep(1000);

        this.mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"sum\":24.6,\"avg\":12.3,\"max\":12.3,\"count\":2}"));
    }
}