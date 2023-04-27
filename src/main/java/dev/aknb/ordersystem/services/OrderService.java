package dev.aknb.ordersystem.services;

import dev.aknb.ordersystem.dtos.order.CreateOrderDto;
import dev.aknb.ordersystem.dtos.order.OrderDto;
import dev.aknb.ordersystem.dtos.order.OrderFilterDto;
import dev.aknb.ordersystem.dtos.order.OrderUpdateStatusDto;
import dev.aknb.ordersystem.dtos.order.UpdateOrderDto;
import dev.aknb.ordersystem.entities.Customer;
import dev.aknb.ordersystem.entities.Order;
import dev.aknb.ordersystem.mappers.OrderMapper;
import dev.aknb.ordersystem.models.MessageType;
import dev.aknb.ordersystem.models.OrderStatus;
import dev.aknb.ordersystem.models.RestException;
import dev.aknb.ordersystem.repositories.order.CustomOrderRepository;
import dev.aknb.ordersystem.repositories.order.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final CustomOrderRepository customOrderRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserService userService;

    public OrderService(CustomOrderRepository customOrderRepository, OrderRepository orderRepository, OrderMapper orderMapper, UserService userService) {
        this.customOrderRepository = customOrderRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userService = userService;
    }

    public OrderDto getOne(Long id) {

        Order order = orderRepository.findById(id).orElseThrow(() ->
                RestException.restThrow(HttpStatus.NOT_FOUND, MessageType.ORDER_NOT_FOUND_BY_ID.name(), id.toString()));
        return orderMapper.toOrderDto(order);
    }


    @Transactional
    public OrderDto create(Long userId, CreateOrderDto createOrderDto) {

        Order order = orderMapper.toOrder(createOrderDto);
        Customer customer = userService.getCustomerIfExistOrCreate(createOrderDto);
        order.setUserId(userId);
        order.setCustomerId(customer.getId());
        order.setStatus(OrderStatus.RECEIVED);
        order = orderRepository.save(order);
        return orderMapper.toOrderDto(
            getIfExistOrThrows(order.getId()));
    }

    public Page<OrderDto> ordersWithFilter(OrderFilterDto request) {

        return customOrderRepository.findAllByFilter(request)
                .map(orderMapper::toOrderDto);
    }

    public OrderDto update(Long orderId, UpdateOrderDto orderDto) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                RestException.restThrow(HttpStatus.NOT_FOUND, MessageType.ORDER_NOT_FOUND_BY_ID.name(), orderId.toString()));

        orderMapper.update(order, orderDto);
        order = orderRepository.save(order);
        return orderMapper.toOrderDto(
                getIfExistOrThrows(orderId));
    }

    private Order getIfExistOrThrows(Long orderId) {

        return orderRepository.findById(orderId).orElseThrow( () -> 
            RestException.restThrow( HttpStatus.NOT_FOUND,  MessageType.ORDER_NOT_FOUND_BY_ID.name(), orderId.toString()));
    }

    public void delete(Long orderId) {
        
        Order order = orderRepository.findById(orderId).orElseThrow( () -> 
            RestException.restThrow(HttpStatus.NOT_FOUND, MessageType.ORDER_NOT_FOUND_BY_ID.name(), orderId.toString()));
        orderRepository.delete(order);
    }

    public OrderDto updateStatus(Long orderId, OrderUpdateStatusDto statusDto) {

        Order order = orderRepository.findById(orderId).orElseThrow( () -> 
            RestException.restThrow(HttpStatus.NOT_FOUND, MessageType.ORDER_NOT_FOUND_BY_ID.name(), orderId.toString()));
        order.setStatus(statusDto.getStatus());
        order = orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }
}
