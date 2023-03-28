package dev.aknb.ordersystem.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateOrderDto {

    private String name;
    private LocalDate acceptedDate;
    private LocalDate deliveredDate;
    private String size;
    private String description;
    private String agreement;
    private String price;

    // Customer info

    private String fullName;
    private String phoneNumber;
    private String address;
}
