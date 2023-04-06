package dev.aknb.ordersystem.mappers;

import dev.aknb.ordersystem.entities.Order;
import dev.aknb.ordersystem.dtos.order.CreateOrderDto;
import dev.aknb.ordersystem.dtos.order.OrderDto;
import dev.aknb.ordersystem.entities.User;
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
