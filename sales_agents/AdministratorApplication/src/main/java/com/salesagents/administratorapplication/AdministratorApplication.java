package com.salesagents.administratorapplication;

import com.salesagents.administratorgui.AdministratorGuiFxApplication;
import com.salesagents.business.administrator.services.impl.AdministratorLoginServiceImpl;
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

        AdministratorGuiFxApplication.setLoginService(administratorLoginService);
        Application.launch(AdministratorGuiFxApplication.class, args);
    }
}
