package dev.aknb.ordersystem.repositories.order;

import dev.aknb.ordersystem.entities.Order;
import dev.aknb.ordersystem.dtos.order.OrderFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomOrderRepository {

    Page<Order> findAllByFilter(OrderFilterDto request);
}
