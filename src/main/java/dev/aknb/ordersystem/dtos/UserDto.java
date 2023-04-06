package dev.aknb.ordersystem.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private Boolean approved;
}
