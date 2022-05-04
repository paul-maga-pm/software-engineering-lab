package com.salesagents.agentapplication;

import com.salesagents.agentgui.AgentGuiFxApplication;
import com.salesagents.business.agent.services.AgentLoginService;
import com.salesagents.business.agent.services.impl.AgentLoginServiceImpl;
import com.salesagents.dataaccess.repository.hibernate.EmployeeDatabaseRepository;
import com.salesagents.dataaccess.repository.security.Sha512HashAlgorithm;
import javafx.application.Application;
import org.hibernate.cfg.Configuration;

public class AgentApplication {
    public static void main(String[] args) {
        var sessionFactory = new Configuration().configure().buildSessionFactory();
        var passwordHashAlgorithm = new Sha512HashAlgorithm();
        var employeeRepository = new EmployeeDatabaseRepository(sessionFactory, passwordHashAlgorithm);

        AgentLoginService loginService = new AgentLoginServiceImpl(employeeRepository);

        AgentGuiFxApplication.setLoginService(loginService);

        Application.launch(AgentGuiFxApplication.class, args);
    }
}
