package com.salesagents.dataaccess.repository;

import com.salesagents.domain.models.Order;

import java.util.Collection;

public interface OrderRepository {
    Collection<Order> getAll();
    void save(Order order);
    Collection<Order> findByAgent(String username);
}
