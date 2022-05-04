package com.salesagents.agentgui.controllers;

import com.salesagents.business.agent.services.AgentLoginService;
import com.salesagents.domain.models.Agent;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AgentMainPageController {
    private Stage applicationPrimaryStage;
    private Scene mainPageScene;
    private Scene loginScene;
    private AgentLoginService loginService;
    private Agent loggedAgent;

    public void setLoginService(AgentLoginService loginService) {
        this.loginService = loginService;
    }

    public void setApplicationPrimaryStage(Stage applicationPrimaryStage) {
        this.applicationPrimaryStage = applicationPrimaryStage;
    }

    public void setMainPageScene(Scene mainPageScene) {
        this.mainPageScene = mainPageScene;
    }

    public void setLoginScene(Scene loginScene) {
        this.loginScene = loginScene;
    }

    public Button logoutButton;

    public void setLoggedAgent(Agent agent) {
        loggedAgent = agent;
    }

    public void handleClickOnLogoutButton(ActionEvent event) {
        loginService.logout();
        applicationPrimaryStage.setScene(loginScene);
        loggedAgent = null;
    }
}
