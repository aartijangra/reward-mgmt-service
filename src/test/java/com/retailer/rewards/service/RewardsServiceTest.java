package com.retailer.rewards.service;

import com.retailer.rewards.data.CustomerDataStore;
import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.exception.InvalidDateRangeException;
import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.CustomerRewardSummary;
import com.retailer.rewards.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RewardsServiceTest {

    @Mock
    private CustomerDataStore dataProvider;

    @InjectMocks
    private RewardsServiceImpl rewardsService;

    private static final LocalDate START = LocalDate.of(2024, 1, 1);
    private static final LocalDate END   = LocalDate.of(2024, 3, 31);
    private static final Pageable  PAGE  = PageRequest.of(0, 10);

    private Customer alice;
    private Customer bob;


    @BeforeEach
    void setUp() {
        alice = new Customer("C001", "Alice Johnson");
        bob   = new Customer("C002", "Bob Smith");
    }


    @Test
    @DisplayName("getAllCustomerRewards: returns summaries for all customers")
    void getAllCustomerRewards_WithMultipleCustomers_ReturnsSummariesForAll() {
        var aliceTx = Arrays.asList(
                new Transaction("T001", "C001", 120.0, LocalDate.of(2024, 1, 15)),
                new Transaction("T002", "C001",  75.0, LocalDate.of(2024, 2, 10))
        );
        var bobTx = Collections.singletonList(
                new Transaction("T003", "C002", 200.0, LocalDate.of(2024, 1, 20))
        );

        when(dataProvider.getCustomersPage(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(alice, bob)));
        when(dataProvider.findTransactionsByCustomerIdAndDateRange(eq("C001"), any(), any()))
                .thenReturn(aliceTx);
        when(dataProvider.findTransactionsByCustomerIdAndDateRange(eq("C002"), any(), any()))
                .thenReturn(bobTx);

        Page<CustomerRewardSummary> result = rewardsService.getAllCustomerRewards(START, END, PAGE);

        assertEquals(2, result.getTotalElements());

        CustomerRewardSummary aliceSummary = result.getContent().stream()
                .filter(s -> "C001".equals(s.getCustomerId())).findFirst().orElseThrow();
        assertEquals(115L, aliceSummary.getTotalPoints());
        assertEquals(2, aliceSummary.getMonthlyRewards().size());

        CustomerRewardSummary bobSummary = result.getContent().stream()
                .filter(s -> "C002".equals(s.getCustomerId())).findFirst().orElseThrow();
        assertEquals(250L, bobSummary.getTotalPoints());
        assertEquals(1, bobSummary.getMonthlyRewards().size());
    }


    @Test
    @DisplayName("getAllCustomerRewards: customer with no transactions → 0 total points")
    void getAllCustomerRewards_WhenCustomerHasNoTransactions_ReturnsZeroPoints() {
        when(dataProvider.getCustomersPage(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(alice)));
        when(dataProvider.findTransactionsByCustomerIdAndDateRange(eq("C001"), any(), any()))
                .thenReturn(Collections.emptyList());

        Page<CustomerRewardSummary> result = rewardsService.getAllCustomerRewards(START, END, PAGE);

        assertEquals(1, result.getTotalElements());
        assertEquals(0L, result.getContent().get(0).getTotalPoints());
        assertTrue(result.getContent().get(0).getMonthlyRewards().isEmpty());
    }


    @Test
    @DisplayName("getAllCustomerRewards: monthly rewards sorted chronologically")
    void getAllCustomerRewards_MonthlyRewardsSortedChronologically() {
        var transactions = Arrays.asList(
                new Transaction("T001", "C001", 120.0, LocalDate.of(2024, 3, 1)),
                new Transaction("T002", "C001",  75.0, LocalDate.of(2024, 1, 1)),
                new Transaction("T003", "C001", 200.0, LocalDate.of(2024, 2, 1))
        );

        when(dataProvider.getCustomersPage(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(alice)));
        when(dataProvider.findTransactionsByCustomerIdAndDateRange(eq("C001"), any(), any()))
                .thenReturn(transactions);

        CustomerRewardSummary summary = rewardsService.getAllCustomerRewards(START, END, PAGE)
                .getContent().get(0);

        assertEquals("January 2024",  summary.getMonthlyRewards().get(0).getMonth());
        assertEquals("February 2024", summary.getMonthlyRewards().get(1).getMonth());
        assertEquals("March 2024",    summary.getMonthlyRewards().get(2).getMonth());
    }


    @Test
    @DisplayName("getAllCustomerRewards: multiple transactions same month aggregated")
    void getAllCustomerRewards_MultipleTransactionsSameMonth_AggregatesCorrectly() {
        var transactions = Arrays.asList(
                new Transaction("T001", "C001", 120.0, LocalDate.of(2024, 1, 10)),
                new Transaction("T002", "C001",  75.0, LocalDate.of(2024, 1, 20))
        );

        when(dataProvider.getCustomersPage(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(alice)));
        when(dataProvider.findTransactionsByCustomerIdAndDateRange(eq("C001"), any(), any()))
                .thenReturn(transactions);

        CustomerRewardSummary summary = rewardsService.getAllCustomerRewards(START, END, PAGE)
                .getContent().get(0);

        assertEquals(1, summary.getMonthlyRewards().size());
        assertEquals(115L, summary.getMonthlyRewards().get(0).getPoints());
        assertEquals(115L, summary.getTotalPoints());
    }


    @Test
    @DisplayName("getAllCustomerRewards: start date after end date → InvalidDateRangeException")
    void getAllCustomerRewards_StartAfterEnd_ThrowsInvalidDateRangeException() {
        assertThrows(InvalidDateRangeException.class,
                () -> rewardsService.getAllCustomerRewards(END, START, PAGE));
    }


    @Test
    @DisplayName("getCustomerRewards: valid customer ID → correct summary")
    void getCustomerRewards_ValidCustomerId_ReturnsCorrectSummary() {
        var transactions = Arrays.asList(
                new Transaction("T001", "C001", 120.0, LocalDate.of(2024, 1, 15)),
                new Transaction("T002", "C001",  45.0, LocalDate.of(2024, 1, 25))
        );

        when(dataProvider.findCustomerById("C001")).thenReturn(Optional.of(alice));
        when(dataProvider.findTransactionsByCustomerIdAndDateRange(eq("C001"), any(), any()))
                .thenReturn(transactions);

        CustomerRewardSummary summary = rewardsService.getCustomerRewards("C001", START, END);

        assertEquals("C001", summary.getCustomerId());
        assertEquals("Alice Johnson", summary.getCustomerName());
        assertEquals(90L, summary.getTotalPoints());
        assertEquals(1, summary.getMonthlyRewards().size());
        assertEquals("January 2024", summary.getMonthlyRewards().get(0).getMonth());
    }


    @Test
    @DisplayName("getCustomerRewards: unknown customer ID → CustomerNotFoundException")
    void getCustomerRewards_UnknownCustomerId_ThrowsCustomerNotFoundException() {
        when(dataProvider.findCustomerById("C999")).thenReturn(Optional.empty());

        CustomerNotFoundException ex = assertThrows(
                CustomerNotFoundException.class,
                () -> rewardsService.getCustomerRewards("C999", START, END));
        assertTrue(ex.getMessage().contains("C999"));
    }


    @Test
    @DisplayName("getCustomerRewards: $0 transaction → 0 pts, no exception")
    void getCustomerRewards_ZeroAmountTransaction_NoPtsAndNoException() {
        when(dataProvider.findCustomerById("C001")).thenReturn(Optional.of(alice));
        when(dataProvider.findTransactionsByCustomerIdAndDateRange(eq("C001"), any(), any()))
                .thenReturn(Collections.singletonList(
                        new Transaction("T001", "C001", 0.0, LocalDate.of(2024, 1, 10))));

        assertEquals(0L, rewardsService.getCustomerRewards("C001", START, END).getTotalPoints());
    }


    @Test
    @DisplayName("getCustomerRewards: 3-month window produces 3 monthly entries")
    void getCustomerRewards_ThreeMonthWindow_ProducesThreeMonthlyEntries() {
        var transactions = Arrays.asList(
                new Transaction("T001", "C001", 120.0, LocalDate.of(2024, 1, 15)),
                new Transaction("T002", "C001", 200.0, LocalDate.of(2024, 2, 10)),
                new Transaction("T003", "C001", 160.0, LocalDate.of(2024, 3,  5))
        );

        when(dataProvider.findCustomerById("C001")).thenReturn(Optional.of(alice));
        when(dataProvider.findTransactionsByCustomerIdAndDateRange(eq("C001"), any(), any()))
                .thenReturn(transactions);

        CustomerRewardSummary summary = rewardsService.getCustomerRewards("C001", START, END);

        assertEquals(3, summary.getMonthlyRewards().size());
        assertEquals(510L, summary.getTotalPoints());
    }


    @Test
    @DisplayName("getCustomerRewards: all transactions same month → 1 monthly entry")
    void getCustomerRewards_AllTransactionsSameMonth_OneMonthlyEntry() {
        var transactions = Arrays.asList(
                new Transaction("T001", "C002",  95.0, LocalDate.of(2024, 2, 5)),
                new Transaction("T002", "C002", 130.0, LocalDate.of(2024, 2, 18))
        );

        when(dataProvider.findCustomerById("C002")).thenReturn(Optional.of(bob));
        when(dataProvider.findTransactionsByCustomerIdAndDateRange(eq("C002"), any(), any()))
                .thenReturn(transactions);

        CustomerRewardSummary summary = rewardsService.getCustomerRewards("C002", START, END);

        assertEquals(1, summary.getMonthlyRewards().size());
        assertEquals(155L, summary.getMonthlyRewards().get(0).getPoints());
        assertEquals(155L, summary.getTotalPoints());
    }


    @Test
    @DisplayName("getCustomerRewards: start date after end date → InvalidDateRangeException")
    void getCustomerRewards_StartAfterEnd_ThrowsInvalidDateRangeException() {
        assertThrows(InvalidDateRangeException.class,
                () -> rewardsService.getCustomerRewards("C001", END, START));
    }
}
