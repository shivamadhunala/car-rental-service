package com.shiva.carrentalservice.services;

import com.shiva.carrentalservice.models.Address;
import com.shiva.carrentalservice.models.user.Person;
import com.shiva.carrentalservice.models.user.User;
import lombok.NonNull;

import java.util.HashMap;

public class UserService {
    private final HashMap<String, User> users;

    public UserService() {
        this.users = new HashMap<>();
    }

    public User createUser(@NonNull Person person) {
        User user = new User(person);
        users.put(user.getId(), user);
        return user;
    }

    public Person createPerson(@NonNull String name, @NonNull Address address, @NonNull String email, @NonNull String phone) {
        return new Person(name, address, email, phone);
    }

    public User getUser(@NonNull String userId) {
        return users.get(userId);
    }

}
