package com.salesagents.administratorapplication;

import com.salesagents.administratorgui.AdministratorGuiFxApplication;
import com.salesagents.networking.proxy.admin.AdminLoginProxy;
import com.salesagents.networking.proxy.RpcClientStream;
import com.salesagents.networking.proxy.admin.AgentsAdministrationProxy;
import com.salesagents.networking.proxy.admin.CatalogAdministrationProxy;
import com.salesagents.networking.proxy.OrderServiceProxy;
import javafx.application.Application;

public class AdministratorApplication {
    public static void main(String[] args) {

        var clientStream = new RpcClientStream("127.0.0.1", 5555);
        var administratorLoginService = new AdminLoginProxy(clientStream);
        var agentAdministrationService = new AgentsAdministrationProxy(clientStream);
        var catalogAdministrationService = new CatalogAdministrationProxy(clientStream);
        var orderService = new OrderServiceProxy(clientStream);

        AdministratorGuiFxApplication.setLoginService(administratorLoginService);
        AdministratorGuiFxApplication.setAgentsAdministrationService(agentAdministrationService);
        AdministratorGuiFxApplication.setCatalogAdministrationService(catalogAdministrationService);
        AdministratorGuiFxApplication.setOrderService(orderService);
        Application.launch(AdministratorGuiFxApplication.class, args);
    }
}
