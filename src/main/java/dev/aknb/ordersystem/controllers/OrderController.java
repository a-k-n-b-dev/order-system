package dev.aknb.ordersystem.controllers;

import dev.aknb.ordersystem.config.ProjectConfig;
import dev.aknb.ordersystem.controllers.constants.ApiConstants;
import dev.aknb.ordersystem.dtos.order.CreateOrderDto;
import dev.aknb.ordersystem.dtos.order.OrderDto;
import dev.aknb.ordersystem.dtos.order.OrderFilterDto;
import dev.aknb.ordersystem.dtos.order.OrderUpdateStatusDto;
import dev.aknb.ordersystem.dtos.order.UpdateOrderDto;
import dev.aknb.ordersystem.models.Response;
import dev.aknb.ordersystem.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public ResponseEntity<Response<OrderDto>> createOrder(@Valid @RequestBody CreateOrderDto request) {

        log.info("Rest request to create order customer full name: {}", request.getFullName());
        return ResponseEntity.ok(
                Response.ok(orderService.create(request)));
    }

    @Operation(summary = "Find all orders as a page default", description = "Order statuses: { RECEIVED, STARTED, PAINTING, FINISHED, DELIVERED }")
    @GetMapping("/list")
    public ResponseEntity<Response<Page<OrderDto>>> list(OrderFilterDto request) {

        log.info("Rest request to list with filter: {}", request);
        return ResponseEntity.ok(Response.ok(orderService.ordersWithFilter(request)));
    }

    @SecurityRequirement(name = ProjectConfig.NAME, scopes = {"ADMIN", "OWNER", "DEV"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'DEV')")
    @Operation(summary = "Update order values.")
    @PutMapping("/update/{id}")
    public ResponseEntity<Response<OrderDto>> update(@PathVariable("id") Long orderId,
                                              @Valid @RequestBody UpdateOrderDto orderDto){

        log.info("Rest request to update order id: {}", orderId);
        return ResponseEntity.ok(Response.ok(orderService.update(orderId, orderDto)));
    }

    @SecurityRequirement(name = ProjectConfig.NAME, scopes = {"ADMIN", "OWNER", "DEV"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'DEV')")
    @Operation(summary = "Delete order by id")
    @PatchMapping("/update/status/{id}")
    public ResponseEntity<Response<OrderDto>> updateStatus(@PathVariable("id") Long orderId, @RequestBody OrderUpdateStatusDto statusDto) {
        
        log.info("Rest request to update status: {}", statusDto.getStatus().name());
        return ResponseEntity.ok(Response.ok(orderService.updateStatus(orderId, statusDto)));
    }

    @SecurityRequirement(name = ProjectConfig.NAME, scopes = {"ADMIN", "OWNER", "DEV"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'DEV')")
    @Operation(summary = "Delete order by id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response<String>> delete(@PathVariable("id") Long orderId) {

        log.info("Rest request  to delete order by id: {}", orderId);
        orderService.delete(orderId);
        return ResponseEntity.ok(Response.ok("Ok order deleted!"));
    }
}
