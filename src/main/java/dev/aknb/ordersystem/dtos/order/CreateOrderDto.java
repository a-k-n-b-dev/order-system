package dev.aknb.ordersystem.dtos.order;

import dev.aknb.ordersystem.utils.validator.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    private String materials;

    // Customer info
    private Long customerId;

    @NotBlank(message = "FULL_NAME_NOT_BLANK")
    @Size(min = 4, max = 100, message = "FULL_NAME_MIN4_MAX100")
    private String fullName;
    @NotBlank(message = "PHONE_NUMBER_NOT_BLANK")
    @Size(min = 13, max = 15, message = "PHONE_NUMBER_MIN13_MAX15")
    @ValidPhoneNumber(message = "INVALID_PHONE_NUMBER")
    private String phoneNumber;
    @NotBlank(message = "ADDRESS_NOT_BLANK")
    private String address;
}
