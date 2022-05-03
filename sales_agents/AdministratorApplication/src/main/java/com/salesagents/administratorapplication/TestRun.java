package com.salesagents.administratorapplication;

import com.salesagents.dataaccess.repository.ProductCatalogRepository;
import com.salesagents.dataaccess.repository.hibernate.ProductCatalogDatabaseRepository;
import com.salesagents.domain.models.Product;
import org.hibernate.cfg.Configuration;

import javax.swing.*;

public class TestRun {
    public static void main(String[] args) {
        var sessionFactory = new Configuration().configure().buildSessionFactory();
        var productRepo = new ProductCatalogDatabaseRepository(sessionFactory);

        var p1 = new Product.ProductBuilder()
                .setId("1")
                .setProductName("1")
                .setType("1")
                .setManufacturer("1")
                .setPrice(1.0)
                .setQuantityInStock(10)
                .build();

        var p2 = new Product.ProductBuilder()
                .setId("2")
                .setProductName("2")
                .setType("2")
                .setManufacturer("2")
                .setPrice(2.0)
                .setQuantityInStock(20)
                .build();

        var p3 = new Product.ProductBuilder()
                .setId("3")
                .setProductName("3")
                .setType("3")
                .setManufacturer("3")
                .setPrice(3.0)
                .setQuantityInStock(30)
                .build();

        productRepo.add(p1);
        productRepo.add(p2);
        productRepo.add(p3);

        for(var p : productRepo.getAll())
            System.out.println(p);
        System.out.println("*******************");
        productRepo.remove(p2.getId());

        for(var p : productRepo.getAll())
            System.out.println(p);
        System.out.println("*******************");
        p1.setPrice(1000.0);
        p1.setQuantityInStock(999);

        productRepo.update(p1);

        for(var p : productRepo.getAll())
            System.out.println(p);

        System.out.println("*******************");
    }
}
