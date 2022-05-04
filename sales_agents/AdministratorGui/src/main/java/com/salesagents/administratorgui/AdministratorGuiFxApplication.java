package com.salesagents.administratorgui;

import com.salesagents.administratorgui.controllers.AdministratorLoginController;
import com.salesagents.administratorgui.controllers.AdministratorMainPageController;
import com.salesagents.administratorgui.controllers.AgentsAdministrationController;
import com.salesagents.administratorgui.controllers.CatalogAdministrationController;
import com.salesagents.business.administrator.services.AdministratorLoginService;
import com.salesagents.business.administrator.services.AgentsAdministrationService;
import com.salesagents.business.administrator.services.CatalogAdministrationService;
import com.salesagents.business.administrator.services.impl.AgentsAdministrationServiceImpl;
import com.salesagents.exceptions.ExceptionBaseClass;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class AdministratorGuiFxApplication extends Application {
    private static AdministratorLoginService loginService;
    private static AgentsAdministrationService agentAdministrationService;
    private static CatalogAdministrationService catalogAdministrationService;

    public static void setLoginService(AdministratorLoginService loginService) {
        AdministratorGuiFxApplication.loginService = loginService;
    }

    public static void setAgentsAdministrationService(AgentsAdministrationService agentAdministrationService) {
        AdministratorGuiFxApplication.agentAdministrationService = agentAdministrationService;
    }

    public static void setCatalogAdministrationService(CatalogAdministrationService catalogAdministrationService) {
        AdministratorGuiFxApplication.catalogAdministrationService = catalogAdministrationService;
    }

    public static void showExceptionMessageBox(String exceptionMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR, exceptionMessage, ButtonType.CLOSE);
        alert.showAndWait();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loginLoader = new FXMLLoader(AdministratorGuiFxApplication.class.getResource("administrator-login-view.fxml"));
        Scene loginScene = new Scene(loginLoader.load());

        FXMLLoader mainPageLoader = new FXMLLoader(AdministratorGuiFxApplication.class.getResource("administrator-main-page-view.fxml"));
        Scene mainPageScene = new Scene(mainPageLoader.load());

        FXMLLoader agentsViewLoader = new FXMLLoader(AdministratorGuiFxApplication.class.getResource("agents-view.fxml"));
        Scene agentsViewScene = new Scene(agentsViewLoader.load());


        FXMLLoader catalogViewLoader = new FXMLLoader(AdministratorGuiFxApplication.class.getResource("catalog-view.fxml"));
        Scene catalogViewScene = new Scene(catalogViewLoader.load());

        AdministratorLoginController loginController = loginLoader.getController();
        loginController.setApplicationPrimaryStage(primaryStage);
        loginController.setAdministratorMainPageScene(mainPageScene);
        loginController.setLoginService(loginService);

        AgentsAdministrationController agentsAdministrationController = agentsViewLoader.getController();
        agentsAdministrationController.setAgentsAdministrationService(agentAdministrationService);

        CatalogAdministrationController catalogController = catalogViewLoader.getController();
        catalogController.setCatalogAdministrationService(catalogAdministrationService);

        AdministratorMainPageController mainPageController = mainPageLoader.getController();
        mainPageController.setLoginScene(loginScene);
        mainPageController.setMainPageScene(mainPageScene);
        mainPageController.setAgentsViewScene(agentsViewScene);
        mainPageController.setCatalogViewScene(catalogViewScene);

        mainPageController.setPrimaryStage(primaryStage);
        mainPageController.setLoginService(loginService);

        mainPageController.setAgentsAdministrationController(agentsAdministrationController);
        mainPageController.setCatalogController(catalogController);

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void showExceptionMessageBox(ExceptionBaseClass exceptionBaseClass) {
        Alert alert = new Alert(Alert.AlertType.ERROR, exceptionBaseClass.getMessage(), ButtonType.CLOSE);
        alert.showAndWait();
    }
}
