package com.retailer.rewards.repository;

import com.retailer.rewards.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** JPA repository for Customer entity operations. */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
}
