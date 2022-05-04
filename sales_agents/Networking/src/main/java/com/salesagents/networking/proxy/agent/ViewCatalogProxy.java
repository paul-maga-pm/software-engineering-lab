package com.salesagents.networking.proxy.agent;

import com.salesagents.business.agent.services.ViewCatalogService;
import com.salesagents.business.utils.ProductObservable;
import com.salesagents.business.utils.ProductObserver;
import com.salesagents.domain.models.Product;
import com.salesagents.exceptions.ExceptionBaseClass;
import com.salesagents.networking.protocols.AgentRpcRequest;
import com.salesagents.networking.protocols.AgentRpcRequestType;
import com.salesagents.networking.protocols.RpcResponse;
import com.salesagents.networking.protocols.RpcResponseType;
import com.salesagents.networking.proxy.RpcClientStream;

import java.util.Collection;

public class ViewCatalogProxy  extends ViewCatalogService {
    private RpcClientStream clientStream;

    public ViewCatalogProxy(RpcClientStream clientStream) {
        this.clientStream = clientStream;
    }

    @Override
    public Collection<Product> getAllProducts() {
        AgentRpcRequest request = new AgentRpcRequest
                .RequestBuilder()
                .setType(AgentRpcRequestType.GET_ALL_PRODUCTS)
                .build();
        clientStream.sendRequest(request);
        RpcResponse response = clientStream.readResponse();

        if (response != null) {
            if (response.getType() == RpcResponseType.ERROR)
                throw new ExceptionBaseClass(response.getData().toString());
            else if (response.getType() == RpcResponseType.OK)
                return (Collection<Product>) response.getData();
        }
        throw new ExceptionBaseClass("Invalid response from server");
    }

    @Override
    public synchronized void addObserver(ProductObserver observer) {
        clientStream.addObserver(observer);
    }

    @Override
    public synchronized void removeObserver(ProductObserver observer) {
        clientStream.removeObserver(observer);
    }
}
