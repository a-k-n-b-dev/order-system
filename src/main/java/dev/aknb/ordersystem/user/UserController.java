package dev.aknb.ordersystem.user;

import dev.aknb.ordersystem.project.ApiConstants;
import dev.aknb.ordersystem.project.ProjectConfig;
import dev.aknb.ordersystem.response.Response;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
