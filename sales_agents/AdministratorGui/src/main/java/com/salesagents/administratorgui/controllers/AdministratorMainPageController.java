package com.salesagents.administratorgui.controllers;

import com.salesagents.business.administrator.services.AdministratorLoginService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AdministratorMainPageController {
    public Button viewAgentsButton;

    private Stage primaryStage;
    // Scenes
    private Scene loginScene;
    private Scene mainPageScene;
    private Scene agentsViewScene;

    private AdministratorLoginService loginService;
    private AgentsAdministrationController agentsAdministrationController;
    private CatalogAdministrationController catalogController;
    private Scene catalogViewScene;

    public void setCatalogController(CatalogAdministrationController catalogController) {
        this.catalogController = catalogController;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setMainPageScene(Scene mainPageScene) {
        this.mainPageScene = mainPageScene;
    }

    public void setAgentsViewScene(Scene agentsViewScene) {
        this.agentsViewScene = agentsViewScene;
    }

    public void setLoginScene(Scene loginScene) {
        this.loginScene = loginScene;
    }

    public void setLoginService(AdministratorLoginService loginService) {
        this.loginService = loginService;
    }

    @FXML
    public Button logoutButton;

    public void handleClickOnLogoutButton(ActionEvent event) {
        loginService.logout();
        primaryStage.setScene(loginScene);
        BorderPane root = (BorderPane) mainPageScene.getRoot();
        root.setCenter(new AnchorPane());
        catalogController.unsubscribeFromProductUpdates();
        catalogController.clearProductsFromView();
        agentsAdministrationController.clearAgentsFromView();
    }

    public void handleClickOnViewAgentsButton(ActionEvent event) {
        agentsAdministrationController.loadAgentsToView();
        BorderPane root = (BorderPane) mainPageScene.getRoot();
        root.setCenter(agentsViewScene.getRoot());
    }

    public void setAgentsAdministrationController(AgentsAdministrationController agentsAdministrationController) {
        this.agentsAdministrationController = agentsAdministrationController;
    }

    public void handleClickOnViewCatalogButton(ActionEvent event) {
        catalogController.loadProductsToView();
        BorderPane root = (BorderPane) mainPageScene.getRoot();
        root.setCenter(catalogViewScene.getRoot());
    }

    public void setCatalogViewScene(Scene catalogViewScene) {
        this.catalogViewScene = catalogViewScene;
    }

    public void bindToProductUpdates() {
        catalogController.bindToProductUpdates();
    }
}
