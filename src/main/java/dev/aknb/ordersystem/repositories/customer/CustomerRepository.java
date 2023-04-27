package dev.aknb.ordersystem.repositories.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.aknb.ordersystem.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
}
