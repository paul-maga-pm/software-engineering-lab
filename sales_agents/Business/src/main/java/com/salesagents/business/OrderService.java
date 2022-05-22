package com.salesagents.business;

import com.salesagents.business.utils.ProductObservable;
import com.salesagents.domain.models.Order;

import java.util.Collection;

public abstract class OrderService extends ProductObservable {
    public abstract void placeOrder(Order order);
    public abstract Collection<Order> getAllOrders();
    public abstract Collection<Order> findByAgent(String username);
}
