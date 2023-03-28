package dev.aknb.ordersystem.order.repository;

import dev.aknb.ordersystem.order.Order;
import dev.aknb.ordersystem.order.dto.OrderFilter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomOrderRepository {

    Page<Order> findAllByFilter(OrderFilter request);
}
