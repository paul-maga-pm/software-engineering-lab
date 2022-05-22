package com.salesagents.domain.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="order_details")
public class OrderDetail implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "product_id", referencedColumnName = "id")
    private Product product;


    @Column (name = "quantity")
    private int quantityInOrder;

    public OrderDetail() {

    }

    public OrderDetail(Product product, int quantityInOrder) {
        this.product = product;
        this.quantityInOrder = quantityInOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantityInOrder() {
        return quantityInOrder;
    }

    public void setQuantityInOrder(int quantityInOrder) {
        this.quantityInOrder = quantityInOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetail)) return false;
        OrderDetail that = (OrderDetail) o;
        return Objects.equals(product.getId(), that.product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(product.getId());
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "product=" + product +
                ", quantityInOrder=" + quantityInOrder +
                '}';
    }


}
