package com.retailer.rewards;

import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RewardsManagementIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        customerRepository.deleteAll();
        customerRepository.save(new Customer("C001", "Alice Johnson"));
        customerRepository.save(new Customer("C002", "Bob Smith"));
        transactionRepository.save(new Transaction("T001", "C001", 120.0, LocalDate.of(2024, 1, 15)));
        transactionRepository.save(new Transaction("T002", "C001", 75.0,  LocalDate.of(2024, 2, 10)));
        transactionRepository.save(new Transaction("T003", "C002", 200.0, LocalDate.of(2024, 1, 20)));
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /api/rewards → 200 OK with paginated content array")
    void getAllRewards_ReturnsOkWithPaginatedContent() throws Exception {
        mockMvc.perform(get("/api/rewards")
                        .param("startDate", "2024-01-01")
                        .param("endDate",   "2024-03-31")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("GET /api/rewards → 400 when start date is after end date")
    void getAllRewards_InvalidDateRange_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/rewards")
                        .param("startDate", "2024-03-31")
                        .param("endDate",   "2024-01-01")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/rewards/{customerId} → 200 OK with correct summary")
    void getCustomerRewards_ValidCustomer_ReturnsCorrectSummary() throws Exception {
        mockMvc.perform(get("/api/rewards/C001")
                        .param("startDate", "2024-01-01")
                        .param("endDate",   "2024-03-31")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId",   is("C001")))
                .andExpect(jsonPath("$.customerName", is("Alice Johnson")));
    }

    @Test
    @DisplayName("GET /api/rewards/{customerId} → 404 for unknown customer")
    void getCustomerRewards_UnknownCustomer_Returns404() throws Exception {
        mockMvc.perform(get("/api/rewards/C999")
                        .param("startDate", "2024-01-01")
                        .param("endDate",   "2024-03-31")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/rewards/{customerId} → 400 when start date is after end date")
    void getCustomerRewards_InvalidDateRange_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/rewards/C001")
                        .param("startDate", "2024-03-31")
                        .param("endDate",   "2024-01-01")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/rewards/{customerId} → filters transactions by date range")
    void getCustomerRewards_FiltersTransactionsByDateRange() throws Exception {
        mockMvc.perform(get("/api/rewards/C001")
                        .param("startDate", "2024-01-01")
                        .param("endDate",   "2024-01-31")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyRewards.length()", is(1)));
    }
}
