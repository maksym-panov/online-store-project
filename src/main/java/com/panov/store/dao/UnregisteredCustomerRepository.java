package com.panov.store.dao;

import com.panov.store.model.UnregisteredCustomer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UnregisteredCustomerRepository implements DAO<UnregisteredCustomer> {
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public UnregisteredCustomerRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Optional<UnregisteredCustomer> get(int id) {
        var entityManager = getManager();
        var unregisteredCustomer = Optional.ofNullable(entityManager.find(UnregisteredCustomer.class, id));
        entityManager.close();
        return unregisteredCustomer;
    }

    @Override
    public Optional<UnregisteredCustomer> getByColumn(String naturalId, String value) {
        var entityManager = getManager();
        Optional<UnregisteredCustomer> unregisteredCustomer =
                Optional.ofNullable((UnregisteredCustomer) entityManager
                    .createQuery("select uc from UnregisteredCustomer uc where :col = :val")
                    .setParameter("col", naturalId)
                    .setParameter("val", value)
                    .getSingleResult()
                );
        entityManager.close();
        return unregisteredCustomer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UnregisteredCustomer> getAll() {
        var entityManager = getManager();
        List<UnregisteredCustomer> unregisteredCustomers =
                (List<UnregisteredCustomer>) entityManager
                        .createQuery("select uc from UnregisteredCustomer uc")
                        .getResultList();
        entityManager.close();
        return unregisteredCustomers;
    }

    @Override
    public Integer insert(UnregisteredCustomer unregisteredCustomer) {
        var entityManager = getManager();
        entityManager.persist(unregisteredCustomer);
        entityManager.close();
        return unregisteredCustomer.getUnregisteredCustomerId();
    }

    @Override
    public Integer update(UnregisteredCustomer unregisteredCustomer) {
        var entityManager = getManager();
        entityManager.merge(unregisteredCustomer);
        entityManager.close();
        return unregisteredCustomer.getUnregisteredCustomerId();
    }

    @Override
    public void delete(UnregisteredCustomer unregisteredCustomer) {
        var entityManager = getManager();
        entityManager.remove(unregisteredCustomer);
        entityManager.close();
    }

    private EntityManager getManager() {
        return entityManagerFactory.createEntityManager();
    }
}