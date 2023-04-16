package dev.aknb.ordersystem.repositories.user;

import dev.aknb.ordersystem.dtos.user.UserFilterDto;
import dev.aknb.ordersystem.entities.User;
import org.springframework.data.domain.Page;

public interface CustomUserRepository {

    Page<User> findUserByFilter(UserFilterDto userFilterDto);
}
