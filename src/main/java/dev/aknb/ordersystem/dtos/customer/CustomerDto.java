package dev.aknb.ordersystem.dtos.customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto {
    
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String address;
}
