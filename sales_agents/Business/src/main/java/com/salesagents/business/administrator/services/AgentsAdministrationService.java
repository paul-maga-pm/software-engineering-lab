package com.salesagents.business.administrator.services;

import com.salesagents.domain.models.Agent;

import java.util.Collection;

public interface AgentsAdministrationService {
    void addAgent(Agent agent);
    Collection<Agent> getAllAgents();
}
