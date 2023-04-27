package dev.aknb.ordersystem.services;

import dev.aknb.ordersystem.dtos.user.UpdateUserStatusDto;
import dev.aknb.ordersystem.dtos.user.UserDto;
import dev.aknb.ordersystem.dtos.user.UserFilterDto;
import dev.aknb.ordersystem.entities.User;
import dev.aknb.ordersystem.mappers.UserMapper;
import dev.aknb.ordersystem.models.Filter;
import dev.aknb.ordersystem.models.MessageType;
import dev.aknb.ordersystem.models.RestException;
import dev.aknb.ordersystem.repositories.customer.CustomCustomerRepository;
import dev.aknb.ordersystem.repositories.customer.CustomerRepository;
import dev.aknb.ordersystem.repositories.user.CustomUserRepository;
import dev.aknb.ordersystem.repositories.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import dev.aknb.ordersystem.dtos.customer.CustomerDto;
import dev.aknb.ordersystem.dtos.order.CreateOrderDto;
import dev.aknb.ordersystem.entities.Customer;
import dev.aknb.ordersystem.mappers.CustomerMapper;

@Service
public class UserService {

    private final CustomCustomerRepository customCustomerRepository;
    private final CustomUserRepository customUserRepository;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(CustomCustomerRepository customCustomerRepository, CustomUserRepository customUserRepository, CustomerRepository customerRepository, CustomerMapper customerMapper, UserRepository userRepository, UserMapper userMapper) {
        this.customCustomerRepository = customCustomerRepository;
        this.customUserRepository = customUserRepository;
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Page<UserDto> getAllUsers(UserFilterDto userFilter) {

        return customUserRepository.findUserByFilter(userFilter)
                .map(userMapper::toUserDto);
    }

    public UserDto updateStatus(Long userId, UpdateUserStatusDto statusDto) {

        User user = userRepository.findById(userId).orElseThrow( () -> 
            RestException.restThrow(HttpStatus.NOT_FOUND, MessageType.USER_NOT_FOUND.name()));
        user.setStatus(statusDto.getStatus());
        user = userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    public Page<CustomerDto> getAllCustomers(Filter filter) {

        return customCustomerRepository.findAllByFilter(filter)
                .map(customerMapper::toCustomerDto);
    }

    public Customer getCustomerIfExistOrCreate(CreateOrderDto createOrderDto) {

        Customer customer = getCustomerIfExistOrNew(createOrderDto.getCustomerId());

        if ( customer.getId() != null ) {
            return customer;
        }

        customerMapper.update(customer, createOrderDto);
        return customerRepository.save(customer);
    }

    public Customer getCustomerIfExistOrNew(Long customerId) {
        if (customerId == null) {

            return new Customer();
        } else {
            return customerRepository.findById(customerId).orElse(new Customer());
        }
    }
}
