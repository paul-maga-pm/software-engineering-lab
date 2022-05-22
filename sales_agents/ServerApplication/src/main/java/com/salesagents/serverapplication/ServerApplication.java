package com.salesagents.serverapplication;

import com.salesagents.business.OrderServiceImpl;
import com.salesagents.business.administrator.services.impl.AdministratorLoginServiceImpl;
import com.salesagents.business.administrator.services.impl.AgentsAdministrationServiceImpl;
import com.salesagents.business.administrator.services.impl.CatalogAdministrationServiceImpl;
import com.salesagents.business.administrator.services.validators.AgentValidatorImpl;
import com.salesagents.business.administrator.services.validators.ProductValidatorImpl;
import com.salesagents.business.agent.services.AgentLoginService;
import com.salesagents.business.agent.services.impl.AgentLoginServiceImpl;
import com.salesagents.dataaccess.repository.hibernate.EmployeeDatabaseRepository;
import com.salesagents.dataaccess.repository.hibernate.OrderDatabaseRepository;
import com.salesagents.dataaccess.repository.hibernate.ProductCatalogDatabaseRepository;
import com.salesagents.dataaccess.repository.security.Sha512HashAlgorithm;
import com.salesagents.domain.models.Agent;
import com.salesagents.domain.models.Order;
import com.salesagents.domain.models.OrderDetail;
import com.salesagents.domain.models.Product;
import com.salesagents.networking.server.RpcServer;
import org.hibernate.cfg.Configuration;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ServerApplication {
    public static void main(String[] args) {
        var sessionFactory = new Configuration().configure().buildSessionFactory();
        var passwordHashAlgorithm = new Sha512HashAlgorithm();
        var employeeRepository = new EmployeeDatabaseRepository(sessionFactory, passwordHashAlgorithm);

        var administratorLoginService = new AdministratorLoginServiceImpl(employeeRepository);

        var agentValidator = new AgentValidatorImpl(employeeRepository);
        var agentAdministrationService = new AgentsAdministrationServiceImpl(employeeRepository, agentValidator);

        var catalogRepo = new ProductCatalogDatabaseRepository(sessionFactory);
        var productValidator = new ProductValidatorImpl();
        var catalogAdministrationService = new CatalogAdministrationServiceImpl(productValidator, catalogRepo);

        var orderRepo = new OrderDatabaseRepository(sessionFactory);
        var orderService = new OrderServiceImpl(orderRepo);

        AgentLoginService agentLoginService = new AgentLoginServiceImpl(employeeRepository);

        RpcServer server = new RpcServer(5555);
        server.setAdminLoginService(administratorLoginService);
        server.setAgentsAdministrationService(agentAdministrationService);
        server.setCatalogAdministrationService(catalogAdministrationService);

        server.setAgentLoginService(agentLoginService);
        server.setOrderService(orderService);
        server.start();
    }
}
