package com.salesagents.administratorgui;

import com.salesagents.administratorgui.controllers.AdministratorLoginController;
import com.salesagents.administratorgui.controllers.AdministratorMainPageController;
import com.salesagents.administratorgui.controllers.AgentsAdministrationController;
import com.salesagents.administratorgui.controllers.CatalogAdministrationController;
import com.salesagents.business.OrderService;
import com.salesagents.business.administrator.services.AdministratorLoginService;
import com.salesagents.business.administrator.services.AgentsAdministrationService;
import com.salesagents.business.administrator.services.CatalogAdministrationService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdministratorGuiFxApplication extends Application {
    private static AdministratorLoginService loginService;
    private static AgentsAdministrationService agentAdministrationService;
    private static CatalogAdministrationService catalogAdministrationService;
    private static OrderService orderService;

    public static void setLoginService(AdministratorLoginService loginService) {
        AdministratorGuiFxApplication.loginService = loginService;
    }

    public static void setAgentsAdministrationService(AgentsAdministrationService agentAdministrationService) {
        AdministratorGuiFxApplication.agentAdministrationService = agentAdministrationService;
    }

    public static void setCatalogAdministrationService(CatalogAdministrationService catalogAdministrationService) {
        AdministratorGuiFxApplication.catalogAdministrationService = catalogAdministrationService;
    }

    public static void setOrderService(OrderService orderService) {
        AdministratorGuiFxApplication.orderService = orderService;
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
        mainPageController.setOrderService(orderService);
        mainPageController.setLoginScene(loginScene);
        mainPageController.setMainPageScene(mainPageScene);
        mainPageController.setAgentsViewScene(agentsViewScene);
        mainPageController.setCatalogViewScene(catalogViewScene);

        mainPageController.setPrimaryStage(primaryStage);
        mainPageController.setLoginService(loginService);

        mainPageController.setAgentsAdministrationController(agentsAdministrationController);
        mainPageController.setCatalogController(catalogController);

        loginController.setMainPageController(mainPageController);

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

}
