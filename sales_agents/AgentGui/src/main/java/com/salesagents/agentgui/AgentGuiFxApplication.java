package com.salesagents.agentgui;

import com.salesagents.agentgui.controllers.AgentLoginController;
import com.salesagents.agentgui.controllers.AgentMainPageController;
import com.salesagents.agentgui.controllers.ViewCatalogController;
import com.salesagents.business.OrderService;
import com.salesagents.business.agent.services.AgentLoginService;
import com.salesagents.business.agent.services.ViewCatalogService;
import com.salesagents.domain.models.Agent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AgentGuiFxApplication extends Application {
    private static AgentLoginService loginProxy;
    private static ViewCatalogService viewCatalogService;
    private static OrderService orderService;

    public static void setLoginService(AgentLoginService loginService) {
        AgentGuiFxApplication.loginProxy = loginService;
    }

    public static void setViewCatalogService(ViewCatalogService viewCatalogService) {
        AgentGuiFxApplication.viewCatalogService = viewCatalogService;
    }

    public static void setOrderService(OrderService orderServiceProxy) {
        AgentGuiFxApplication.orderService = orderServiceProxy;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loginSceneLoader = new FXMLLoader(AgentGuiFxApplication.class.getResource("agent-login-view.fxml"));
        Scene loginScene = new Scene(loginSceneLoader.load());

        FXMLLoader mainPageLoader = new FXMLLoader(AgentGuiFxApplication.class.getResource("agent-main-page-view.fxml"));
        Scene mainPageScene = new Scene(mainPageLoader.load());

        FXMLLoader catalogViewLoader = new FXMLLoader(AgentGuiFxApplication.class.getResource("catalog-view.fxml"));
        Scene catalogScene = new Scene(catalogViewLoader.load());

        ViewCatalogController viewCatalogController = catalogViewLoader.getController();
        viewCatalogController.setViewCatalogService(viewCatalogService);

        AgentLoginController loginController = loginSceneLoader.getController();
        loginController.setApplicationPrimaryStage(primaryStage);
        loginController.setLoginService(loginProxy);
        loginController.setAdministratorMainPageScene(mainPageScene);


        AgentMainPageController mainPageController = mainPageLoader.getController();
        mainPageController.setMainPageScene(mainPageScene);
        mainPageController.setLoginScene(loginScene);
        mainPageController.setViewCatalogScene(catalogScene);
        mainPageController.setApplicationPrimaryStage(primaryStage);
        mainPageController.setLoginService(loginProxy);
        mainPageController.setViewCatalogController(viewCatalogController);
        mainPageController.setViewCatalogService(viewCatalogService);
        mainPageController.setOrderService(orderService);

        loginController.setMainPageController(mainPageController);

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
}
