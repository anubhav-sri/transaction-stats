package com.transaction;

import com.transaction.handlers.TransactionDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class Configs {
    @Bean
    public TransactionDatabase transactionDatabase() {
        return new TransactionDatabase();
    }

    @Bean
    public Clock clock(){
        return Clock.systemUTC();
    }
}
