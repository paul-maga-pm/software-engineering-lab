package com.salesagents.business.agent.services;

import com.salesagents.domain.models.Product;

import java.util.Collection;

public interface ViewCatalogService {
    Collection<Product> getAllProducts();
}
