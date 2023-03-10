package com.panov.store.services;

import com.panov.store.exceptions.ResourceNotCreatedException;
import com.panov.store.exceptions.ResourceNotDeletedException;
import com.panov.store.exceptions.ResourceNotFoundException;
import com.panov.store.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.panov.store.dao.DAO;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final DAO<User> repository;

    @Autowired
    public UserService(DAO<User> repository) {
        this.repository = repository;
    }

    public List<User> getAllUserList() {
        return repository.getAll();
    }

    public User getById(Integer id) {
        return repository.get(id).orElseThrow(() -> new ResourceNotFoundException("Could not find this user"));
    }

    public List<User> getByNaturalId(String naturalId) {
        var users = repository.getByColumn(naturalId);
        if (users == null)
            return Collections.emptyList();
        return users;
    }

    public Integer createUser(User user) {
        var matches = thisNaturalIdExists(user);
        if (matches.size() != 0)
            throw new ResourceNotCreatedException(matches);

        Integer id = null;

        try {
            id = repository.insert(user);
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (id == null)
            throw new ResourceNotCreatedException("Could not create this user");

        return id;
    }

    public Integer changeUser(User user) {
        var matches = thisNaturalIdExists(user);
        if (matches.size() != 0)
            throw new ResourceNotCreatedException(matches);

        Integer id = null;

        try {
            id = repository.update(user);
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (id == null)
            throw new ResourceNotCreatedException("Could not change this user");

        return id;
    }

    public void deleteUser(Integer id) {
        try {
            repository.delete(id);
        } catch(Exception e) {
            e.printStackTrace();
            throw new ResourceNotDeletedException("Could not delete this user");
        }
    }

    private Map<String, String> thisNaturalIdExists(User user) {
        Map<String, String> matches = new HashMap<>();

        try {
            var phoneNumberMatch = getByNaturalId(user.getPersonalInfo().getPhoneNumber());
            if (phoneNumberMatch.size() != 0)
                matches.put("phoneNumber", "User with this phone number already exists");
        } catch(Exception ignored) {}

        try {
            var emailMatch = getByNaturalId(user.getPersonalInfo().getEmail());
            if (emailMatch.size() != 0)
                matches.put("email", "User with this phone number already exists");
        } catch(Exception ignored) {}

        return matches;
    }
}
