package com.microservices.order_service.service;

import com.microservices.order_service.DTO.InventoryResponse;
import com.microservices.order_service.model.Order;
import com.microservices.order_service.model.OrderLineItems;
import com.microservices.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public String placeOrder(Order order) {
//        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = order.getOrderLineItems();

        order.getOrderLineItems().stream()
                .map(this::mapToOrder)
                .toList();
        order.setOrderLineItems(orderLineItems);

        List<String> skuCodes= order.getOrderLineItems()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

//        Should call inventory service first and place order if product is in stock
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/inventory/get", uriBuilder ->
                        uriBuilder.queryParam("skuCode", skuCodes)
                                .build())
                        .retrieve()
                                .bodyToMono(InventoryResponse[].class)
                                        .block();
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);
        if (allProductsInStock) {
            orderRepository.save(order);
            return "Order placed successfully";
        } else {
            throw new IllegalArgumentException("Product is not in stock, try again later.");
        }

    }

    private OrderLineItems mapToOrder(OrderLineItems orderLineItems) {
//        OrderLineItems orderLineItems1 = new OrderLineItems();
        orderLineItems.setPrice(orderLineItems.getPrice());
        orderLineItems.setQuantity(orderLineItems.getQuantity());
        orderLineItems.setSkuCode(orderLineItems.getSkuCode());

        return orderLineItems;

    }


}
