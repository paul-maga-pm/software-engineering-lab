package com.salesagents.business.utils;

import com.salesagents.domain.models.Product;

public interface ProductObserver {
    void productWasAdded(Product newProduct);
    void productWasUpdated(Product newValueOfProduct);
    void productWasRemoved(String idOfRemovedProduct);
}
