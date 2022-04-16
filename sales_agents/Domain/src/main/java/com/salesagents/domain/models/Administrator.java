package com.salesagents.domain.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Administrator extends Employee{
    public Administrator() {
    }
    public Administrator(String name, String username, String password) {
        super(name, username, password);
    }

}
