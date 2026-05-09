package com.example.nexuspay.service;

import com.example.nexuspay.entity.User;
import com.example.nexuspay.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Counter;

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
            if (sender.getBalance() < amount) {
                throw new RuntimeException("Insufficient balance");
            }

            // Deduct from sender
            sender.setBalance(sender.getBalance() - amount);

            // Simulate real-world delay
            Thread.sleep(2000);

            // Add to receiver
            receiver.setBalance(receiver.getBalance() + amount);

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