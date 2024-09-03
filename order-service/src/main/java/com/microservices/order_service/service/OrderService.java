package com.microservices.order_service.service;

import com.microservices.order_service.model.Order;
import com.microservices.order_service.model.OrderLineItems;
import com.microservices.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public void placeOrder(Order order) {
//        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = order.getOrderLineItems();

        order.getOrderLineItems().stream()
                .map(this::mapToOrder)
                .toList();
        order.setOrderLineItems(orderLineItems);
        orderRepository.save(order);

    }

    private OrderLineItems mapToOrder(OrderLineItems orderLineItems) {
//        OrderLineItems orderLineItems1 = new OrderLineItems();
        orderLineItems.setPrice(orderLineItems.getPrice());
        orderLineItems.setQuantity(orderLineItems.getQuantity());
        orderLineItems.setSkuCode(orderLineItems.getSkuCode());

        return orderLineItems;

    }


}
