package com.assignment.rewards.service;

import com.assignment.rewards.dto.*;
import com.assignment.rewards.entity.Transaction;
import com.assignment.rewards.repository.TransactionRepository;
import com.assignment.rewards.util.RewardCalculator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RewardService {

    private final TransactionRepository txnRepo;

    public RewardService(TransactionRepository txnRepo) {
        this.txnRepo = txnRepo;
    }

    @Cacheable("allRewards")
    public Page<RewardResponse> getAllCustomerRewards(
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageReq) {

        List<Transaction> txnList =
                txnRepo.findByTxnDateBetween(startDate, endDate);

        // Group by client
        Map<Long, List<Transaction>> clientGroupMap =
                txnList.stream()
                        .collect(Collectors.groupingBy(Transaction::getClientId));

        // Build reward response per client
        List<RewardResponse> rewardList =
                clientGroupMap.entrySet()
                        .stream()
                        .map(clientEntry ->
                                calculateReward(clientEntry.getKey(), clientEntry.getValue()))
                        .toList();

        // Manual pagination on clients
        int fromIdx = (int) pageReq.getOffset();
        int toIdx =
                Math.min(fromIdx + pageReq.getPageSize(), rewardList.size());

        List<RewardResponse> pageSlice =
                rewardList.subList(fromIdx, toIdx);

        return new PageImpl<>(pageSlice, pageReq, rewardList.size());
    }

    private RewardResponse calculateReward(Long clientId,
                                           List<Transaction> txnList) {

        Map<Month, Integer> monthlyPointsMap = new HashMap<>();
        int cumulativePoints = 0;

        for (Transaction tx : txnList) {

            Month txnMonth = tx.getTxnDate().getMonth();
            int earnedPoints = RewardCalculator.calculatePoints(tx.getPurchaseAmount());

            monthlyPointsMap.merge(txnMonth, earnedPoints, Integer::sum);
            cumulativePoints += earnedPoints;
        }

        List<MonthlyReward> monthlyBreakdowns =
                monthlyPointsMap.entrySet()
                        .stream()
                        .map(monthEntry -> new MonthlyReward(
                                monthEntry.getKey().toString(),
                                monthEntry.getValue()))
                        .toList();

        return new RewardResponse(clientId, monthlyBreakdowns, cumulativePoints);
    }
}
