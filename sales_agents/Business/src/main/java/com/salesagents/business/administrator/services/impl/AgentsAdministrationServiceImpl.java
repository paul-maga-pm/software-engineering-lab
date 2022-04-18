package com.salesagents.business.administrator.services.impl;

import com.salesagents.business.administrator.services.AgentsAdministrationService;
import com.salesagents.business.administrator.services.validators.AgentValidator;
import com.salesagents.business.administrator.services.validators.AgentValidatorImpl;
import com.salesagents.dataaccess.repository.EmployeeRepository;
import com.salesagents.dataaccess.repository.hibernate.EmployeeDatabaseRepository;
import com.salesagents.domain.models.Agent;

import java.util.Collection;

public class AgentsAdministrationServiceImpl implements AgentsAdministrationService {
    private EmployeeRepository employeeRepository;
    private AgentValidator agentValidator;

    public AgentsAdministrationServiceImpl(EmployeeDatabaseRepository employeeRepository,
                                           AgentValidatorImpl agentValidator) {
        this.employeeRepository = employeeRepository;
        this.agentValidator = agentValidator;
    }


    @Override
    public void addAgent(String name, String username, String password) {
        Agent agent = new Agent(name, username, password);
        agentValidator.validate(agent);
        employeeRepository.save(agent);
    }

    @Override
    public Collection<Agent> getAllAgents() {
        return employeeRepository.getAllAgents();
    }
}
