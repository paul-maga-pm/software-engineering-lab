package com.salesagents.agentgui.controllers;

import com.salesagents.business.agent.services.AgentLoginService;
import com.salesagents.business.agent.services.ViewCatalogService;
import com.salesagents.domain.models.Agent;
import com.salesagents.exceptions.ExceptionBaseClass;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AgentLoginController {
    public PasswordField passwordField;
    public TextField usernameTextField;
    public Button loginButton;

    private AgentMainPageController mainPageController;
    private ViewCatalogController viewCatalogController;
    private ViewCatalogService viewCatalogService;

    private AgentLoginService loginService;
    private Scene administratorMainPageScene;
    private Stage applicationPrimaryStage;

    public void handleClickOnLoginButton(ActionEvent event) {
        try {
            String username = usernameTextField.getText().strip();
            String password = passwordField.getText();
            Agent agent = loginService.login(username, password);
            mainPageController.setLoggedAgent(agent);
            viewCatalogService.addObserver(viewCatalogController);
            applicationPrimaryStage.setScene(administratorMainPageScene);
        } catch (ExceptionBaseClass exception) {
            showExceptionMessageBox(exception);
        }
    }

    public void setLoginService(AgentLoginService loginService) {
        this.loginService = loginService;
    }

    public void setAdministratorMainPageScene(Scene administratorMainPageScene) {
        this.administratorMainPageScene = administratorMainPageScene;
    }

    public void setApplicationPrimaryStage(Stage applicationPrimaryStage) {
        this.applicationPrimaryStage = applicationPrimaryStage;
    }

    private void showExceptionMessageBox(ExceptionBaseClass exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.CLOSE);
        alert.showAndWait();
    }

    public void setMainPageController(AgentMainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }

    public void setViewCatalogController(ViewCatalogController viewCatalogController) {
        this.viewCatalogController = viewCatalogController;
    }

    public void setCatalogAdministrationService(ViewCatalogService catalogAdministrationService) {
        this.viewCatalogService = catalogAdministrationService;
    }
}
