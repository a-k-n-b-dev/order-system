package dev.aknb.ordersystem.services;

import dev.aknb.ordersystem.dtos.user.UserDto;
import dev.aknb.ordersystem.dtos.user.UserFilterDto;
import dev.aknb.ordersystem.mappers.UserMapper;
import dev.aknb.ordersystem.repositories.user.CustomUserRepository;
import dev.aknb.ordersystem.repositories.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final CustomUserRepository customUserRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(CustomUserRepository customUserRepository, UserRepository userRepository, UserMapper userMapper) {
        this.customUserRepository = customUserRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Page<UserDto> getAll(UserFilterDto userFilter) {

        return customUserRepository.findUserByFilter(userFilter)
                .map(userMapper::toUserDto);
    }
}
