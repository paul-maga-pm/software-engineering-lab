package com.salesagents.networking.server;

import com.salesagents.business.OrderService;
import com.salesagents.business.administrator.services.AdministratorLoginService;
import com.salesagents.business.administrator.services.AgentsAdministrationService;
import com.salesagents.business.administrator.services.CatalogAdministrationService;
import com.salesagents.business.agent.services.AgentLoginService;
import com.salesagents.business.utils.ProductObserver;
import com.salesagents.domain.models.Administrator;
import com.salesagents.domain.models.Agent;
import com.salesagents.domain.models.Order;
import com.salesagents.domain.models.Product;
import com.salesagents.exceptions.ExceptionBaseClass;
import com.salesagents.networking.protocols.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RpcWorker implements Runnable, ProductObserver {
    private RpcServerStream serverStream;
    private AdministratorLoginService adminLoginService;
    private AgentsAdministrationService agentsAdministrationService;
    private boolean clientIsConnected;
    private CatalogAdministrationService catalogAdministrationService;
    private AgentLoginService agentLoginService;
    private OrderService orderService;

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
                    response = handleAgentRequest((AgentRpcRequest) request);

                serverStream.sendResponse(response);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
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

    private RpcResponse handleAgentRequest(AgentRpcRequest request) {
        if (request.getType() == AgentRpcRequestType.LOGIN)
            return handleAgentLoginRequest(request);

        if (request.getType() == AgentRpcRequestType.LOGOUT)
            return handleLogoutAgentRequest();

        if (request.getType() == AgentRpcRequestType.GET_ALL_PRODUCTS)
            return handleGetAllProductsRequest();

        if (request.getType() == AgentRpcRequestType.PLACE_ORDER)
            return handlePlaceOrderRequest(request);

        if (request.getType() == AgentRpcRequestType.GET_ORDERS)
            return handleGetOrdersOfAgentRequest(request);

        System.out.println("Invalid request");
        return new RpcResponse
                .ResponseBuilder()
                .setType(RpcResponseType.ERROR)
                .setData("Invalid request")
                .build();
    }

    private RpcResponse handleGetOrdersOfAgentRequest(AgentRpcRequest request) {
        try {
            String agentName = request.getData().toString();
            Collection<Order> orders = orderService.findByAgent(agentName);
            return new RpcResponse.ResponseBuilder()
                    .setData(orders)
                    .setType(RpcResponseType.OK)
                    .build();
        } catch (ExceptionBaseClass exception) {
            return new RpcResponse
                    .ResponseBuilder()
                    .setType(RpcResponseType.ERROR)
                    .setData(exception.getMessage())
                    .build();
        }
    }

    private RpcResponse handlePlaceOrderRequest(AgentRpcRequest request) {
        Order order = (Order) request.getData();
        try {
            orderService.placeOrder(order);
            return new RpcResponse.ResponseBuilder()
                    .setType(RpcResponseType.OK)
                    .build();
        } catch (ExceptionBaseClass exception) {
            return new RpcResponse
                    .ResponseBuilder()
                    .setType(RpcResponseType.ERROR)
                    .setData(exception.getMessage())
                    .build();
        }
    }

    private RpcResponse handleLogoutAgentRequest() {
        agentLoginService.logout();
        clientIsConnected = false;
        catalogAdministrationService.removeObserver(this);
        orderService.removeObserver(this);
        return new RpcResponse
                .ResponseBuilder()
                .setType(RpcResponseType.OK)
                .build();
    }

    private RpcResponse handleAgentLoginRequest(AgentRpcRequest request) {
        Map<String, String> authenticationInfo = (HashMap<String, String>) request.getData();
        String username = authenticationInfo.get("username");
        String password = authenticationInfo.get("password");

        try {
            Agent agent = agentLoginService.login(username, password);
            catalogAdministrationService.addObserver(this);
            orderService.addObserver(this);
            return new RpcResponse
                    .ResponseBuilder()
                    .setType(RpcResponseType.OK)
                    .setData(agent)
                    .build();
        } catch (ExceptionBaseClass exception) {
            return new RpcResponse
                    .ResponseBuilder()
                    .setType(RpcResponseType.ERROR)
                    .setData(exception.getMessage())
                    .build();
        }
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
            return handleGetAllProductsRequest();

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

    private RpcResponse handleGetAllProductsRequest() {
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
            orderService.addObserver(this);
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

    public void setAgentLoginService(AgentLoginService agentLoginService) {
        this.agentLoginService = agentLoginService;
    }

    @Override
    public void productWasAdded(Product newProduct) {
        RpcResponse notification = new RpcResponse
                .ResponseBuilder()
                .setData(newProduct)
                .setType(RpcResponseType.PRODUCT_WAS_ADDED)
                .build();

        try {
            serverStream.sendResponse(notification);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void productWasUpdated(Product newValueOfProduct) {
        RpcResponse notification = new RpcResponse
                .ResponseBuilder()
                .setData(newValueOfProduct)
                .setType(RpcResponseType.PRODUCT_WAS_UPDATED)
                .build();

        try {
            serverStream.sendResponse(notification);
        } catch (IOException exception) {
            System.out.println(exception.toString());
        }
    }

    @Override
    public void productWasRemoved(String idOfRemovedProduct) {
        RpcResponse notification = new RpcResponse
                .ResponseBuilder()
                .setType(RpcResponseType.PRODUCT_WAS_REMOVED)
                .setData(idOfRemovedProduct)
                .build();

        try {
            serverStream.sendResponse(notification);
        } catch (IOException exception) {
            System.out.println(exception.toString());
        }
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
