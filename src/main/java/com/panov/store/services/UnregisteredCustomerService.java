package com.panov.store.services;

import com.panov.store.dao.DAO;
import com.panov.store.exceptions.ResourceNotCreatedException;
import com.panov.store.exceptions.ResourceNotDeletedException;
import com.panov.store.exceptions.ResourceNotFoundException;
import com.panov.store.exceptions.ResourceNotUpdatedException;
import com.panov.store.model.UnregisteredCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UnregisteredCustomerService {
    private final DAO<UnregisteredCustomer> repository;

    @Autowired
    public UnregisteredCustomerService(DAO<UnregisteredCustomer> repository) {
        this.repository = repository;
    }

    public List<UnregisteredCustomer> getUnregCustomerList() {
        var list = repository.getAll();
        if (list == null)
            return Collections.emptyList();
        return list;
    }

    public UnregisteredCustomer getById(Integer id) {
        return repository.get(id).orElseThrow(() -> new ResourceNotFoundException("Could not find this unregistered customer"));
    }

    public List<UnregisteredCustomer> getByPhoneNumber(String phoneNumber) {
        var customers = repository.getByColumn(phoneNumber);
        if (customers == null)
            return Collections.emptyList();
        return customers;
    }

    public Integer createUnregisteredCustomer(UnregisteredCustomer unregCust) {
        Integer id = null;

        try {
            id = repository.insert(unregCust);
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (id == null)
            throw new ResourceNotCreatedException("Could not create this unregistered customer");

        return id;
    }

    public Integer changeUnregisteredCustomer(UnregisteredCustomer unregCust) {
        Integer id = null;

        try {
            id = repository.update(unregCust);
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (id == null)
            throw new ResourceNotUpdatedException("Could not change this unregistered customer");

        return id;
    }

    public void deleteUnregisteredCustomer(Integer id) {
        try {
            repository.delete(id);
        } catch(Exception e) {
            e.printStackTrace();
            throw new ResourceNotDeletedException("Could not delete this unregistered customer");
        }
    }
}
