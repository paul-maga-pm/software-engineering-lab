package com.salesagents.business.administrator.services;

import com.salesagents.domain.models.Administrator;

public interface AdministratorLoginService {
    Administrator login(String username, String password);
    void logout();
}
