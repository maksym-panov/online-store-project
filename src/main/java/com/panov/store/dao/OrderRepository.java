package com.panov.store.dao;

import com.panov.store.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository implements DAO<Order> {
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public OrderRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Optional<Order> get(int id) {
        var entityManager = getManager();
        var order = Optional.ofNullable(entityManager.find(Order.class, id));
        entityManager.close();
        return order;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> getAll() {
        var entityManager = getManager();
        List<Order> orders = (List<Order>) entityManager
                .createQuery("select o from Order o")
                .getResultList();
        entityManager.close();
        return orders;
    }

    @Override
    public List<Order> getByColumn(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer insert(Order order) {
        var entityManager = getManager();
        entityManager.getTransaction().begin();

        attach(entityManager, order);
        entityManager.persist(order);

        entityManager.getTransaction().commit();
        entityManager.close();

        return order.getOrderId();
    }

    @Override
    public Integer update(Order order) {
        var entityManager = getManager();
        entityManager.getTransaction().begin();

        attach(entityManager, order);
        entityManager.merge(order);

        entityManager.getTransaction().commit();
        entityManager.close();
        return order.getOrderId();
    }

    @Override
    public void delete(Integer id) {
        var entityManager = getManager();
        entityManager.getTransaction().begin();

        var order = entityManager.find(Order.class, id);
        attach(entityManager, order);
        entityManager.remove(order);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private EntityManager getManager() {
        return entityManagerFactory.createEntityManager();
    }

    private void attach(EntityManager entityManager, Order order) {
        // Order products
        var detOrdProd = order.getOrderProducts();
        if (detOrdProd == null)
            detOrdProd = new ArrayList<>();
        List<OrderProducts> orderProducts = new ArrayList<>();
        for (var d : detOrdProd) {
            try {
                orderProducts.add(entityManager.find(OrderProducts.class, d.getOrderProductsId()));
            } catch(Exception ignored) {}
        }
        order.setOrderProducts(orderProducts);

        // Delivery type
        var detDelType = order.getDeliveryType();
        try {
            order.setDeliveryType(entityManager.find(DeliveryType.class, detDelType.getDeliveryTypeId()));
        } catch(Exception ignored) {}

        // User
        var detUser = order.getUser();
        try {
            order.setUser(entityManager.find(User.class, detUser.getUserId()));
        } catch(Exception ignored) {}

        // Unregistered customer
        var detUnregCust = order.getUnregisteredCustomer();
        try {
            order.setUnregisteredCustomer(entityManager
                    .find(UnregisteredCustomer.class, detUnregCust.getUnregisteredCustomerId()));
        } catch(Exception ignored) {}
    }
}
