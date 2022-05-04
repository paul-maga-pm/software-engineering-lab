package com.salesagents.agentgui.controllers;

import com.salesagents.business.agent.services.AgentLoginService;
import com.salesagents.domain.models.Agent;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AgentMainPageController {
    public Button viewCatalogButton;
    private Stage applicationPrimaryStage;
    private Scene mainPageScene;
    private Scene loginScene;
    private Scene catalogScene;
    private AgentLoginService loginService;
    private Agent loggedAgent;
    private ViewCatalogController catalogController;

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

    public void setViewCatalogScene(Scene catalogScene) {
        this.catalogScene = catalogScene;
    }

    public void handleClickOnViewCatalogButton(ActionEvent event) {
        catalogController.setLoggedAgent(loggedAgent);
        catalogController.loadProductsToView();
        BorderPane root = (BorderPane) mainPageScene.getRoot();
        root.setCenter(catalogScene.getRoot());
    }

    public void setViewCatalogController(ViewCatalogController viewCatalogController) {
        this.catalogController = viewCatalogController;
    }
}
