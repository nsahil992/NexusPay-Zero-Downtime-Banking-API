package com.example.nexuspay.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter successCounter(MeterRegistry registry) {
        return Counter.builder("transaction_success_total")
                .description("Total successful transactions")
                .register(registry);
    }

    @Bean
    public Counter failureCounter(MeterRegistry registry) {
        return Counter.builder("transaction_failure_total")
                .description("Total failed transactions")
                .register(registry);
    }
}