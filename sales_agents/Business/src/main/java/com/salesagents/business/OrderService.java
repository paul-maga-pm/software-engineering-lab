package com.salesagents.business;

import com.salesagents.domain.models.Order;

import java.util.Collection;

public interface OrderService {
    void placeOrder(Order order);
    Collection<Order> getAllOrders();
    Collection<Order> findByAgent(String username);
}
