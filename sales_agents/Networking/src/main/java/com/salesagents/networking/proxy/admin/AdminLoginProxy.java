package com.salesagents.networking.proxy.admin;

import com.salesagents.business.administrator.services.AdministratorLoginService;
import com.salesagents.domain.models.Administrator;
import com.salesagents.exceptions.ExceptionBaseClass;
import com.salesagents.networking.protocols.AdminRpcRequest;
import com.salesagents.networking.protocols.AdminRpcRequestType;
import com.salesagents.networking.protocols.RpcResponse;
import com.salesagents.networking.protocols.RpcResponseType;
import com.salesagents.networking.proxy.RpcClientStream;

import java.util.HashMap;
import java.util.Map;

public class AdminLoginProxy implements AdministratorLoginService {
    private RpcClientStream rpcClientStream;

    public AdminLoginProxy(RpcClientStream rpcClientStream) {
        this.rpcClientStream = rpcClientStream;
    }

    @Override
    public Administrator login(String username, String password) {
        rpcClientStream.openConnection();

        Map<String, String> authenticationInfoMap = new HashMap<>();
        authenticationInfoMap.put("username", username);
        authenticationInfoMap.put("password", password);
        AdminRpcRequest loginRequest = new AdminRpcRequest.RequestBuilder()
                .setData(authenticationInfoMap)
                .setType(AdminRpcRequestType.LOGIN)
                .build();

        rpcClientStream.sendRequest(loginRequest);
        RpcResponse response = rpcClientStream.readResponse();

        if (response != null) {
            if (response.getType() == RpcResponseType.ERROR) {
                String error = response.getData().toString();
                rpcClientStream.closeConnection();
                throw new ExceptionBaseClass(error);
            } else if (response.getType() == RpcResponseType.OK) {
                return (Administrator)response.getData();
            }
        }

        throw new ExceptionBaseClass("Received invalid response from server");
    }

    @Override
    public void logout() {
        AdminRpcRequest logoutRequest = new AdminRpcRequest
                .RequestBuilder()
                .setType(AdminRpcRequestType.LOGOUT)
                .build();

        rpcClientStream.sendRequest(logoutRequest);
        rpcClientStream.readResponse();
        rpcClientStream.closeConnection();
    }
}
