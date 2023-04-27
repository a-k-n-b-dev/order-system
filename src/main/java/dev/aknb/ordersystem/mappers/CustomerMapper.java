package dev.aknb.ordersystem.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import dev.aknb.ordersystem.dtos.customer.CustomerDto;
import dev.aknb.ordersystem.dtos.order.CreateOrderDto;
import dev.aknb.ordersystem.entities.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", source = "customerId")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void update(@MappingTarget Customer customer, CreateOrderDto createOrderDto);
    
    CustomerDto toCustomerDto(Customer source);
}
