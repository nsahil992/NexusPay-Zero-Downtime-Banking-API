package com.example.nexuspay.service;

import com.example.nexuspay.entity.User;
import com.example.nexuspay.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TransferService {
    private final UserRepository userRepository;

    public TransferService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void transfer(String fromAccount, String toAccount, double amount) {
        // Fetch Sender
        User sender = userRepository.findByAccountNumber(fromAccount)
                .orElseThrow(() -> new RuntimeException("sender not found"));

        // Fetch Receiver
        User receiver = userRepository.findByAccountNumber(toAccount)
                .orElseThrow(() -> new RuntimeException("receiver not found"));

        // Validate Balance
        if (sender.getBalance() < amount) {
            throw new RuntimeException("insufficient funds");
        }

        // Deduct from sender
        sender.setBalance(sender.getBalance() - amount);

        // Simulate real-world delay
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Add to receiver
        receiver.setBalance(receiver.getBalance() + amount);

        // Save both
        userRepository.save(sender);
        userRepository.save(receiver);
    }
}
