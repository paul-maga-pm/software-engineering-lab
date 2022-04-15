package com.salesagents.dataaccess.repository.hibernate;

import com.salesagents.dataaccess.repository.EmployeeRepository;
import com.salesagents.dataaccess.repository.security.HashAlgorithm;
import com.salesagents.domain.models.Employee;

public class EmployeeDatabaseRepository implements EmployeeRepository {
    private HashAlgorithm hashAlgorithm;

    public EmployeeDatabaseRepository(HashAlgorithm hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    @Override
    public Employee save(Employee employee) {
        return null;
    }

    @Override
    public Employee findByUsernameAndPassword(String username, String password) {
        return null;
    }
}
