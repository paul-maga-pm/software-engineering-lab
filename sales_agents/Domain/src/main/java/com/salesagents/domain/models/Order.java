package com.salesagents.domain.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

@Entity
@Table (name = "orders")
public class Order implements Serializable {

    @Id
    @Column(name = "order_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderNumber;

    @Column (name = "client_name")
    private String clientName;

    @Column(name = "placing_date")
    private LocalDateTime placingDate;

    @OneToMany (cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "order_no")
    private Set<OrderDetail> orderDetailSet;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "agent_username", referencedColumnName = "username")
    private Agent agent;

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Order() {
        orderDetailSet = new HashSet<>();
    }

    public Order(String clientName, LocalDateTime placingDate) {
        this.clientName = clientName;
        this.placingDate = placingDate;
        orderDetailSet = new HashSet<>();
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        if (!this.orderDetailSet.add(orderDetail)) {
            Predicate<OrderDetail> pred = (o) -> o.equals(orderDetail);
            int oldQuantity = this.orderDetailSet.stream().filter(pred).findFirst().get().getQuantityInOrder();
            orderDetailSet.remove(orderDetail);
            int newQuantity = oldQuantity + orderDetail.getQuantityInOrder();
            orderDetail.setQuantityInOrder(newQuantity);
            this.orderDetailSet.add(orderDetail);
        }
        int oldQuantityInStock = orderDetail.getProduct().getQuantityInStock();
        int newQuantityInStock = oldQuantityInStock - orderDetail.getQuantityInOrder();
        orderDetail.getProduct().setQuantityInStock(newQuantityInStock);
    }

    public Set<OrderDetail> getOrderDetailSet() {
        return this.orderDetailSet;
    }

    public String getClientName() {
        return clientName;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public LocalDateTime getPlacingDate() {
        return placingDate;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setPlacingDate(LocalDateTime placingDate) {
        this.placingDate = placingDate;
    }

    public void setOrderDetailSet(Set<OrderDetail> orderDetailSet) {
        this.orderDetailSet = orderDetailSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return orderNumber == order.orderNumber &&
                Objects.equals(clientName, order.clientName) &&
                Objects.equals(placingDate, order.placingDate) &&
                Objects.equals(orderDetailSet, order.orderDetailSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientName, orderNumber, placingDate, orderDetailSet);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", clientName='" + clientName + '\'' +
                ", placingDate=" + placingDate +
                ", orderDetailSet=" + orderDetailSet +
                ", agent=" + agent +
                '}';
    }
}
