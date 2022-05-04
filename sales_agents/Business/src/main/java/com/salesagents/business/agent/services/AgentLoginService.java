package com.salesagents.business.agent.services;

import com.salesagents.domain.models.Agent;

public interface AgentLoginService {
    Agent login(String username, String password);
    void logout();
}
