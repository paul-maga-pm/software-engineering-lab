package com.salesagents.dataaccess.repository;

import com.salesagents.domain.models.Product;

import java.util.Collection;
import java.util.Optional;

public interface ProductCatalogRepository {
    boolean add(Product product);
    boolean remove(String productId);
    boolean update(Product newValue);
    Collection<Product> getAll();
}
