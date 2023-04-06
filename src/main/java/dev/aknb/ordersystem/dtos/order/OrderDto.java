package dev.aknb.ordersystem.dtos.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto extends CreateOrderDto{

    private Long id;
}
