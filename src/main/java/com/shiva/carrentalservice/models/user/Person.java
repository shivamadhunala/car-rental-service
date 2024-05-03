package com.shiva.carrentalservice.models.user;

import com.shiva.carrentalservice.models.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Person {
    private String name;
    private Address address;
    private String email;
    private String phone;
}
