package com.salesagents.administratorgui.controllers;

import com.salesagents.business.administrator.services.CatalogAdministrationService;
import com.salesagents.domain.models.Agent;
import com.salesagents.domain.models.Product;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Collection;

public class CatalogAdministrationController {
    public TableColumn<Product, String> idTableColumn;
    public TableColumn<Product, String> nameTableColumn;
    public TableColumn<Product, String> typeTableColumn;
    public TableColumn<Product, String> manufacturerTableColumn;
    public TableColumn<Product, Double> priceTableColumn;
    public TableColumn<Product, Integer> quantityTableColumn;
    public TableView<Product> productTableView;
    private CatalogAdministrationService catalogService;


    private ObservableList<Product> productObservableList;

    public void setCatalogAdministrationService(CatalogAdministrationService catalogAdministrationService) {
        catalogService = catalogAdministrationService;
    }

    public void loadProductsToView() {
        if (productObservableList == null) {
            Collection<Product> allProducts = catalogService.getAll();
            productObservableList = FXCollections.observableArrayList();
            productTableView.setItems(productObservableList);
            productObservableList.setAll(allProducts);
        }
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
}
