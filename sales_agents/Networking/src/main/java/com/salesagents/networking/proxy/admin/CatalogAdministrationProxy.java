package com.salesagents.networking.proxy.admin;

import com.salesagents.business.administrator.services.CatalogAdministrationService;
import com.salesagents.business.utils.ProductObserver;
import com.salesagents.domain.models.Product;
import com.salesagents.exceptions.ExceptionBaseClass;
import com.salesagents.networking.protocols.*;
import com.salesagents.networking.proxy.RpcClientStream;

import java.util.Collection;

public class CatalogAdministrationProxy extends CatalogAdministrationService {
    private RpcClientStream clientStream;

    public CatalogAdministrationProxy(RpcClientStream clientStream) {
        this.clientStream = clientStream;
    }

    @Override
    public synchronized void addObserver(ProductObserver observer) {
        clientStream.addObserver(observer);
    }

    @Override
    public synchronized void removeObserver(ProductObserver observer) {
        clientStream.removeObserver(observer);
    }

    @Override
    public boolean add(Product product) {
        AdminRpcRequest request = new AdminRpcRequest.AdminRequestBuilder()
                .setData(product)
                .setType(AdminRpcRequestType.ADD_PRODUCT)
                .build();

        clientStream.sendRequest(request);
        RpcResponse response = clientStream.readResponse();

        if (response != null) {
            if (response.getType() == RpcResponseType.ERROR) {
                String error = response.getData().toString();
                throw new ExceptionBaseClass(error);
            } else if (response.getType() == RpcResponseType.OK) {
                return (Boolean)response.getData();
            }
        }
        throw new ExceptionBaseClass("Invalid response from server");
    }

    @Override
    public boolean update(Product product) {
        AdminRpcRequest request = new AdminRpcRequest.AdminRequestBuilder()
                .setType(AdminRpcRequestType.UPDATE_PRODUCT)
                .setData(product)
                .build();

        clientStream.sendRequest(request);
        RpcResponse response = clientStream.readResponse();

        if (response != null) {
            if (response.getType() == RpcResponseType.ERROR)
                throw new ExceptionBaseClass(response.getData().toString());
            else if (response.getType() == RpcResponseType.OK)
                return (Boolean) response.getData();
        }
        throw new ExceptionBaseClass("Invalid response from server");
    }

    @Override
    public boolean remove(String productId) {
        AdminRpcRequest request = new AdminRpcRequest.AdminRequestBuilder()
                .setType(AdminRpcRequestType.REMOVE_PRODUCT)
                .setData(productId)
                .build();

        clientStream.sendRequest(request);
        RpcResponse response = clientStream.readResponse();

        if (response != null) {
            if (response.getType() == RpcResponseType.ERROR)
                throw new ExceptionBaseClass(response.getData().toString());
            else if (response.getType() == RpcResponseType.OK)
                return (Boolean)response.getData();
        }
        throw new ExceptionBaseClass("Invalid response from server");
    }

    @Override
    public Collection<Product> getAll() {
        AdminRpcRequest request = new AdminRpcRequest.AdminRequestBuilder()
                .setType(AdminRpcRequestType.GET_ALL_PRODUCTS)
                .build();

        clientStream.sendRequest(request);
        RpcResponse response = clientStream.readResponse();

        if (response != null) {
            if (response.getType() == RpcResponseType.ERROR) {
                String error = response.getData().toString();
                throw new ExceptionBaseClass(error);
            } else if (response.getType() == RpcResponseType.OK)
                return (Collection<Product>)response.getData();
        }
        throw new ExceptionBaseClass("Invalid response from server");
    }
}
