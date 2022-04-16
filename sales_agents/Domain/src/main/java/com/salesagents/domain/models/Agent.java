package com.salesagents.domain.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("AGENT")
public class Agent extends Employee{
    public Agent() {
    }

    public Agent(String name, String username, String password) {
        super(name, username, password);
    }
}
