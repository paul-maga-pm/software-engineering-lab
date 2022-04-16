package com.salesagents.dataaccess.repository.hibernate;

import com.salesagents.dataaccess.repository.EmployeeRepository;
import com.salesagents.dataaccess.repository.exceptions.DatabaseException;
import com.salesagents.dataaccess.repository.security.HashAlgorithm;
import com.salesagents.domain.models.Employee;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


public class EmployeeDatabaseRepository implements EmployeeRepository {
    private HashAlgorithm hashAlgorithm;
    private SessionFactory sessionFactory;

    private static final String FIND_BY_USERNAME_AND_PASSWORD_HQL =
            "FROM Employee " +
            "WHERE username=:usernameParam and password=:passwordParam";


    public EmployeeDatabaseRepository(SessionFactory factory, HashAlgorithm hashAlgorithm) {
        this.sessionFactory = factory;
        this.hashAlgorithm = hashAlgorithm;
    }

    @Override
    public void save(Employee employee) {
        var existingEmployee = findByUsernameAndPassword(employee.getUsername(), employee.getPassword());
        if (existingEmployee == null) {
            Transaction saveTransaction = null;
            var password = employee.getPassword();
            try (Session session = sessionFactory.openSession()) {
                var hashedPassword = hashAlgorithm.hash(password);

                employee.setPassword(hashedPassword);
                saveTransaction = session.beginTransaction();
                session.persist(employee);
                saveTransaction.commit();
            } catch (HibernateException exception) {
                if (saveTransaction != null)
                    saveTransaction.rollback();
                throw new DatabaseException(exception);
            } finally {
                employee.setPassword(password);
            }
        }
    }

    @Override
    public Employee findByUsernameAndPassword(String username, String password) {
        try(Session session = sessionFactory.openSession()) {
            var query = session.createQuery(FIND_BY_USERNAME_AND_PASSWORD_HQL, Employee.class);
            query.setParameter("usernameParam", username);
            var hashedPassword = hashAlgorithm.hash(password);
            query.setParameter("passwordParam", hashedPassword);
            var result = query.getResultList();

            if (result.size() == 0)
                return null;
            var foundEmployee = result.get(0);
            foundEmployee.setPassword(password);
            return foundEmployee;
        } catch (HibernateException exception) {
            throw new DatabaseException(exception);
        }
    }
}
