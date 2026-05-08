package com.example.nexuspay.controller;

import org.springframework.web.bind.annotation.*;
import com.example.nexuspay.service.TransferService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {

        this.transferService = transferService;

    }

    // 1. TRANSFER API (CORE)
    @PostMapping("/transfer")
    public String transfer(@RequestBody Map<String, Object> request) {
        String from = (String) request.get("from");
        String to = (String) request.get("to");
        Double amount = Double.valueOf(request.get("amount").toString());
        transferService.transfer(from, to, amount);
        return "Transfer successful";
    }

    // 2. VERSION ENDPOINT (FOR CANARY DEMO)
    @GetMapping("/version")
    public Map<String, String> version() {
        return Map.of(
                "version", "v1.0.0",
                "status", "stable"
        );
    }

    // 3. CHAOS ENDPOINT (FOR FAILURE TESTING)
    @PostMapping("/admin/fail")
    public String fail() {
        throw new RuntimeException("Simulated failure");
    }
}
