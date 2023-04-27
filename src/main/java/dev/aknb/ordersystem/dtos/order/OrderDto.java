package dev.aknb.ordersystem.dtos.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDto extends UpdateOrderDto{

    private Long id;
    private Long UserId;
    private List<String> imagesToken;
}
