package com.salesagents.dataaccess.repository.hibernate;

import com.salesagents.dataaccess.repository.ProductCatalogRepository;
import com.salesagents.dataaccess.repository.exceptions.DatabaseException;
import com.salesagents.domain.models.Product;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.management.Query;
import java.util.Collection;
import java.util.Optional;

public class ProductCatalogDatabaseRepository implements ProductCatalogRepository {
    private SessionFactory sessionFactory;

    public ProductCatalogDatabaseRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean add(Product product) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            var findByIdQuery = session.createQuery("from Product where id=:id_param", Product.class);
            findByIdQuery.setParameter("id_param", product.getId());
            var resultList = findByIdQuery.getResultList();
            if (resultList.size() > 0)
                return false;
            transaction = session.beginTransaction();
            session.persist(product);
            transaction.commit();
            return true;
        } catch (HibernateException exception) {
            if (transaction != null)
                transaction.rollback();
            throw new DatabaseException(exception);
        }
    }

    @Override
    public boolean remove(String productId) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            var findByIdQuery = session.createQuery("from Product where id=:id_param and isDeleted=false", Product.class);
            findByIdQuery.setParameter("id_param", productId);
            var resultList = findByIdQuery.getResultList();
            if (resultList.size() < 1)
                return false;

            transaction = session.beginTransaction();

            session.createQuery("update Product set isDeleted=true where id=:id_param")
                            .setParameter("id_param", productId)
                            .executeUpdate();

            transaction.commit();
            return true;
        } catch (HibernateException exception) {
            if (transaction != null)
                transaction.rollback();
            throw new DatabaseException(exception);
        }
    }

    @Override
    public boolean update(Product newValue) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            var findByIdQuery = session.createQuery("from Product where id=:id_param and isDeleted=false", Product.class);
            findByIdQuery.setParameter("id_param", newValue.getId());
            var resultList = findByIdQuery.getResultList();

            if (resultList.size() < 1)
                return false;

            transaction = session.beginTransaction();
            session.merge(newValue);
            transaction.commit();
            return true;
        } catch (HibernateException exception) {
            if (transaction != null)
                transaction.rollback();
            throw new DatabaseException(exception);
        }
    }

    @Override
    public Collection<Product> getAll() {
        try (Session session = sessionFactory.openSession()) {
            var selectAllQuery = session.createQuery("from Product where isDeleted=false", Product.class);
            return selectAllQuery.getResultList();
        } catch (HibernateException exception) {
            throw new DatabaseException(exception);
        }

    }


}
