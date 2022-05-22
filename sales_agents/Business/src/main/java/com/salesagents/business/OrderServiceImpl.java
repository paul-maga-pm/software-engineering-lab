package com.salesagents.business;

import com.salesagents.dataaccess.repository.OrderRepository;
import com.salesagents.domain.models.Order;

import java.util.Collection;

public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void placeOrder(Order order) {
        this.orderRepository.save(order);
    }

    @Override
    public Collection<Order> getAllOrders() {
        return this.orderRepository.getAll();
    }

    @Override
    public Collection<Order> findByAgent(String username) {
        return this.orderRepository.findByAgent(username);
    }

}
