package dev.aknb.ordersystem.order;

import dev.aknb.ordersystem.order.dto.CreateOrderDto;
import dev.aknb.ordersystem.order.dto.OrderDto;
import dev.aknb.ordersystem.project.ApiConstants;
import dev.aknb.ordersystem.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(ApiConstants.API_ORDER)
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/get-one")
    public ResponseEntity<Response<OrderDto>> getOne(@RequestParam Long id) {

        log.info("Rest request to get order id: {}", id);
        return ResponseEntity.ok(
                Response.ok(orderService.getOne(id)));
    }

    @PostMapping("/create")
    public ResponseEntity<Response<OrderDto>> createOrder(@RequestBody CreateOrderDto request) {

        log.info("Rest request to create order customer full name: {}", request.getFullName());
        return ResponseEntity.ok(
                Response.ok(orderService.create(request)));
    }
}
