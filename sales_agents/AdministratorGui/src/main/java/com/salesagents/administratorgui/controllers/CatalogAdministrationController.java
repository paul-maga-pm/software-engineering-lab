package com.salesagents.administratorgui.controllers;

import com.salesagents.administratorgui.AdministratorGuiFxApplication;
import com.salesagents.business.administrator.services.CatalogAdministrationService;
import com.salesagents.domain.models.Agent;
import com.salesagents.domain.models.Product;
import com.salesagents.exceptions.ExceptionBaseClass;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Collection;

public class CatalogAdministrationController {
    public TableColumn<Product, String> idTableColumn;
    public TableColumn<Product, String> nameTableColumn;
    public TableColumn<Product, String> typeTableColumn;
    public TableColumn<Product, String> manufacturerTableColumn;
    public TableColumn<Product, Double> priceTableColumn;
    public TableColumn<Product, Integer> quantityTableColumn;
    public TableView<Product> productTableView;
    public TextField selectedProductId;
    public TextField idField;
    public TextField nameField;
    public TextField typeField;
    public TextField manufacturerField;
    public TextField priceField;
    public TextField quantityField;
    public Button saveButton;
    public Button updatePriceButton;
    public TextField priceUpdateField;
    public TextField updateQuantityField;
    public Button updateQuantityButton;
    public Button deleteButton;
    private CatalogAdministrationService catalogService;


    private ObservableList<Product> productObservableList;

    public void setCatalogAdministrationService(CatalogAdministrationService catalogAdministrationService) {
        catalogService = catalogAdministrationService;
    }

    public void loadProductsToView() {
        if (productObservableList == null) {
            productObservableList = FXCollections.observableArrayList();
            productTableView.setItems(productObservableList);
            try {
                Collection<Product> allProducts = catalogService.getAll();
                productObservableList.setAll(allProducts);
            } catch (ExceptionBaseClass exception) {
                showExceptionMessageBox(exception);
            }
        }
    }

    public void clearProductsFromView() {
        productObservableList = null;
        productTableView.setItems(FXCollections.observableArrayList());
    }

    @FXML
    void initialize() {
        idTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getId()));
        nameTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getProductName()));
        typeTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getType()));
        manufacturerTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getManufacturer()));
        priceTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getPrice()));
        quantityTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getQuantityInStock()));

        productTableView.getSelectionModel()
                .selectedItemProperty()
                .addListener(new ChangeListener<Product>() {
                    @Override
                    public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
                        if (observable.getValue() != null)
                            selectedProductId.setText(observable.getValue().getId());
                    }
                });

    }

    public void handleClickOnSaveButton(ActionEvent event) {
        String id = idField.getText().strip();
        String name = nameField.getText().strip();
        String manufacturer = manufacturerField.getText().strip();
        String type = typeField.getText().strip();
        String priceStr = priceField.getText().strip();
        String quantityStr = quantityField.getText().strip();

        double price;
        int quantity;

        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException exception) {
            showExceptionMessageBox("Invalid numeric value for price");
            return;
        }

        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException exception) {
            showExceptionMessageBox("Invalid numeric value for quantity");
            return;
        }

        Product product = new Product.ProductBuilder()
                .setId(id)
                .setProductName(name)
                .setManufacturer(manufacturer)
                .setType(type)
                .setPrice(price)
                .setQuantityInStock(quantity)
                .build();

        try {
            boolean resultOfAdding = catalogService.add(product);

            if (resultOfAdding) {
                showMessageBox("Product saved");

                Platform.runLater(() -> {
                    productObservableList.add(product);
                });
            }
            else
                showExceptionMessageBox("Couldn't save the product");

        } catch (ExceptionBaseClass exceptionBaseClass) {
            showExceptionMessageBox(exceptionBaseClass);
        }
    }

    private void showMessageBox(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.CLOSE);
        alert.showAndWait();
    }

    private void showExceptionMessageBox(ExceptionBaseClass exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.CLOSE);
        alert.showAndWait();
    }

    private void showExceptionMessageBox(String exceptionMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR, exceptionMessage, ButtonType.CLOSE);
        alert.showAndWait();
    }

    public void handleClickOnUpdatePriceButton(ActionEvent event) {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null)
            return;

        String priceStr = priceUpdateField.getText();
        double newPrice;

        try {
            newPrice = Double.parseDouble(priceStr);
        } catch (NumberFormatException exception) {
            showExceptionMessageBox("Invalid numeric value for price");
            return;
        }

        try {
            selectedProduct.setPrice(newPrice);

            boolean resultOfUpdate = catalogService.update(selectedProduct);

            if (resultOfUpdate) {
                showMessageBox("Price updated");

                Platform.runLater(() -> {
                    productTableView.refresh();
                });
            } else
                showExceptionMessageBox("Couldn't update the price");
        } catch (ExceptionBaseClass exceptionBaseClass) {
            showExceptionMessageBox(exceptionBaseClass);
        }

    }

    public void handleClickOnUpdateQuantityButton(ActionEvent event) {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null)
            return;

        String quantityStr = updateQuantityField.getText();
        int newQuantity;

        try {
            newQuantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException exception) {
            showExceptionMessageBox("Invalid numeric value for quantity");
            return;
        }

        try {
            selectedProduct.setQuantityInStock(newQuantity);

            boolean resultOfUpdate = catalogService.update(selectedProduct);

            if (resultOfUpdate) {
                showMessageBox("Quantity updated");

                Platform.runLater(() -> {
                    productTableView.refresh();
                });
            } else
                showExceptionMessageBox("Couldn't update the quantity");
        } catch (ExceptionBaseClass exceptionBaseClass) {
            showExceptionMessageBox(exceptionBaseClass);
        }
    }

    public void handleClickOnDeleteButton(ActionEvent event) {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null)
            return;

        String productId = selectedProduct.getId();

        try {
            boolean resultOfRemove = catalogService.remove(productId);
            if (resultOfRemove) {
                showMessageBox("Product has been removed from catalog");

                Platform.runLater(() -> {
                    productObservableList.remove(selectedProduct);
                    productTableView.refresh();
                });
            } else
                showExceptionMessageBox("Couldn't remove the product");
        } catch (ExceptionBaseClass exception) {
            showExceptionMessageBox(exception);
        }
    }
}
