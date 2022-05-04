package com.salesagents.networking.proxy.admin;

import com.salesagents.business.administrator.services.AgentsAdministrationService;
import com.salesagents.domain.models.Agent;
import com.salesagents.exceptions.ExceptionBaseClass;
import com.salesagents.networking.protocols.AdminRpcRequest;
import com.salesagents.networking.protocols.AdminRpcRequestType;
import com.salesagents.networking.protocols.RpcResponse;
import com.salesagents.networking.protocols.RpcResponseType;
import com.salesagents.networking.proxy.RpcClientStream;

import java.util.Collection;

public class AgentsAdministrationProxy implements AgentsAdministrationService {
    private RpcClientStream clientStream;

    public AgentsAdministrationProxy(RpcClientStream clientStream) {
        this.clientStream = clientStream;
    }

    @Override
    public void addAgent(Agent agent) {
        AdminRpcRequest request = new AdminRpcRequest
                .RequestBuilder()
                .setData(agent)
                .setType(AdminRpcRequestType.REGISTER_AGENT)
                .build();

        clientStream.sendRequest(request);
        RpcResponse response = clientStream.readResponse();

        if (response != null) {
            if (response.getType() == RpcResponseType.ERROR) {
                String error = response.getData().toString();
                throw new ExceptionBaseClass(error);
            } else if (response.getType() == RpcResponseType.OK) {
                return;
            }
        }

        throw new ExceptionBaseClass("Invalid response from server");
    }

    @Override
    public Collection<Agent> getAllAgents() {
        AdminRpcRequest request = new AdminRpcRequest
                .RequestBuilder()
                .setType(AdminRpcRequestType.GET_ALL_AGENTS)
                .build();
        clientStream.sendRequest(request);
        RpcResponse response = clientStream.readResponse();

        if (response != null) {
            if (response.getType() == RpcResponseType.ERROR) {
                String error = response.getData().toString();
                throw new ExceptionBaseClass(error);
            } else if (response.getType() == RpcResponseType.OK) {
                return (Collection<Agent>) response.getData();
            }
        }

        throw new ExceptionBaseClass("Invalid response from server");
    }
}
