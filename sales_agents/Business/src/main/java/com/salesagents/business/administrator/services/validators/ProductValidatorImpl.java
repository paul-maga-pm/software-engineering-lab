package com.salesagents.business.administrator.services.validators;

import com.salesagents.business.exceptions.InvalidEntityException;
import com.salesagents.domain.models.Product;

import java.util.Objects;

public class ProductValidatorImpl implements ProductValidator{

    @Override
    public void validate(Product product) {
        String error = "";

        if (Objects.equals(product.getId(), ""))
            error += "Id can't be empty\n";

        if (product.getProductName().equals(""))
            error += "Name can't be empty\n";

        if (product.getManufacturer().equals(""))
            error += "Manufacturer can't be empty\n";

        if (product.getPrice() < 0)
            error += "Price can't be negative\n";

        if (product.getQuantityInStock() < 0)
           error += "Quantity can't be negative";

        if (error.length() > 0)
            throw new InvalidEntityException(error);
    }
}
