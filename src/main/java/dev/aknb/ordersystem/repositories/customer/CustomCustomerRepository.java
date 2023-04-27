package dev.aknb.ordersystem.repositories.customer;

import org.springframework.data.domain.Page;

import dev.aknb.ordersystem.entities.Customer;
import dev.aknb.ordersystem.models.Filter;

public interface CustomCustomerRepository {
    
    Page<Customer> findAllByFilter(Filter filter);
}
