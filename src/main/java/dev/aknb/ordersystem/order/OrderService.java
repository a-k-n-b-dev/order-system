package dev.aknb.ordersystem.order;

import dev.aknb.ordersystem.exception.RestException;
import dev.aknb.ordersystem.message.MessageType;
import dev.aknb.ordersystem.order.dto.CreateOrderDto;
import dev.aknb.ordersystem.order.dto.OrderDto;
import dev.aknb.ordersystem.role.Role;
import dev.aknb.ordersystem.role.RoleEnum;
import dev.aknb.ordersystem.user.User;
import dev.aknb.ordersystem.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, OrderMapper orderMapper) {
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
        order = orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }
}
