package dev.aknb.ordersystem.services;

import dev.aknb.ordersystem.dtos.user.UserDto;
import dev.aknb.ordersystem.dtos.user.UserFilterDto;
import dev.aknb.ordersystem.mappers.UserMapper;
import dev.aknb.ordersystem.models.UserStatus;
import dev.aknb.ordersystem.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> getAll(UserFilterDto userFilter) {

        if (userFilter.getUserStatus().equals(UserStatus.PENDING)) {

            return userRepository.findAllByApproved(false)
                    .stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        } else if (userFilter.getUserStatus().equals(UserStatus.APPROVED)) {

            return userRepository.findAllByApproved(true)
                    .stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        }
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
