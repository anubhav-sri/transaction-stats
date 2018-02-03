package com.transaction.controllers;

import com.transaction.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

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
}