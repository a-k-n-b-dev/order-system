package dev.aknb.ordersystem.dtos.order;


import dev.aknb.ordersystem.models.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderUpdateStatusDto {
    
    @NotNull(message = "STATUS_NOT_NULL")
    private OrderStatus status;
}
