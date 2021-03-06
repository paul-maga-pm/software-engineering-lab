package com.salesagents.business.administrator.services.impl;

import com.salesagents.business.administrator.services.AdministratorLoginService;
import com.salesagents.business.exceptions.LoginException;
import com.salesagents.dataaccess.repository.EmployeeRepository;
import com.salesagents.domain.models.Administrator;
import com.salesagents.domain.models.Employee;


public class AdministratorLoginServiceImpl implements AdministratorLoginService {
    private EmployeeRepository employeeRepository;
    private boolean isAdminLogged;

    public AdministratorLoginServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        isAdminLogged = false;
    }

    @Override
    public Administrator login(String username, String password) {
        Employee employee = employeeRepository.findByUsernameAndPassword(username, password);
        Administrator administrator = null;

        if (employee == null)
            throw new LoginException("Invalid username or password");

        try {
            administrator = (Administrator) employee;

            if (isAdminLogged)
                throw new LoginException("Administrator is already logged");
            isAdminLogged = true;
        } catch (ClassCastException exception) {
            throw new LoginException("Incorrect username or password");
        }

        return administrator;
    }

    @Override
    public void logout() {
        isAdminLogged = false;
    }
}
