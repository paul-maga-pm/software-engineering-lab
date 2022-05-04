package com.salesagents.business.administrator.services.impl;

import com.salesagents.business.administrator.services.CatalogAdministrationService;
import com.salesagents.business.administrator.services.validators.ProductValidator;
import com.salesagents.business.utils.ProductObservable;
import com.salesagents.dataaccess.repository.ProductCatalogRepository;
import com.salesagents.domain.models.Product;

import java.util.Collection;

public class CatalogAdministrationServiceImpl extends CatalogAdministrationService {
    private ProductValidator productValidator;
    private ProductCatalogRepository productRepository;

    public CatalogAdministrationServiceImpl(ProductValidator productValidator,
                                            ProductCatalogRepository productRepository) {
        this.productValidator = productValidator;
        this.productRepository = productRepository;
    }

    @Override
    synchronized public boolean add(Product product) {
        productValidator.validate(product);
        boolean productWasAdded = productRepository.add(product);

        if (productWasAdded)
            notifyThatProductWasAdded(product);

        return productWasAdded;
    }

    @Override
    synchronized  public boolean update(Product product) {
        productValidator.validate(product);

        boolean productWasUpdated = productRepository.update(product);

        if (productWasUpdated)
            notifyThatProductWasUpdated(product);

        return productWasUpdated;
    }

    @Override
    synchronized  public boolean remove(String productId) {
        boolean productWasRemoved = productRepository.remove(productId);

        if (productWasRemoved)
            notifyThatProductWasRemoved(productId);

        return productWasRemoved;
    }

    @Override
    synchronized  public Collection<Product> getAll() {
        return productRepository.getAll();
    }
}
