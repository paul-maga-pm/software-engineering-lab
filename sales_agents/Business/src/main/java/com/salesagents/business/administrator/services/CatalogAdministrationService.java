package com.salesagents.business.administrator.services;

import com.salesagents.business.utils.ProductObservable;
import com.salesagents.domain.models.Product;

import java.util.Collection;

public abstract class CatalogAdministrationService extends ProductObservable {
    public abstract boolean add(Product product);
    public abstract boolean update(Product product);
    public abstract boolean remove(String productId);
    public abstract Collection<Product> getAll();
}
