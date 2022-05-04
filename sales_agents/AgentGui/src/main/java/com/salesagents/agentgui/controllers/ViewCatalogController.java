package com.salesagents.agentgui.controllers;

import com.salesagents.business.agent.services.ViewCatalogService;
import com.salesagents.business.utils.ProductObserver;
import com.salesagents.domain.models.Agent;
import com.salesagents.domain.models.Product;
import com.salesagents.exceptions.ExceptionBaseClass;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Collection;

public class ViewCatalogController implements ProductObserver {
    public TableView<Product> productTableView;
    public TableColumn<Product, String> idTableColumn;
    public TableColumn<Product, String> nameTableColumn;
    public TableColumn<Product, String> typeTableColumn;
    public TableColumn<Product, String> manufacturerTableColumn;
    public TableColumn<Product, Double> priceTableColumn;
    public TableColumn<Product, Integer> quantityTableColumn;

    private ViewCatalogService viewCatalogService;
    private Agent loggedAgent;

    private ObservableList<Product> productObservableList = null;

    public void setViewCatalogService(ViewCatalogService viewCatalogService) {
        this.viewCatalogService = viewCatalogService;
    }

    @FXML
    void initialize() {
        idTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getId()));
        nameTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getProductName()));
        typeTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getType()));
        manufacturerTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getManufacturer()));
        priceTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getPrice()));
        quantityTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getQuantityInStock()));
    }

    public void clearProductsFromTableView() {
        productObservableList = null;
        productTableView.setItems(FXCollections.observableArrayList());
    }

    public void loadProductsToView() {
        if (productObservableList == null) {
            productObservableList = FXCollections.observableArrayList();
            productTableView.setItems(productObservableList);
            try {
                Collection<Product> allProducts = viewCatalogService.getAllProducts();
                productObservableList.setAll(allProducts);
            } catch (ExceptionBaseClass exception) {
                showExceptionMessageBox(exception);
            }
        }
    }

    private void showExceptionMessageBox(ExceptionBaseClass exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.CLOSE);
        alert.showAndWait();
    }

    public void setLoggedAgent(Agent loggedAgent) {
        this.loggedAgent = loggedAgent;
    }

    @Override
    public void productWasAdded(Product newProduct) {
        Platform.runLater(() -> {
            productObservableList.add(newProduct);
        });
    }

    @Override
    public void productWasUpdated(Product newValueOfProduct) {
        Platform.runLater(() -> {
            for(int i = 0; i < productObservableList.size(); i++) {
                String crtId = productObservableList.get(i).getId();
                if (newValueOfProduct.getId().equals(crtId)) {
                    productObservableList.set(i, newValueOfProduct);
                    break;
                }
            }
            productTableView.refresh();
        });
    }

    @Override
    public void productWasRemoved(String idOfRemovedProduct) {
        Platform.runLater(() -> {
            productObservableList.removeIf(p -> p.getId().equals(idOfRemovedProduct));
            productTableView.refresh();
        });
    }
}
