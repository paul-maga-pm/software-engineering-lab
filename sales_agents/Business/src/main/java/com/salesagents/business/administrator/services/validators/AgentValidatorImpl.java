package com.salesagents.business.administrator.services.validators;

import com.salesagents.business.administrator.services.exceptions.InvalidAgentException;
import com.salesagents.dataaccess.repository.EmployeeRepository;
import com.salesagents.domain.models.Agent;

public class AgentValidatorImpl implements AgentValidator{
    private EmployeeRepository employeeRepository;

    public AgentValidatorImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void validate(Agent agent) {
        String errors = "";

        if (agent.getName().equals(""))
            errors += "Name can't be empty\n";

        String username = agent.getUsername();
        String password = agent.getPassword();
        if (username.equals(""))
            errors += "Username can't be empty\n";

        if (password.equals(""))
            errors += "Password can't be empty\n";

        if (!errors.equals(""))
            throw new InvalidAgentException(errors);

        if (employeeRepository.findByUsernameAndPassword(username, password) != null)
            throw new InvalidAgentException(username + " is already used");
    }
}
