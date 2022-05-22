package com.salesagents.networking.proxy.agent;

import com.salesagents.business.OrderService;
import com.salesagents.domain.models.Order;
import com.salesagents.exceptions.ExceptionBaseClass;
import com.salesagents.networking.protocols.AgentRpcRequest;
import com.salesagents.networking.protocols.AgentRpcRequestType;
import com.salesagents.networking.protocols.RpcResponseType;
import com.salesagents.networking.proxy.RpcClientStream;

import java.util.Collection;

public class OrderServiceProxy implements OrderService {
    private RpcClientStream clientStream;

    public OrderServiceProxy(RpcClientStream clientStream) {
        this.clientStream = clientStream;
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
        return null;
    }
}
