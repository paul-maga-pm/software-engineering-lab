package com.salesagents.dataaccess.repository;

import com.salesagents.domain.models.Employee;

public interface EmployeeRepository {
    void save(Employee employee);
    Employee findByUsernameAndPassword(String username, String password);
}
