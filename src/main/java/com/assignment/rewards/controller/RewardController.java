package com.assignment.rewards.controller;

import com.assignment.rewards.dto.RewardResponse;
import com.assignment.rewards.service.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardSvc;

    public RewardController(RewardService rewardSvc) {
        this.rewardSvc = rewardSvc;
    }

    @Operation(summary = "Get rewards for all customers with pagination")
    @GetMapping
    public ResponseEntity<Page<RewardResponse>> getAllRewards(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize) {

        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }

        Pageable pageReq = PageRequest.of(pageNum, pageSize);

        Page<RewardResponse> rewardPage =
                rewardSvc.getAllCustomerRewards(startDate, endDate, pageReq);

        return ResponseEntity.ok(rewardPage);
    }
}