package com.salesagents.agentgui;

import com.salesagents.agentgui.controllers.AgentLoginController;
import com.salesagents.agentgui.controllers.AgentMainPageController;
import com.salesagents.agentgui.controllers.ViewCatalogController;
import com.salesagents.business.agent.services.AgentLoginService;
import com.salesagents.business.agent.services.ViewCatalogService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AgentGuiFxApplication extends Application {
    private static AgentLoginService loginService;
    private static ViewCatalogService viewCatalogService;

    public static void setLoginService(AgentLoginService loginService) {
        AgentGuiFxApplication.loginService = loginService;
    }

    public static void setViewCatalogService(ViewCatalogService viewCatalogService) {
        AgentGuiFxApplication.viewCatalogService = viewCatalogService;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loginSceneLoader = new FXMLLoader(AgentGuiFxApplication.class.getResource("agent-login-view.fxml"));
        Scene loginScene = new Scene(loginSceneLoader.load());

        FXMLLoader mainPageLoader = new FXMLLoader(AgentGuiFxApplication.class.getResource("agent-main-page-view.fxml"));
        Scene mainPageScene = new Scene(mainPageLoader.load());

        FXMLLoader catalogViewLoader = new FXMLLoader(AgentGuiFxApplication.class.getResource("catalog-view.fxml"));
        Scene catalogScene = new Scene(catalogViewLoader.load());

        AgentLoginController loginController = loginSceneLoader.getController();
        loginController.setApplicationPrimaryStage(primaryStage);
        loginController.setLoginService(loginService);
        loginController.setAdministratorMainPageScene(mainPageScene);

        ViewCatalogController viewCatalogController = catalogViewLoader.getController();
        viewCatalogController.setViewCatalogService(viewCatalogService);


        AgentMainPageController mainPageController = mainPageLoader.getController();
        mainPageController.setMainPageScene(mainPageScene);
        mainPageController.setLoginScene(loginScene);
        mainPageController.setViewCatalogScene(catalogScene);
        mainPageController.setApplicationPrimaryStage(primaryStage);
        mainPageController.setLoginService(loginService);
        mainPageController.setViewCatalogController(viewCatalogController);


        loginController.setMainPageController(mainPageController);

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
}
