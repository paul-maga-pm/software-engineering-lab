package com.salesagents.dataaccess.repository.hibernate;

import com.salesagents.dataaccess.repository.OrderRepository;
import com.salesagents.dataaccess.repository.exceptions.DatabaseException;
import com.salesagents.domain.models.Order;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


import java.util.List;

public class OrderDatabaseRepository implements OrderRepository {
    private SessionFactory sessionFactory;

    public OrderDatabaseRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Order> getAll() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            var selectAllQuery = session.createQuery("FROM Order", Order.class);
            var result =  selectAllQuery.getResultList();
            transaction.commit();
            return result;
        } catch (HibernateException exception) {
            if (transaction != null)
                transaction.rollback();
            throw new DatabaseException(exception);
        }
    }

    @Override
    public void save(Order order) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(order);
            transaction.commit();
        } catch (HibernateException exception) {
            if (transaction != null)
                transaction.rollback();
            throw new DatabaseException(exception);
        }
    }

    @Override
    public List<Order> findByAgent(String username) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            var query = session.createQuery("from Order where agent_username=:=username_param", Order.class);
            query.setParameter("username_param", username);
            transaction = session.beginTransaction();
            var result = query.getResultList();
            transaction.commit();
            return result;
        } catch (HibernateException exception) {
            if (transaction != null)
                transaction.rollback();
            throw new DatabaseException(exception);
        }
    }
}
