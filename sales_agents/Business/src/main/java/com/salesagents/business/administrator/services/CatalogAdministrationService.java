package com.salesagents.business.administrator.services;

import com.salesagents.domain.models.Product;

import java.util.Collection;

public interface CatalogAdministrationService {
    boolean add(Product product);
    boolean update(Product product);
    boolean remove(String productId);
    Collection<Product> getAll();
}
