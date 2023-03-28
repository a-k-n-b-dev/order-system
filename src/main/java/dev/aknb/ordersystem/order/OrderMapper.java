package dev.aknb.ordersystem.order;

import dev.aknb.ordersystem.order.dto.CreateOrderDto;
import dev.aknb.ordersystem.order.dto.OrderDto;
import dev.aknb.ordersystem.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "address", source = "user.address")
    OrderDto toOrderDto(Order source);
    Order toOrder(CreateOrderDto source);
    User toCustomer(CreateOrderDto source);
}
