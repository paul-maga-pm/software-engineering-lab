package com.salesagents.business.administrator.services;

import com.salesagents.domain.models.Agent;

import java.util.Collection;

public interface AgentsAdministrationService {
    void addAgent(String name, String username, String password);
    Collection<Agent> getAllAgents();
}
