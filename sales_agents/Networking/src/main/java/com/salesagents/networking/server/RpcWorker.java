package com.salesagents.networking.server;

import com.salesagents.business.administrator.services.AdministratorLoginService;
import com.salesagents.domain.models.Administrator;
import com.salesagents.exceptions.ExceptionBaseClass;
import com.salesagents.networking.protocols.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RpcWorker implements Runnable{
    private RpcServerStream serverStream;
    private AdministratorLoginService adminLoginService;
    private boolean clientIsConnected;

    public RpcWorker(RpcServerStream serverStream) {
        this.serverStream = serverStream;
        this.clientIsConnected = true;
    }

    public void setAdminLoginService(AdministratorLoginService adminLoginService) {
        this.adminLoginService = adminLoginService;
    }

    @Override
    public void run() {
        System.out.println("Client connected");
        while (clientIsConnected) {
            try {
                RpcRequest request = serverStream.readRequest();

                RpcResponse response;

                if (request instanceof AdminRpcRequest)
                    response = handleAdminRequest((AdminRpcRequest) request);
                else
                    response = handleAgentRequest(request);

                serverStream.sendResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            serverStream.closeObjectStreams();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Client disconnected");
    }

    private RpcResponse handleAgentRequest(RpcRequest request) {
        return null;
    }

    private RpcResponse handleAdminRequest(AdminRpcRequest request) {

        if (request.getType() == AdminRpcRequestType.LOGIN)
            return handleAdminLoginRequest(request);

        if (request.getType() == AdminRpcRequestType.LOGOUT)
            return handleAdminLogoutRequest();

        throw new ExceptionBaseClass("Invalid request");
    }

    private RpcResponse handleAdminLogoutRequest() {
        adminLoginService.logout();
        clientIsConnected = false;
        return new RpcResponse.ResponseBuilder()
                .setType(RpcResponseType.OK)
                .build();
    }

    private RpcResponse handleAdminLoginRequest(AdminRpcRequest request) {
        Map<String, String> authenticationInfoMap = (HashMap<String, String>) request.getData();
        String username = authenticationInfoMap.get("username");
        String password = authenticationInfoMap.get("password");

        try {
            Administrator admin = adminLoginService.login(username, password);
            return new RpcResponse.ResponseBuilder()
                    .setType(RpcResponseType.OK)
                    .setData(admin)
                    .build();
        } catch (ExceptionBaseClass exception) {
            clientIsConnected = false;
            return new RpcResponse.ResponseBuilder()
                    .setData(exception.getMessage())
                    .setType(RpcResponseType.ERROR)
                    .build();
        }
    }
}
