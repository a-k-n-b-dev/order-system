package dev.aknb.ordersystem.controllers;

import dev.aknb.ordersystem.controllers.constants.ApiConstants;
import dev.aknb.ordersystem.config.ProjectConfig;
import dev.aknb.ordersystem.models.Response;
import dev.aknb.ordersystem.dtos.UserDto;
import dev.aknb.ordersystem.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(ApiConstants.API_USER)
@Tag(name = "User APIs")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @SecurityRequirement(name = ProjectConfig.NAME, scopes = {"ADMIN", "OWNER", "DEV"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'DEV')")
    @GetMapping("/list")
    public ResponseEntity<Response<List<UserDto>>> findAll() {

        log.info("Rest request to get list of users");
        return ResponseEntity.ok(Response.ok(userService.getAll()));
    }
}
