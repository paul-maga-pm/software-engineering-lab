package com.salesagents.dataaccess.repository;

import com.salesagents.domain.models.Agent;
import com.salesagents.domain.models.Employee;

import java.util.Collection;

public interface EmployeeRepository {
    void save(Employee employee);
    Employee findByUsernameAndPassword(String username, String password);
    Employee findByUsername(String username);
    Collection<Agent> getAllAgents();
}
