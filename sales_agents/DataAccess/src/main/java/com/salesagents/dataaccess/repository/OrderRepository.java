package com.salesagents.dataaccess.repository;

import com.salesagents.domain.models.Order;

import java.util.List;

public interface OrderRepository {
    List<Order> getAll();
    void save(Order order);
    List<Order> findByAgent(String username);
}
