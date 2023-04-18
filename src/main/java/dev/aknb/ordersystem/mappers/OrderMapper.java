package dev.aknb.ordersystem.mappers;

import dev.aknb.ordersystem.dtos.order.CreateOrderDto;
import dev.aknb.ordersystem.dtos.order.OrderDto;
import dev.aknb.ordersystem.entities.Image;
import dev.aknb.ordersystem.entities.Order;
import dev.aknb.ordersystem.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "address", source = "user.address")
    @Mapping(target = "imagesToken", expression = "java(getImagesToken(source.getImages()))")
    OrderDto toOrderDto(Order source);
    Order toOrder(CreateOrderDto source);
    User toCustomer(CreateOrderDto source);

    default List<String> getImagesToken(List<Image> images) {
        return images.stream()
                .map(Image::getToken)
                .collect(Collectors.toList());
    }

    void update(@MappingTarget Order target, CreateOrderDto source);
}
