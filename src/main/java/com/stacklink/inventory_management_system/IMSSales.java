package com.stacklink.inventory_management_system;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class IMSSales {

    public TableView<Sales> viewSales(Stage stage, ObservableList<Sales> salesObservableList){
        TableView<Sales> salesTableView = new TableView<>();
        salesTableView.setPlaceholder(new Label("No new sales available"));
        salesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        salesTableView.prefHeightProperty().bind(stage.heightProperty());

        TableColumn<Sales, String> indexCol = new TableColumn<>("#");
        indexCol.setCellValueFactory((e) -> e.getValue().indexProperty());

        TableColumn<Sales,String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory((e) -> e.getValue().dateProperty());

        TableColumn<Sales, String> saleIDCol = new TableColumn<>("Sale ID");
        saleIDCol.setCellValueFactory((e) -> e.getValue().saleIDProperty());

        TableColumn<Sales,String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory((e) -> e.getValue().codeProperty());

        TableColumn<Sales, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory((e) -> e.getValue().nameProperty());

        TableColumn<Sales, String> quantity = new TableColumn<>("Quantity");
        quantity.setCellValueFactory((e) -> e.getValue().quantityProperty());

        TableColumn<Sales, String> price = new TableColumn<>("Price");
        price.setCellValueFactory((e) -> e.getValue().priceProperty());

        TableColumn<Sales, String> profitCol = new TableColumn<>("Profit");
        profitCol.setCellValueFactory((e) -> e.getValue().profitProperty());

        salesTableView.getColumns().addAll(indexCol, dateCol, saleIDCol, codeCol, nameCol, quantity, price, profitCol);
        salesTableView.setItems(salesObservableList);
        return salesTableView;
    }
}
