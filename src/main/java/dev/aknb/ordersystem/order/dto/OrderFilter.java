package dev.aknb.ordersystem.order.dto;

import dev.aknb.ordersystem.base.Filter;
import dev.aknb.ordersystem.order.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFilter extends Filter {

    private OrderStatus status = OrderStatus.DELIVERED;
}
