package com.salesagents.agentgui;

import com.salesagents.agentgui.controllers.AgentLoginController;
import com.salesagents.agentgui.controllers.AgentMainPageController;
import com.salesagents.business.agent.services.AgentLoginService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AgentGuiFxApplication extends Application {
    private static AgentLoginService loginService;

    public static void setLoginService(AgentLoginService loginService) {
        AgentGuiFxApplication.loginService = loginService;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loginSceneLoader = new FXMLLoader(AgentGuiFxApplication.class.getResource("agent-login-view.fxml"));
        Scene loginScene = new Scene(loginSceneLoader.load());

        FXMLLoader mainPageLoader = new FXMLLoader(AgentGuiFxApplication.class.getResource("agent-main-page-view.fxml"));
        Scene mainPageScene = new Scene(mainPageLoader.load());

        AgentLoginController loginController = loginSceneLoader.getController();
        loginController.setApplicationPrimaryStage(primaryStage);
        loginController.setLoginService(loginService);
        loginController.setAdministratorMainPageScene(mainPageScene);

        AgentMainPageController mainPageController = mainPageLoader.getController();
        mainPageController.setMainPageScene(mainPageScene);
        mainPageController.setLoginScene(loginScene);
        mainPageController.setApplicationPrimaryStage(primaryStage);
        mainPageController.setLoginService(loginService);

        loginController.setMainPageController(mainPageController);

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
}
