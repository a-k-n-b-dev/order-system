package dev.aknb.ordersystem.dtos.order;

import dev.aknb.ordersystem.models.Filter;
import dev.aknb.ordersystem.models.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFilterDto extends Filter {

    private OrderStatus status = OrderStatus.DELIVERED;
}
