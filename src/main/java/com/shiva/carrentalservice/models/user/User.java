package com.shiva.carrentalservice.models.user;

import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
public class User {
    private final String id;
    private final Person person;

    public User(@NonNull Person person) {
        this.id = UUID.randomUUID().toString();
        this.person = person;
    }
}
