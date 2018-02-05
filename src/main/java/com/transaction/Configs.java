package com.transaction;

import com.transaction.handlers.TransactionDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configs {
    @Bean
    public TransactionDatabase transactionDatabase() {
        return new TransactionDatabase();
    }
}
