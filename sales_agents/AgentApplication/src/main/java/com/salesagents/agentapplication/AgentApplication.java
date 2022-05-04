package com.salesagents.agentapplication;

import com.salesagents.agentgui.AgentGuiFxApplication;
import com.salesagents.business.agent.services.AgentLoginService;
import com.salesagents.business.agent.services.impl.AgentLoginServiceImpl;
import com.salesagents.business.agent.services.impl.ViewCatalogServiceImpl;
import com.salesagents.dataaccess.repository.hibernate.EmployeeDatabaseRepository;
import com.salesagents.dataaccess.repository.hibernate.ProductCatalogDatabaseRepository;
import com.salesagents.dataaccess.repository.security.Sha512HashAlgorithm;
import com.salesagents.networking.proxy.RpcClientStream;
import com.salesagents.networking.proxy.agent.AgentLoginProxy;
import com.salesagents.networking.proxy.agent.ViewCatalogProxy;
import javafx.application.Application;
import org.hibernate.cfg.Configuration;

import javax.xml.catalog.Catalog;

public class AgentApplication {
    public static void main(String[] args) {
        RpcClientStream stream = new RpcClientStream("127.0.0.1", 5555);
        AgentLoginService loginService = new AgentLoginProxy(stream);
        var viewCatalogService = new ViewCatalogProxy(stream);
        AgentGuiFxApplication.setLoginService(loginService);
        AgentGuiFxApplication.setViewCatalogService(viewCatalogService);
        Application.launch(AgentGuiFxApplication.class, args);
    }
}
