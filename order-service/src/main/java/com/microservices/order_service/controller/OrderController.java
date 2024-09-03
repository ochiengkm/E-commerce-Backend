package com.microservices.order_service.controller;

import com.microservices.order_service.model.Order;
import com.microservices.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/place")
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody Order order) {
        orderService.placeOrder(order);
        return "Order Placed Successfully";
    }
}
