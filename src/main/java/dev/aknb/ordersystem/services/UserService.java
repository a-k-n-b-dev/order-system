package dev.aknb.ordersystem.services;

import dev.aknb.ordersystem.dtos.user.UpdateUserStatusDto;
import dev.aknb.ordersystem.dtos.user.UserDto;
import dev.aknb.ordersystem.dtos.user.UserFilterDto;
import dev.aknb.ordersystem.entities.User;
import dev.aknb.ordersystem.mappers.UserMapper;
import dev.aknb.ordersystem.models.MessageType;
import dev.aknb.ordersystem.models.RestException;
import dev.aknb.ordersystem.repositories.user.CustomUserRepository;
import dev.aknb.ordersystem.repositories.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    public UserDto updateStatus(Long userId, UpdateUserStatusDto statusDto) {

        User user = userRepository.findById(userId).orElseThrow( () -> 
            RestException.restThrow(HttpStatus.NOT_FOUND, MessageType.USER_NOT_FOUND.name()));
        user.setStatus(statusDto.getStatus());
        user = userRepository.save(user);
        return userMapper.toUserDto(user);
    }
}
