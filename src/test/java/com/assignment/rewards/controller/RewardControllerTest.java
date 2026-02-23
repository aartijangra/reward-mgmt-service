package com.assignment.rewards.controller;

import com.assignment.rewards.entity.Transaction;
import com.assignment.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for RewardController.
 */
@SpringBootTest
@AutoConfigureMockMvc
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository txnRepo;

    @Test
    void shouldReturnRewards() throws Exception {

        txnRepo.save(new Transaction(null, 1L,
                150.0, LocalDate.of(2024, 1, 10)));

        mockMvc.perform(get("/api/rewards")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-03-31"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestIfInvalidDateRange() throws Exception {

        mockMvc.perform(get("/api/rewards")
                        .param("startDate", "2024-03-31")
                        .param("endDate", "2024-01-01"))
                .andExpect(status().isBadRequest());
    }
}

