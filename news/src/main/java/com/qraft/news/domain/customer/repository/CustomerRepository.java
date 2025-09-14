package com.qraft.news.domain.customer.repository;

import com.qraft.news.domain.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByToken(String token);
    boolean existsByToken(String token);
}
