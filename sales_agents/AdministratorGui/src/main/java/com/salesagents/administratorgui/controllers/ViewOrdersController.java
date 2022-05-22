package com.salesagents.administratorgui.controllers;

import com.salesagents.business.OrderService;
import com.salesagents.domain.models.Agent;
import com.salesagents.domain.models.Order;
import com.salesagents.domain.models.OrderDetail;
import com.salesagents.exceptions.ExceptionBaseClass;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;

public class ViewOrdersController {
    public TableView<Order> ordersTableView;
    public TableColumn<Order, Long> orderNoColumn;
    public TableColumn<Order, String> clientNameColumn;
    public TableColumn<Order, LocalDateTime> placingDateColumn;
    public TableColumn<Order, Double> totalColumn;


    public TableView<OrderDetail> productsTableView;
    public TableColumn<OrderDetail, String> idColumn;
    public TableColumn<OrderDetail, String> productNameColumn;
    public TableColumn<OrderDetail, Integer> quantityColumn;

    private OrderService orderService;

    private ObservableList<Order> orderObservableList = null;
    private ObservableList<OrderDetail> orderDetailObservableList = null;

    @FXML
    void initialize() {
        orderNoColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getOrderNumber()));
        clientNameColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getClientName()));
        placingDateColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getPlacingDate()));
        totalColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getTotal()));

        idColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getProduct().getId()));
        productNameColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getProduct().getProductName()));
        quantityColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getQuantityInOrder()));

        ordersTableView.getSelectionModel()
                .selectedItemProperty()
                .addListener(new ChangeListener<Order>() {
                    @Override
                    public void changed(ObservableValue<? extends Order> observable, Order oldValue, Order newValue) {
                        if (newValue != null) {
                            orderDetailObservableList = FXCollections.observableArrayList(newValue.getOrderDetailSet());
                            productsTableView.setItems(orderDetailObservableList);
                        }
                    }
                });
    }

    void loadOrdersToView() {
        if (orderObservableList == null) {
            try {
                var orders = orderService.getAllOrders();
                orderObservableList = FXCollections.observableArrayList(orders);
                ordersTableView.setItems(orderObservableList);
            } catch (ExceptionBaseClass exception) {
                showExceptionMessageBox(exception);
            }
        }
    }

    void clearOrdersFromView() {
        ordersTableView.setItems(FXCollections.observableArrayList());
        productsTableView.setItems(FXCollections.observableArrayList());
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    private void showExceptionMessageBox(ExceptionBaseClass exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.CLOSE);
        alert.showAndWait();
    }

}
