package com.example.nexuspay;

import com.example.nexuspay.entity.User;
import com.example.nexuspay.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NexuspayApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setupUsers() {

        userRepository.deleteAll();

        User sender = new User();
        sender.setAccountNumber("ACC1001");
        sender.setBalance(10000.0);

        User receiver = new User();
        receiver.setAccountNumber("ACC1002");
        receiver.setBalance(5000.0);

        userRepository.save(sender);
        userRepository.save(receiver);
    }

    @Test
    void contextLoads() {
    }

    // HEALTHCHECK ENDPOINT
    @Test
    void healthcheckEndpointShouldReturn200() throws Exception {

        mockMvc.perform(get("/api/v1/healthcheck"))
                .andExpect(status().isOk());

    }

    // ACTUATOR HEALTH
    @Test
    void actuatorHealthShouldReturn200() throws Exception {

        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());

    }

    // VERSION ENDPOINT
    @Test
    void versionEndpointShouldReturn200() throws Exception {

        mockMvc.perform(get("/api/v1/version"))
                .andExpect(status().isOk());

    }

    // SUCCESSFUL TRANSFER
    @Test
    void transferEndpointShouldReturn200AndUpdateBalances() throws Exception {

        String requestBody = """
                {
                    "from": "ACC1001",
                    "to": "ACC1002",
                    "amount": 500
                }
                """;

        mockMvc.perform(post("/api/v1/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        User sender =
                userRepository.findByAccountNumber("ACC1001").orElseThrow();

        User receiver =
                userRepository.findByAccountNumber("ACC1002").orElseThrow();

        assertEquals(9500.0, sender.getBalance());
        assertEquals(5500.0, receiver.getBalance());

    }

    // FAILED TRANSFER (INSUFFICIENT BALANCE)
    @Test
    void transferShouldFailIfInsufficientBalance() {

        String requestBody = """
            {
                "from": "ACC1001",
                "to": "ACC1002",
                "amount": 500000
            }
            """;

        assertThrows(Exception.class, () -> {

            mockMvc.perform(post("/api/v1/transfer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

        });

    }

    // FAILED TRANSFER (SENDER NOT FOUND)
    @Test
    void transferShouldFailIfSenderDoesNotExist() {

        String requestBody = """
            {
                "from": "INVALID",
                "to": "ACC1002",
                "amount": 100
            }
            """;

        assertThrows(Exception.class, () -> {

            mockMvc.perform(post("/api/v1/transfer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

        });

    }

    // CHAOS ENDPOINT
    @Test
    void chaosEndpointShouldThrowException() {

        assertThrows(Exception.class, () -> {

            mockMvc.perform(post("/api/v1/admin/fail"));

        });

    }
}