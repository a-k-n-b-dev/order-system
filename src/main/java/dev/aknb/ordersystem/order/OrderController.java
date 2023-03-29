package dev.aknb.ordersystem.order;

import dev.aknb.ordersystem.order.dto.CreateOrderDto;
import dev.aknb.ordersystem.order.dto.OrderDto;
import dev.aknb.ordersystem.order.dto.OrderFilter;
import dev.aknb.ordersystem.project.ApiConstants;
import dev.aknb.ordersystem.project.ProjectConfig;
import dev.aknb.ordersystem.response.Response;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(ApiConstants.API_ORDER)
@Tag(name = "Order APIs")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OrderDto>> getOne(@PathVariable("id") Long id) {

        log.info("Rest request to get order id: {}", id);
        return ResponseEntity.ok(
                Response.ok(orderService.getOne(id)));
    }

    @SecurityRequirement(name = ProjectConfig.NAME, scopes = {"ADMIN", "OWNER", "DEV"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'DEV')")
    @PostMapping
    public ResponseEntity<Response<OrderDto>> createOrder(@RequestBody CreateOrderDto request) {

        log.info("Rest request to create order customer full name: {}", request.getFullName());
        return ResponseEntity.ok(
                Response.ok(orderService.create(request)));
    }

    @GetMapping("/list")
    public ResponseEntity<Response<Page<OrderDto>>> list(OrderFilter request) {

        log.info("Rest request to list with filter: {}", request);
        return ResponseEntity.ok(Response.ok(orderService.ordersWithFilter(request)));
    }
}
