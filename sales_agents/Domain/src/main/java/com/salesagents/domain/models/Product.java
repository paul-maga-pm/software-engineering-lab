package com.salesagents.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;

import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @Column (name = "id")
    private String id;

    @Column (name = "product_name")
    private String productName;

    @Column (name = "product_type")
    private String type;

    @Column (name = "manufacturer")
    private String manufacturer;

    @Column (name = "price")
    private double price;

    @Column (name = "quantity_in_stock")
    private int quantityInStock;

    @Column (name = "deleted")
    private boolean isDeleted;

    public Product() {

    }

    public static class ProductBuilder {
        private Product instance;


        public ProductBuilder() {
            instance = new Product();
        }

        public ProductBuilder setId(String id) {
            instance.setId(id);
            return this;
        }

        public ProductBuilder setProductName(String productName) {
            instance.setProductName(productName);
            return this;
        }

        public ProductBuilder setManufacturer(String manufacturer) {
            instance.setManufacturer(manufacturer);
            return this;
        }

        public ProductBuilder setType(String type) {
            instance.setType(type);
            return this;
        }

        public ProductBuilder setPrice(double price) {
            instance.setPrice(price);
            return this;
        }

        public ProductBuilder setQuantityInStock(int quantity) {
            instance.setQuantityInStock(quantity);
            return this;
        }

        public Product build() {
            return instance;
        }

    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getType() {
        return type;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 &&
                quantityInStock == product.quantityInStock &&
                Objects.equals(id, product.id) &&
                Objects.equals(productName, product.productName) &&
                Objects.equals(type, product.type) &&
                Objects.equals(manufacturer, product.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, type, manufacturer, price, quantityInStock);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", productName='" + productName + '\'' +
                ", type='" + type + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", price=" + price +
                ", quantityInStock=" + quantityInStock +
                '}';
    }
}
