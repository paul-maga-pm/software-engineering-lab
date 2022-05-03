package com.salesagents.business.administrator.services.impl;

import com.salesagents.business.administrator.services.CatalogAdministrationService;
import com.salesagents.business.administrator.services.validators.ProductValidator;
import com.salesagents.dataaccess.repository.ProductCatalogRepository;
import com.salesagents.domain.models.Product;

import java.util.Collection;

public class CatalogAdministrationServiceImpl implements CatalogAdministrationService {
    private ProductValidator productValidator;
    private ProductCatalogRepository productRepository;

    public CatalogAdministrationServiceImpl(ProductValidator productValidator,
                                            ProductCatalogRepository productRepository) {
        this.productValidator = productValidator;
        this.productRepository = productRepository;
    }

    @Override
    public boolean add(Product product) {
        productValidator.validate(product);
        return productRepository.add(product);
    }

    @Override
    public boolean update(Product product) {
        productValidator.validate(product);
        return productRepository.update(product);
    }

    @Override
    public boolean remove(String productId) {
        return productRepository.remove(productId);
    }

    @Override
    public Collection<Product> getAll() {
        return productRepository.getAll();
    }
}
