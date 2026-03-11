package com.retailer.rewards.controller;

import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.exception.InvalidDateRangeException;
import com.retailer.rewards.model.CustomerRewardSummary;
import com.retailer.rewards.model.MonthlyReward;
import com.retailer.rewards.service.RewardsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RewardsControllerTest {

    @Mock
    private RewardsService rewardsService;

    @InjectMocks
    private RewardsController rewardsController;

    private static final LocalDate START = LocalDate.of(2024, 1, 1);
    private static final LocalDate END = LocalDate.of(2024, 3, 31);
    private static final Pageable PAGE = PageRequest.of(0, 10);

    @Test
    @DisplayName("GET /api/rewards → 200 with all summaries")
    void getAllRewards_ReturnsOkWithAllSummaries() {
        List<MonthlyReward> aliceMonthly = Arrays.asList(
                new MonthlyReward("January 2024", 115L),
                new MonthlyReward("February 2024", 250L)
        );
        CustomerRewardSummary alice = new CustomerRewardSummary("C001", "Alice Johnson", aliceMonthly, 365L);

        List<MonthlyReward> bobMonthly = Collections.singletonList(
                new MonthlyReward("January 2024", 50L)
        );
        CustomerRewardSummary bob = new CustomerRewardSummary("C002", "Bob Smith", bobMonthly, 50L);

        Page<CustomerRewardSummary> resultPage = new PageImpl<>(Arrays.asList(alice, bob));
        when(rewardsService.getAllCustomerRewards(any(LocalDate.class), any(LocalDate.class), any(Pageable.class)))
                .thenReturn(resultPage);

        ResponseEntity<Page<CustomerRewardSummary>> response = rewardsController.getAllRewards(START, END, 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals("C001", response.getBody().getContent().get(0).getCustomerId());
        assertEquals("Alice Johnson", response.getBody().getContent().get(0).getCustomerName());
        assertEquals(365L, response.getBody().getContent().get(0).getTotalPoints());
        assertEquals(2, response.getBody().getContent().get(0).getMonthlyRewards().size());
        assertEquals("C002", response.getBody().getContent().get(1).getCustomerId());
        assertEquals(50L, response.getBody().getContent().get(1).getTotalPoints());
    }

    @Test
    @DisplayName("GET /api/rewards → 200 with empty array when no customers")
    void getAllRewards_WhenNoCustomers_ReturnsEmptyArray() {
        when(rewardsService.getAllCustomerRewards(any(LocalDate.class), any(LocalDate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        ResponseEntity<Page<CustomerRewardSummary>> response = rewardsController.getAllRewards(START, END, 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getContent().size());
    }

    @Test
    @DisplayName("GET /api/rewards → 400 when start date is after end date")
    void getAllRewards_StartAfterEnd_ReturnsBadRequest() {
        when(rewardsService.getAllCustomerRewards(any(LocalDate.class), any(LocalDate.class), any(Pageable.class)))
                .thenThrow(new InvalidDateRangeException("Start date must not be after end date"));

        assertThrows(InvalidDateRangeException.class,
                () -> rewardsController.getAllRewards(END, START, 0, 10));
    }

    @Test
    @DisplayName("GET /api/rewards/C001 → 200 with summary for Alice")
    void getCustomerRewards_ValidCustomer_ReturnsOkWithSummary() {
        List<MonthlyReward> monthly = Arrays.asList(
                new MonthlyReward("January 2024", 115L),
                new MonthlyReward("February 2024", 250L),
                new MonthlyReward("March 2024", 205L)
        );
        CustomerRewardSummary alice = new CustomerRewardSummary("C001", "Alice Johnson", monthly, 570L);

        when(rewardsService.getCustomerRewards(eq("C001"), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(alice);

        ResponseEntity<CustomerRewardSummary> response = rewardsController.getCustomerRewards("C001", START, END);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("C001", response.getBody().getCustomerId());
        assertEquals("Alice Johnson", response.getBody().getCustomerName());
        assertEquals(570L, response.getBody().getTotalPoints());
        assertEquals(3, response.getBody().getMonthlyRewards().size());
        assertEquals("January 2024", response.getBody().getMonthlyRewards().get(0).getMonth());
        assertEquals(115L, response.getBody().getMonthlyRewards().get(0).getPoints());
        assertEquals("February 2024", response.getBody().getMonthlyRewards().get(1).getMonth());
        assertEquals(250L, response.getBody().getMonthlyRewards().get(1).getPoints());
        assertEquals("March 2024", response.getBody().getMonthlyRewards().get(2).getMonth());
        assertEquals(205L, response.getBody().getMonthlyRewards().get(2).getPoints());
    }

    @Test
    @DisplayName("GET /api/rewards/C999 → 404 Not Found")
    void getCustomerRewards_UnknownCustomer_Returns404() {
        when(rewardsService.getCustomerRewards(eq("C999"), any(LocalDate.class), any(LocalDate.class)))
                .thenThrow(new CustomerNotFoundException("Customer not found with ID: C999"));

        assertThrows(CustomerNotFoundException.class,
                () -> rewardsController.getCustomerRewards("C999", START, END));
    }

    @Test
    @DisplayName("GET /api/rewards/C002 → 200 with 0 points when no transactions")
    void getCustomerRewards_CustomerNoTransactions_ReturnsZeroPoints() {
        CustomerRewardSummary bob = new CustomerRewardSummary(
                "C002", "Bob Smith", Collections.emptyList(), 0L);

        when(rewardsService.getCustomerRewards(eq("C002"), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(bob);

        ResponseEntity<CustomerRewardSummary> response = rewardsController.getCustomerRewards("C002", START, END);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("C002", response.getBody().getCustomerId());
        assertEquals(0L, response.getBody().getTotalPoints());
        assertEquals(0, response.getBody().getMonthlyRewards().size());
    }

    @Test
    @DisplayName("GET /api/rewards/C003 → 200 with aggregated single month")
    void getCustomerRewards_SingleMonthMultipleTransactions_ReturnsAggregatedMonthlyEntry() {
        List<MonthlyReward> monthly = Collections.singletonList(
                new MonthlyReward("February 2024", 155L)
        );
        CustomerRewardSummary carol = new CustomerRewardSummary("C003", "Carol Davis", monthly, 155L);

        when(rewardsService.getCustomerRewards(eq("C003"), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(carol);

        ResponseEntity<CustomerRewardSummary> response = rewardsController.getCustomerRewards("C003",
                LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 29));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(155L, response.getBody().getTotalPoints());
        assertEquals(1, response.getBody().getMonthlyRewards().size());
        assertEquals("February 2024", response.getBody().getMonthlyRewards().get(0).getMonth());
        assertEquals(155L, response.getBody().getMonthlyRewards().get(0).getPoints());
    }

    @Test
    @DisplayName("GET /api/rewards/{customerId} → 400 when start date is after end date")
    void getCustomerRewards_StartAfterEnd_ReturnsBadRequest() {
        when(rewardsService.getCustomerRewards(eq("C001"), any(LocalDate.class), any(LocalDate.class)))
                .thenThrow(new InvalidDateRangeException("Start date must not be after end date"));

        assertThrows(InvalidDateRangeException.class,
                () -> rewardsController.getCustomerRewards("C001", END, START));
    }
}
