package com.salesagents.administratorapplication;

import com.salesagents.administratorgui.AdministratorGuiFxApplication;
import com.salesagents.business.administrator.services.impl.AdministratorLoginServiceImpl;
import com.salesagents.business.administrator.services.impl.AgentsAdministrationServiceImpl;
import com.salesagents.business.administrator.services.validators.AgentValidatorImpl;
import com.salesagents.dataaccess.repository.hibernate.EmployeeDatabaseRepository;
import com.salesagents.dataaccess.repository.security.Sha512HashAlgorithm;
import javafx.application.Application;
import org.hibernate.cfg.Configuration;

public class AdministratorApplication {
    public static void main(String[] args) {
        var sessionFactory = new Configuration().configure().buildSessionFactory();
        var passwordHashAlgorithm = new Sha512HashAlgorithm();
        var employeeRepository = new EmployeeDatabaseRepository(sessionFactory, passwordHashAlgorithm);

        var administratorLoginService = new AdministratorLoginServiceImpl(employeeRepository);

        var agentValidator = new AgentValidatorImpl(employeeRepository);
        var agentAdministrationService = new AgentsAdministrationServiceImpl(employeeRepository, agentValidator);

        AdministratorGuiFxApplication.setLoginService(administratorLoginService);
        AdministratorGuiFxApplication.setAgentsAdministrationService(agentAdministrationService);

        Application.launch(AdministratorGuiFxApplication.class, args);
    }
}