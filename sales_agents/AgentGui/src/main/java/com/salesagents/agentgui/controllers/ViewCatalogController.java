package com.salesagents.agentgui.controllers;

import com.salesagents.business.OrderService;
import com.salesagents.business.agent.services.ViewCatalogService;
import com.salesagents.business.utils.ProductObserver;
import com.salesagents.domain.models.Agent;
import com.salesagents.domain.models.Order;
import com.salesagents.domain.models.OrderDetail;
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

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

public class ViewCatalogController implements ProductObserver {
    public TableView<Product> productTableView;
    public TableColumn<Product, String> idTableColumn;
    public TableColumn<Product, String> nameTableColumn;
    public TableColumn<Product, String> typeTableColumn;
    public TableColumn<Product, String> manufacturerTableColumn;
    public TableColumn<Product, Double> priceTableColumn;
    public TableColumn<Product, Integer> quantityTableColumn;
    public Button addDetailToOrderButton;
    public Button placeOrderButton;
    public TextField selectedProductIdField;
    public TextField quantityField;
    public TextField clientNameField;
    public TableView<OrderDetail> orderDetailTableView;
    public TableColumn<OrderDetail, String> productIdInDetail;
    public TableColumn<OrderDetail, Integer> productQuantityInOrder;

    public ObservableList<OrderDetail> orderDetailsObservableList = null;


    private ViewCatalogService viewCatalogService;
    private Agent loggedAgent;

    private ObservableList<Product> productObservableList = null;
    private OrderService orderService;

    private Order currentOrder = null;


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

        productTableView.getSelectionModel()
                .selectedItemProperty()
                .addListener(new ChangeListener<Product>() {
                    @Override
                    public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
                        if (newValue != null) {
                            selectedProductIdField.setText(newValue.getId());
                        }
                    }
                });

        productIdInDetail.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getProduct().getId()));
        productQuantityInOrder.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getQuantityInOrder()));
    }

    public void clearProductsFromTableView() {
        productObservableList = null;
        productTableView.setItems(FXCollections.observableArrayList());

        currentOrder = null;
        orderDetailsObservableList = null;
        orderDetailTableView.setItems(FXCollections.observableArrayList());
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


    public void handleClickOnAddDetail(ActionEvent event) {
        Platform.runLater(() -> {
            if (currentOrder == null)
                currentOrder = new Order();

            if (orderDetailsObservableList == null) {
                orderDetailsObservableList = FXCollections.observableArrayList();
                orderDetailTableView.setItems(orderDetailsObservableList);
            }

            String id = selectedProductIdField.getText();
            Product selectedProduct = productTableView.getItems()
                    .stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .get();

            int selectedQuantity = Integer.parseInt(quantityField.getText());

            if (selectedQuantity >  selectedProduct.getQuantityInStock()) {
                showExceptionMessageBox(new ExceptionBaseClass("Invalid quantity"));
                return;
            }

            OrderDetail detail = new OrderDetail(selectedProduct, selectedQuantity);
            currentOrder.addOrderDetail(detail);

            if (orderDetailsObservableList.contains(detail)) {
                int index = orderDetailsObservableList.indexOf(detail);
                int oldQuant = orderDetailsObservableList.get(index).getQuantityInOrder();
                orderDetailsObservableList.get(index).setQuantityInOrder(oldQuant + detail.getQuantityInOrder());
                orderDetailTableView.refresh();
            } else orderDetailsObservableList.add(detail);
            productTableView.refresh();
        });
    }

    public void handleClickOnPlaceOrderButton(ActionEvent event) {
        String clientName = clientNameField.getText().strip();

        if (Objects.equals(clientName, "")) {
            showExceptionMessageBox(new ExceptionBaseClass("Invalid client name"));
            return;
        }
        currentOrder.setPlacingDate(LocalDateTime.now());
        currentOrder.setClientName(clientName);
        currentOrder.setAgent(loggedAgent);

        try {
            orderService.placeOrder(currentOrder);
            currentOrder = null;
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order has been placed", ButtonType.CLOSE);
            alert.showAndWait();
            orderDetailTableView.setItems(FXCollections.observableArrayList());
        } catch (ExceptionBaseClass exception) {
            showExceptionMessageBox(exception);
        }
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
