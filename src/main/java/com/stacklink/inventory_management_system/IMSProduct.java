package com.stacklink.inventory_management_system;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;

public class IMSProduct {

    IMSDatabase database = new IMSDatabase();
    DialogWindow dialog = new DialogWindow();

    public  void AddNewProduct(){
        Stage window = new Stage(StageStyle.UTILITY);
        AnchorPane root = new AnchorPane();

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25.0,25.0,25.0,25.0));

        Label _productNameL = new Label("Product name");
        _productNameL.setFont(Font.font(18.0));
        grid.add(_productNameL, 0,0);
        TextField _productNameF = new TextField();
        _productNameF.setFont(Font.font(20.0));
        grid.add(_productNameF, 1,0);

        Label _descriptionL = new Label("Description");
        _descriptionL.setFont(Font.font(18.0));
        grid.add(_descriptionL, 0, 1);
        TextArea descriptionF = new TextArea();
        descriptionF.setFont(Font.font(20.0));
        descriptionF.setPrefHeight(30.0);
        descriptionF.setPrefWidth(_productNameF.getWidth());
        grid.add(descriptionF, 1,1);

        Label _productCodeL = new Label("Product code");
        _productCodeL.setFont(Font.font(18.0));
        grid.add(_productCodeL, 0, 2);
        TextField _productCodeF = new TextField();
        _productCodeF.setFont(Font.font(20.0));
        grid.add(_productCodeF, 1,2);

        Label categoryL = new Label("Category");
        categoryL.setFont(Font.font(18.0));
        grid.add(categoryL, 0,3);
        ComboBox<String> category = new ComboBox<>();
        category.getItems().addAll(database.getCategoryData());
        category.setValue("Select One");
        category.setEditable(false);
        category.setMinWidth(57);
        category.setMinHeight(41);
        grid.add(category, 1,3);

        Label quantityL = new Label("Quantity");
        quantityL.setFont(Font.font(18.0));
        grid.add(quantityL,0,4);
        TextField quantityF = new TextField();
        quantityF.setFont(Font.font(20.0));
        grid.add(quantityF, 1,4);

        Label costPriceL = new Label("Cost price");
        costPriceL.setFont(Font.font(18.0));
        grid.add(costPriceL, 0,5);
        TextField costPriceF = new TextField();
        costPriceF.setFont(Font.font(20.0));
        grid.add(costPriceF, 1,5);

        Label salePriceL = new Label("Sale price");
        salePriceL.setFont(Font.font(18.0));
        grid.add(salePriceL, 0,6);
        TextField salePriceF = new TextField();
        salePriceF.setFont(Font.font(20.0));
        grid.add(salePriceF, 1,6);

        Label dateAddedL = new Label("Date Added");
        dateAddedL.setFont(Font.font(18.0));
        grid.add(dateAddedL, 0,7);
        DatePicker date = new DatePicker();
        date.setValue(LocalDate.now());
        date.setMinWidth(50);
        date.setMinHeight(41);
        grid.add(date, 1,7);

        Button submit = new Button("ADD");
        submit.setTextFill(Color.WHITE);
        submit.setFont(Font.font(20.0));
        submit.setOnAction(e -> {
            Product product = new Product("",_productNameF.getText(), descriptionF.getText(), _productCodeF.getText(),
                    category.getValue(),Integer.parseInt(quantityF.getText()), Integer.parseInt(costPriceF.getText()),
                    Integer.parseInt(salePriceF.getText()),
                    String.valueOf(date.getValue()), 0,0);
            if(database.addProduct(product))
            {
                dialog.showDialog("Alert", product.getProductName()+" added successfully");
                _productNameF.clear();
                _productCodeF.clear();
                quantityF.clear();
                costPriceF.clear();
                salePriceF.clear();
                descriptionF.clear();
            }
            else
                dialog.showDialog("Exception", "Error occurred while adding "+product.getProductName());
        });
        submit.setBackground(new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        Button cancel = new Button("CANCEL");
        cancel.setFont(Font.font(20.0));
        cancel.setTextFill(Color.WHITE);
        cancel.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        cancel.setOnAction(e -> window.close());

        HBox btnBox = new HBox(50, submit, cancel);
        grid.add(btnBox, 1,8);
        root.getChildren().add(grid);
        AnchorPane.setLeftAnchor(grid, 80.0);

        Scene scene = new Scene(root, 587,550,true);
        window.setScene(scene);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("New Product");
        window.setResizable(false);
        window.showAndWait();
    }

    public TableView<Product> viewProducts(){
        TableView<Product> productTable = new TableView<>();
        productTable.setPlaceholder(new Label("Zero products are currently available"));
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        productTable.setEditable(true);

        TableColumn<Product, String> indexCol = new TableColumn<>("Table Id");
        indexCol.setCellValueFactory((e) -> e.getValue().indexProperty());

        TableColumn<Product, String> name = new TableColumn<>("Product Name");
        name.setCellValueFactory((e) -> e.getValue().productNameProperty());
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(e -> {
            if (database.updateProduct("Product","name", e.getOldValue(), e.getNewValue()))
                dialog.showDialog("Success", e.getOldValue()+ " updated to "+e.getNewValue()+" successfully");
        });

        TableColumn<Product, String> description = new TableColumn<>("Description");
        description.setCellValueFactory((e) -> e.getValue().descriptionProperty());
        description.setCellFactory(TextFieldTableCell.forTableColumn());
        description.setOnEditCommit(e -> {
            if(database.updateProduct("Product","description", e.getOldValue(), e.getNewValue()))
                dialog.showDialog("Success", e.getOldValue()+" updated to "+e.getNewValue()+" successfully");
        });

        TableColumn<Product, String> code = new TableColumn<>("Product Code");
        code.setCellValueFactory((e) -> e.getValue().productCodeProperty());
        code.setCellFactory(TextFieldTableCell.forTableColumn());
        code.setOnEditCommit(e -> {
            if(database.updateProduct("Product","code", e.getOldValue(), e.getNewValue()))
                dialog.showDialog("Success", e.getOldValue()+ " updated to "+e.getNewValue()+" successfully");
        });

        TableColumn<Product, String> category = new TableColumn<>("Category");
        category.setCellValueFactory((e) -> e.getValue().categoryProperty());

        TableColumn<Product, String> quantity = new TableColumn<>("Quantity");
        quantity.setCellValueFactory((e) -> e.getValue().quantityProperty());
        quantity.setCellFactory(TextFieldTableCell.forTableColumn());
        quantity.setOnEditCommit(e -> {
            if(database.updateProduct("Product","quantity", Integer.parseInt(e.getOldValue()),
                    Integer.parseInt(e.getNewValue())))
                dialog.showDialog("Success", e.getOldValue()+ " updated to "+e.getNewValue()+" successfully");
        });

        TableColumn<Product, String> cost = new TableColumn<>("Cost Price");
        cost.setCellValueFactory((e) -> e.getValue().costPriceProperty());

        TableColumn<Product, String> sale = new TableColumn<>("Sale Price");
        sale.setCellValueFactory((e) -> e.getValue().salePriceProperty());
        sale.setCellFactory(TextFieldTableCell.forTableColumn());
        sale.setOnEditCommit((e) -> {
            if(database.updateProduct("Product", "sale", Integer.parseInt(e.getOldValue()),
                    Integer.parseInt(e.getNewValue())))
                dialog.showDialog("Success", e.getOldValue()+" updated to "+e.getNewValue()+" successfully");
        });

        TableColumn<Product, String> dateAdded = new TableColumn<>("Date Added");
        dateAdded.setCellValueFactory((e) -> e.getValue().dateAddedProperty());

        TableColumn<Product, String> total = new TableColumn<>("Total Cost");
        total.setCellValueFactory((e) -> e.getValue().totalProperty());

        TableColumn<Product, String> sales = new TableColumn<>("Expected Sales");
        sales.setCellValueFactory((e) -> e.getValue().expectedSalesProperty());
        productTable.editableProperty().setValue(true);

        productTable.getColumns().addAll(indexCol, name, description, code, category, quantity, cost, sale,total, sales);
        productTable.setItems(database.getProducts());

        return productTable;
    }
}
