package dev.aknb.ordersystem.dtos.order;

import dev.aknb.ordersystem.models.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderDto extends CreateOrderDto{

    private OrderStatus status;
}
