package com.assignment.rewards.service;

import com.assignment.rewards.dto.RewardResponse;
import com.assignment.rewards.entity.Transaction;
import com.assignment.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for RewardService.
 */
class RewardServiceTest {

    private TransactionRepository txnRepo;
    private RewardService rewardSvc;

    @BeforeEach
    void setup() {
        txnRepo = Mockito.mock(TransactionRepository.class);
        rewardSvc = new RewardService(txnRepo);
    }

    @Test
    void shouldCalculateRewardsForMultipleCustomers() {

        List<Transaction> txnList = List.of(
                new Transaction(1L, 1L, 120.0,
                        LocalDate.of(2024, 1, 10)),
                new Transaction(2L, 1L, 80.0,
                        LocalDate.of(2024, 1, 15)),
                new Transaction(3L, 2L, 200.0,
                        LocalDate.of(2024, 2, 10))
        );

        when(txnRepo.findByTxnDateBetween(
                LocalDate.of(2024,1,1),
                LocalDate.of(2024,3,31)))
                .thenReturn(txnList);

        Page<RewardResponse> result =
                rewardSvc.getAllCustomerRewards(
                        LocalDate.of(2024,1,1),
                        LocalDate.of(2024,3,31),
                        PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
    }

    @Test
    void shouldReturnEmptyWhenNoTransactions() {

        when(txnRepo.findByTxnDateBetween(
                LocalDate.now(),
                LocalDate.now()))
                .thenReturn(List.of());

        Page<RewardResponse> result =
                rewardSvc.getAllCustomerRewards(
                        LocalDate.now(),
                        LocalDate.now(),
                        PageRequest.of(0, 10));

        assertTrue(result.isEmpty());
    }
}
