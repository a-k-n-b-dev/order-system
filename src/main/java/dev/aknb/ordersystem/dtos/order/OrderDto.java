package dev.aknb.ordersystem.dtos.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDto extends CreateOrderDto{

    private Long id;

    private List<String> imagesToken;
}
