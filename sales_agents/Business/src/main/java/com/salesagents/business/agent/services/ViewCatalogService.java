package com.salesagents.business.agent.services;

import com.salesagents.business.utils.ProductObservable;
import com.salesagents.domain.models.Product;

import java.util.Collection;

public abstract class ViewCatalogService extends ProductObservable {
    public abstract Collection<Product> getAllProducts();
}
