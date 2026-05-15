package com.example.nexuspay.service;

import com.example.nexuspay.entity.User;
import com.example.nexuspay.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Counter;

import java.math.BigDecimal;

@Service
public class TransferService {
    private final Counter successCounter;
    private final Counter failureCounter;

    private final UserRepository userRepository;

    public TransferService(UserRepository userRepository,
                           Counter successCounter,
                           Counter failureCounter) {
        this.userRepository = userRepository;
        this.successCounter = successCounter;
        this.failureCounter = failureCounter;
    }

    @Transactional
    public void transfer(String fromAccount, String toAccount, Double amount) throws InterruptedException {

        try {
            // Fetch Sender
            User sender = userRepository.findByAccountNumber(fromAccount)
                    .orElseThrow(() -> new RuntimeException("Sender not found"));

            // Fetch Receiver
            User receiver = userRepository.findByAccountNumber(toAccount)
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));

            // Validate Balance
            if (sender.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
                throw new RuntimeException("Insufficient balance");
            }

            // Deduct from sender
            sender.setBalance(
                    sender.getBalance().subtract(BigDecimal.valueOf(amount))
            );

            // Simulate real-world delay
            Thread.sleep(2000);

            // Add to receiver
            receiver.setBalance(
                    receiver.getBalance().add(BigDecimal.valueOf(amount))
            );

            userRepository.save(sender);
            userRepository.save(receiver);

            // Success Metric
            successCounter.increment();

        } catch (Exception e) {

            // Failure Metric
            failureCounter.increment();

            throw e;
        }
    }
}