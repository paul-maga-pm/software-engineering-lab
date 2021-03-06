package com.salesagents.administratorgui.controllers;

import com.salesagents.business.administrator.services.AdministratorLoginService;
import com.salesagents.exceptions.ExceptionBaseClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AdministratorLoginController {

    private AdministratorLoginService loginService;
    private Scene administratorMainPageScene;
    private Stage applicationPrimaryStage;
    private AdministratorMainPageController mainPageController;


    public void setMainPageController(AdministratorMainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }

    public void setAdministratorMainPageScene(Scene administratorMainPageScene) {
        this.administratorMainPageScene = administratorMainPageScene;
    }

    public void setApplicationPrimaryStage(Stage applicationPrimaryStage) {
        this.applicationPrimaryStage = applicationPrimaryStage;
    }


    public void setLoginService(AdministratorLoginService loginService) {
        this.loginService = loginService;
    }

    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField usernameTextField;
    @FXML
    public Button loginButton;

    public void handleClickOnLoginButton(ActionEvent event) {
        try {
            String username = usernameTextField.getText().strip();
            String password = passwordField.getText();
            loginService.login(username, password);
            mainPageController.bindToProductUpdates();
            applicationPrimaryStage.setScene(administratorMainPageScene);
        } catch (ExceptionBaseClass exception) {
            showExceptionMessageBox(exception);
        }
    }

    private void showExceptionMessageBox(ExceptionBaseClass exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.CLOSE);
        alert.showAndWait();
    }

}
