package com.salesagents.business.agent.services.impl;

import com.salesagents.business.agent.services.AgentLoginService;
import com.salesagents.business.exceptions.LoginException;
import com.salesagents.dataaccess.repository.EmployeeRepository;
import com.salesagents.domain.models.Agent;
import com.salesagents.domain.models.Employee;

public class AgentLoginServiceImpl implements AgentLoginService {
    private EmployeeRepository employeeRepository;

    public AgentLoginServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Agent login(String username, String password) {
        Employee employee = employeeRepository.findByUsernameAndPassword(username, password);

        if (employee == null)
            throw new LoginException("Invalid username or password");

        Agent agent = null;

        try {
            agent = (Agent) employee;
        } catch (ClassCastException exception) {
            throw new LoginException("Agent is not registered");
        }
        return agent;
    }

    @Override
    public void logout() {

    }
}
