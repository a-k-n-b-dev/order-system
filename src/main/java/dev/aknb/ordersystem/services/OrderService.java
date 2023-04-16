package dev.aknb.ordersystem.services;

import dev.aknb.ordersystem.dtos.order.CreateOrderDto;
import dev.aknb.ordersystem.dtos.order.OrderDto;
import dev.aknb.ordersystem.dtos.order.OrderFilterDto;
import dev.aknb.ordersystem.entities.Order;
import dev.aknb.ordersystem.entities.Role;
import dev.aknb.ordersystem.entities.User;
import dev.aknb.ordersystem.mappers.OrderMapper;
import dev.aknb.ordersystem.models.MessageType;
import dev.aknb.ordersystem.models.OrderStatus;
import dev.aknb.ordersystem.models.RestException;
import dev.aknb.ordersystem.models.RoleEnum;
import dev.aknb.ordersystem.repositories.user.UserRepository;
import dev.aknb.ordersystem.repositories.order.CustomOrderRepository;
import dev.aknb.ordersystem.repositories.order.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderService {

    private final CustomOrderRepository customOrderRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    public OrderService(CustomOrderRepository customOrderRepository, OrderRepository orderRepository, UserRepository userRepository, OrderMapper orderMapper) {
        this.customOrderRepository = customOrderRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
    }

    public OrderDto getOne(Long id) {

        Order order = orderRepository.findById(id).orElseThrow(() ->
                RestException.restThrow(HttpStatus.NOT_FOUND, MessageType.ORDER_NOT_FOUND_BY_ID.name(), id.toString()));
        return orderMapper.toOrderDto(order);
    }


    @Transactional
    public OrderDto create(CreateOrderDto request) {

        Order order = orderMapper.toOrder(request);
        User customer = orderMapper.toCustomer(request);
        customer.setRole(new Role(RoleEnum.CUSTOMER));
        customer = userRepository.save(customer);
        order.setUserId(customer.getId());
        order.setStatus(OrderStatus.RECEIVED);
        order = orderRepository.save(order);
        order.setUser(customer);
        return orderMapper.toOrderDto(order);
    }

    public Page<OrderDto> ordersWithFilter(OrderFilterDto request) {

        return customOrderRepository.findAllByFilter(request)
                .map(orderMapper::toOrderDto);
    }

    public OrderDto update(Long orderId, CreateOrderDto orderDto) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                RestException.restThrow(HttpStatus.NOT_FOUND, MessageType.ORDER_NOT_FOUND_BY_ID.name(), orderId.toString()));

        orderMapper.update(order, orderDto);

        User customer = new User();
        if (orderDto.getUserId() != null) {

            customer = userRepository.findById(orderDto.getUserId()).orElseThrow(() ->
                    RestException.restThrow(HttpStatus.NOT_FOUND, MessageType.USER_NOT_FOUND.name()));
        } else {
            customer.setFullName(orderDto.getFullName());
            customer.setAddress(orderDto.getAddress());
            customer.setPhoneNumber(orderDto.getPhoneNumber());
            customer = userRepository.save(customer);
            order.setUser(customer);
        }
        order.setUserId(customer.getId());
        order = orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }
}
