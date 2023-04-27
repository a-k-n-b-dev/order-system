package dev.aknb.ordersystem.controllers;

import dev.aknb.ordersystem.config.ProjectConfig;
import dev.aknb.ordersystem.controllers.constants.ApiConstants;
import dev.aknb.ordersystem.dtos.customer.CustomerDto;
import dev.aknb.ordersystem.dtos.user.UpdateUserStatusDto;
import dev.aknb.ordersystem.dtos.user.UserDto;
import dev.aknb.ordersystem.dtos.user.UserFilterDto;
import dev.aknb.ordersystem.models.Filter;
import dev.aknb.ordersystem.models.Response;
import dev.aknb.ordersystem.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(ApiConstants.BASE_URL)
@Tag(name = "User/Customer APIs")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Find all users", description = "User statuses: { PENDING, VERIFIED, APPROVED, ALL }")
    @SecurityRequirement(name = ProjectConfig.NAME, scopes = {"ADMIN", "OWNER", "DEV"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'DEV')")
    @GetMapping("/user/list")
    public ResponseEntity<Response<Page<UserDto>>> findAll(UserFilterDto userFilter) {

        log.info("Rest request to get list of users by filter: {}", userFilter);
        return ResponseEntity.ok(Response.ok(userService.getAllUsers(userFilter)));
    }

    @Operation(summary = "Update user status", description = "User statuses: { PENDING, VERIFIED, APPROVED, ALL }")
    @SecurityRequirement(name = ProjectConfig.NAME, scopes = {"ADMIN", "OWNER", "DEV"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'DEV')")
    @GetMapping("/user/update/{id}")
    public ResponseEntity<Response<UserDto>> updateStatus(@PathVariable("id") Long userId, @RequestBody UpdateUserStatusDto statusDto) {

        log.info("Rest request to update user status: {}", statusDto);
        return ResponseEntity.ok(Response.ok(userService.updateStatus(userId, statusDto)));
    }

    @Operation(summary = "Find all customers")
    @SecurityRequirement(name = ProjectConfig.NAME, scopes = {"ADMIN", "OWNER", "DEV"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'DEV')")
    @GetMapping("/customer/list")
    public ResponseEntity<Response<Page<CustomerDto>>> findAll(Filter filter) {

        log.info("Rest request to get list of customers by filter: {}", filter);
        return ResponseEntity.ok(
            Response.ok(
                userService.getAllCustomers(filter)));
    }
}
