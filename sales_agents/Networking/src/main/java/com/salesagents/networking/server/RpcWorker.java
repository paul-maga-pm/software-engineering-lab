package com.salesagents.networking.server;

import com.salesagents.business.administrator.services.AdministratorLoginService;
import com.salesagents.business.administrator.services.AgentsAdministrationService;
import com.salesagents.business.administrator.services.CatalogAdministrationService;
import com.salesagents.domain.models.Administrator;
import com.salesagents.domain.models.Agent;
import com.salesagents.domain.models.Product;
import com.salesagents.exceptions.ExceptionBaseClass;
import com.salesagents.networking.protocols.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RpcWorker implements Runnable{
    private RpcServerStream serverStream;
    private AdministratorLoginService adminLoginService;
    private AgentsAdministrationService agentsAdministrationService;
    private boolean clientIsConnected;
    private CatalogAdministrationService catalogAdministrationService;

    public RpcWorker(RpcServerStream serverStream) {
        this.serverStream = serverStream;
        this.clientIsConnected = true;
    }

    public void setAdminLoginService(AdministratorLoginService adminLoginService) {
        this.adminLoginService = adminLoginService;
    }

    public void setAgentsAdministrationService(AgentsAdministrationService agentsAdministrationService) {
        this.agentsAdministrationService = agentsAdministrationService;
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

        if (request.getType() == AdminRpcRequestType.REGISTER_AGENT)
            return handleAdminRegisterAgentRequest(request);

        if (request.getType() == AdminRpcRequestType.GET_ALL_AGENTS)
            return handleAdminGetAllAgentsRequest(request);

        if (request.getType() == AdminRpcRequestType.ADD_PRODUCT)
            return handleAddProductAdminRequest(request);

        if (request.getType() == AdminRpcRequestType.GET_ALL_PRODUCTS)
            return handleGetAllProductsAdminRequest();

        if (request.getType() == AdminRpcRequestType.UPDATE_PRODUCT)
            return handleUpdateProductAdminRequest(request);

        if (request.getType() == AdminRpcRequestType.REMOVE_PRODUCT)
            return handleRemoveProductAdminRequest(request);

        System.out.println("Invalid request");
        return new RpcResponse
                .ResponseBuilder()
                .setType(RpcResponseType.ERROR)
                .setData("Invalid request")
                .build();
    }

    private RpcResponse handleRemoveProductAdminRequest(AdminRpcRequest request) {
        try {
            String productId = request.getData().toString();
            boolean removeResult = catalogAdministrationService.remove(productId);
            return new RpcResponse
                    .ResponseBuilder()
                    .setType(RpcResponseType.OK)
                    .setData(removeResult)
                    .build();
        } catch (ExceptionBaseClass exception) {
            return new RpcResponse
                    .ResponseBuilder()
                    .setType(RpcResponseType.ERROR)
                    .setData(exception.getMessage())
                    .build();
        }
    }

    private RpcResponse handleUpdateProductAdminRequest(AdminRpcRequest request) {
        try {
            Product product = (Product) request.getData();
            boolean updateResult = catalogAdministrationService.update(product);
            return new RpcResponse
                    .ResponseBuilder()
                    .setType(RpcResponseType.OK)
                    .setData(updateResult)
                    .build();
        } catch (ExceptionBaseClass exception) {
            return new RpcResponse
                    .ResponseBuilder()
                    .setType(RpcResponseType.ERROR)
                    .setData(exception.getMessage())
                    .build();
        }
    }

    private RpcResponse handleGetAllProductsAdminRequest() {
        try {
            Collection<Product> products = catalogAdministrationService.getAll();
            return new RpcResponse
                    .ResponseBuilder()
                    .setType(RpcResponseType.OK)
                    .setData(products)
                    .build();
        } catch (ExceptionBaseClass exception) {
            return new RpcResponse.ResponseBuilder()
                    .setType(RpcResponseType.ERROR)
                    .setData(exception.getMessage())
                    .build();
        }
    }

    private RpcResponse handleAddProductAdminRequest(AdminRpcRequest request) {
        try {
            Product product = (Product) request.getData();
            boolean addingResult = catalogAdministrationService.add(product);
            return new RpcResponse.ResponseBuilder()
                    .setType(RpcResponseType.OK)
                    .setData(addingResult)
                    .build();
        } catch (ExceptionBaseClass exception) {
            return new RpcResponse.ResponseBuilder()
                    .setType(RpcResponseType.ERROR)
                    .setData(exception.getMessage())
                    .build();
        }
    }

    private RpcResponse handleAdminGetAllAgentsRequest(AdminRpcRequest request) {
        try {
            var agents = agentsAdministrationService.getAllAgents();
            return new RpcResponse
                    .ResponseBuilder()
                    .setType(RpcResponseType.OK)
                    .setData(agents)
                    .build();
        } catch (ExceptionBaseClass exception) {
            String error = exception.getMessage();
            return new RpcResponse
                    .ResponseBuilder()
                    .setType(RpcResponseType.ERROR)
                    .setData(error)
                    .build();
        }
    }

    private RpcResponse handleAdminRegisterAgentRequest(AdminRpcRequest request) {
        Agent agent = (Agent) request.getData();

        try {
            agentsAdministrationService.addAgent(agent);
            return new RpcResponse.ResponseBuilder()
                    .setType(RpcResponseType.OK)
                    .build();
        } catch (ExceptionBaseClass exceptionBaseClass) {
            String error = exceptionBaseClass.getMessage();
            return new RpcResponse.ResponseBuilder()
                    .setData(error)
                    .setType(RpcResponseType.ERROR)
                    .build();
        }
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

    public void setCatalogAdministrationService(CatalogAdministrationService catalogAdministrationService) {
        this.catalogAdministrationService = catalogAdministrationService;
    }
}
