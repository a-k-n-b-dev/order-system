package dev.aknb.ordersystem.dtos.user;

import dev.aknb.ordersystem.models.Filter;
import dev.aknb.ordersystem.models.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFilterDto extends Filter {

    private UserStatus userStatus = UserStatus.PENDING;
}
