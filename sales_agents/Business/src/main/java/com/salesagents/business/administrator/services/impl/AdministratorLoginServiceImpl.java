package com.salesagents.business.administrator.services.impl;

import com.salesagents.business.administrator.services.AdministratorLoginService;
import com.salesagents.business.exceptions.LoginException;
import com.salesagents.dataaccess.repository.EmployeeRepository;
import com.salesagents.domain.models.Administrator;
import com.salesagents.domain.models.Employee;


public class AdministratorLoginServiceImpl implements AdministratorLoginService {
    private EmployeeRepository employeeRepository;

    public AdministratorLoginServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Administrator login(String username, String password) {
        Employee employee = employeeRepository.findByUsernameAndPassword(username, password);
        Administrator administrator = null;


        try {
            administrator = (Administrator) employee;
        } catch (ClassCastException exception) {
            throw new LoginException("Incorrect username or password");
        }

        return administrator;
    }

    @Override
    public void logout() {

    }
}
