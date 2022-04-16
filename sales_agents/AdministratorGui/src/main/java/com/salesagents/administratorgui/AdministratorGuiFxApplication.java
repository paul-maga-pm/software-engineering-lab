package com.salesagents.administratorgui;

import com.salesagents.administratorgui.controllers.AdministratorLoginController;
import com.salesagents.business.administrator.services.AdministratorLoginService;
import com.salesagents.exceptions.ExceptionBaseClass;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class AdministratorGuiFxApplication extends Application {
    private static AdministratorLoginService loginService;

    public static void setLoginService(AdministratorLoginService loginService) {
        AdministratorGuiFxApplication.loginService = loginService;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loginLoader = new FXMLLoader(AdministratorGuiFxApplication.class.getResource("administrator-login-view.fxml"));
        Scene loginScene = new Scene(loginLoader.load());

        FXMLLoader mainPageLoader = new FXMLLoader(AdministratorGuiFxApplication.class.getResource("administrator-main-page-view.fxml"));
        Scene mainPageScene = new Scene(mainPageLoader.load());

        AdministratorLoginController loginController = loginLoader.getController();
        loginController.setApplicationPrimaryStage(primaryStage);
        loginController.setAdministratorMainPageScene(mainPageScene);
        loginController.setLoginService(loginService);


        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void showExceptionMessageBox(ExceptionBaseClass exceptionBaseClass) {
        Alert alert = new Alert(Alert.AlertType.ERROR, exceptionBaseClass.getMessage(), ButtonType.CLOSE);
        alert.showAndWait();
    }
}
