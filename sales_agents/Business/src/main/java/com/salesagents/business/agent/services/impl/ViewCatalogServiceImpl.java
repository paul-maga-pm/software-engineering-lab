package com.salesagents.business.agent.services.impl;

import com.salesagents.business.agent.services.ViewCatalogService;
import com.salesagents.dataaccess.repository.ProductCatalogRepository;
import com.salesagents.domain.models.Product;

import java.util.Collection;

public class ViewCatalogServiceImpl implements ViewCatalogService {
    private ProductCatalogRepository productCatalogRepository;

    public ViewCatalogServiceImpl(ProductCatalogRepository productCatalogRepository) {
        this.productCatalogRepository = productCatalogRepository;
    }

    @Override
    public Collection<Product> getAllProducts() {
        return productCatalogRepository.getAll();
    }
}
