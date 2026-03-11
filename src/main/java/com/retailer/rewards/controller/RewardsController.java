package com.retailer.rewards.controller;

import com.retailer.rewards.model.CustomerRewardSummary;
import com.retailer.rewards.service.RewardsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/** REST controller for managing customer reward points. */
@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

    private final RewardsService rewardsService;

    /** Constructs a RewardsController with the specified rewards service. */
    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    /** Retrieves reward summaries for all customers within the specified date range. */
    @GetMapping
    public ResponseEntity<Page<CustomerRewardSummary>> getAllRewards(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(rewardsService.getAllCustomerRewards(startDate, endDate, pageable));
    }

    /** Retrieves the reward summary for a specific customer within the specified date range. */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerRewardSummary> getCustomerRewards(
            @PathVariable String customerId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(rewardsService.getCustomerRewards(customerId, startDate, endDate));
    }
}

