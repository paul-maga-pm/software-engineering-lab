package com.salesagents.networking.proxy.agent;

import com.salesagents.business.agent.services.AgentLoginService;
import com.salesagents.domain.models.Agent;
import com.salesagents.exceptions.ExceptionBaseClass;
import com.salesagents.networking.protocols.AgentRpcRequest;
import com.salesagents.networking.protocols.AgentRpcRequestType;
import com.salesagents.networking.protocols.RpcResponse;
import com.salesagents.networking.protocols.RpcResponseType;
import com.salesagents.networking.proxy.RpcClientStream;

import java.util.HashMap;
import java.util.Map;

public class AgentLoginProxy implements AgentLoginService {
    private RpcClientStream clientStream;

    public AgentLoginProxy(RpcClientStream clientStream) {
        this.clientStream = clientStream;
    }

    @Override
    public Agent login(String username, String password) {
        Map<String, String> authenticationInfoMap = new HashMap<>();
        authenticationInfoMap.put("username", username);
        authenticationInfoMap.put("password", password);
        AgentRpcRequest request = new AgentRpcRequest
                .RequestBuilder()
                .setData(authenticationInfoMap)
                .setType(AgentRpcRequestType.LOGIN)
                .build();

        clientStream.openConnection();
        clientStream.sendRequest(request);
        RpcResponse response = clientStream.readResponse();

        if (response != null) {
            if (response.getType() == RpcResponseType.ERROR)
                throw new ExceptionBaseClass(response.getData().toString());
            else if (response.getType() == RpcResponseType.OK)
                return (Agent)response.getData();
        }
        throw new ExceptionBaseClass("Invalid response from server");
    }

    @Override
    public void logout() {
        AgentRpcRequest request = new AgentRpcRequest
                .RequestBuilder()
                .setType(AgentRpcRequestType.LOGOUT)
                .build();

        clientStream.sendRequest(request);
        clientStream.readResponse();
        clientStream.closeConnection();
    }
}
