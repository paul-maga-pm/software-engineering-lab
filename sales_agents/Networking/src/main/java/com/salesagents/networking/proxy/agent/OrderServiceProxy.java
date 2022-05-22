package com.salesagents.networking.proxy.agent;

import com.salesagents.business.OrderService;
import com.salesagents.business.utils.ProductObserver;
import com.salesagents.domain.models.Order;
import com.salesagents.exceptions.ExceptionBaseClass;
import com.salesagents.networking.protocols.AgentRpcRequest;
import com.salesagents.networking.protocols.AgentRpcRequestType;
import com.salesagents.networking.protocols.RpcResponseType;
import com.salesagents.networking.proxy.RpcClientStream;

import java.util.Collection;

public class OrderServiceProxy extends OrderService {
    private RpcClientStream clientStream;

    public OrderServiceProxy(RpcClientStream clientStream) {
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
    public void placeOrder(Order order) {
        AgentRpcRequest request = new AgentRpcRequest.RequestBuilder()
                .setType(AgentRpcRequestType.PLACE_ORDER)
                .setData(order)
                .build();

        clientStream.sendRequest(request);
        var response = clientStream.readResponse();

        if (response != null) {
            if (response.getType() == RpcResponseType.ERROR)
                    throw new ExceptionBaseClass(response.getData().toString());
            else if (response.getType() == RpcResponseType.OK)
                return;
        }
        throw new ExceptionBaseClass("Invalid response from server");
    }

    @Override
    public Collection<Order> getAllOrders() {
        return null;
    }

    @Override
    public Collection<Order> findByAgent(String username) {
        AgentRpcRequest request = new AgentRpcRequest.RequestBuilder()
                .setData(username)
                .setType(AgentRpcRequestType.GET_ORDERS)
                .build();

        clientStream.sendRequest(request);
        var response = clientStream.readResponse();

        if (response != null) {
            if (response.getType() == RpcResponseType.OK) {
                return (Collection<Order>)response.getData();
            }
            else if (response.getType() == RpcResponseType.ERROR)
                throw new ExceptionBaseClass(response.getData().toString());
        }
        throw new ExceptionBaseClass("Invalid response from server");
    }
}
