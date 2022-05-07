package com.salesagents.administratorgui.controllers;

import com.salesagents.administratorgui.AdministratorGuiFxApplication;
import com.salesagents.business.administrator.services.AgentsAdministrationService;
import com.salesagents.domain.models.Agent;
import com.salesagents.exceptions.ExceptionBaseClass;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Collection;

public class AgentsAdministrationController {

    @FXML
    public TextField agentNameTextField;
    @FXML
    public TextField agentUsernameTextField;
    @FXML
    public TextField agentPasswordTextField;
    @FXML
    public Button registerAgentButton;

    @FXML
    public TableColumn<Agent, String> agentNameTableColumn;
    @FXML
    public TableColumn<Agent, String> agentUsernameTableColumn;
    @FXML
    public TableView<Agent> agentTableView;

    private AgentsAdministrationService agentAdministrationService;

    private ObservableList<Agent> agentObservableList = null;


    @FXML
    void initialize() {
        agentNameTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getName()));
        agentUsernameTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getUsername()));
    }

    public void loadAgentsToView() {
        if (agentObservableList == null) {
            Collection<Agent> allAgents = agentAdministrationService.getAllAgents();
            agentObservableList = FXCollections.observableArrayList();
            agentTableView.setItems(agentObservableList);
            agentObservableList.setAll(allAgents);
        }
    }

    public void clearAgentsFromView() {
        agentObservableList = null;
        agentTableView.setItems(FXCollections.observableArrayList());
    }

    public void setAgentsAdministrationService(AgentsAdministrationService agentAdministrationService) {
        this.agentAdministrationService = agentAdministrationService;
    }

    @FXML
    public void handleClickOnRegisterAgentButton(ActionEvent event) {
        String name = agentNameTextField.getText().strip();
        String username = agentUsernameTextField.getText().strip();
        String password = agentPasswordTextField.getText().strip();

        try {
            Agent agent = new Agent(name, username, password);
            agentAdministrationService.addAgent(agent);
            agentObservableList.add(agent);
        } catch (ExceptionBaseClass exception) {
            showExceptionMessageBox(exception);
        }
    }

    private void showExceptionMessageBox(ExceptionBaseClass exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.CLOSE);
        alert.showAndWait();
    }
}
