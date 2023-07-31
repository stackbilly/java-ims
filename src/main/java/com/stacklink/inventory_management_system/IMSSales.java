package com.stacklink.inventory_management_system;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class IMSSales {

    IMSDatabase database = new IMSDatabase();
    DialogWindow dialog = new DialogWindow();

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

    public void cancelSale(){
        AnchorPane root = new AnchorPane();

        Stage window = new Stage(StageStyle.UTILITY);

        GridPane grid = new GridPane();
        grid.setVgap(12.0);
        grid.setHgap(12.0);
        grid.setPadding(new Insets(25.0,25.0,25.0,25.0));

        AnchorPane.setLeftAnchor(grid, 20.0);

        Label saleIDL = new Label("Sale ID");
        saleIDL.setFont(Font.font(15.0));
        grid.add(saleIDL, 0,0);
        TextField saleIDF = new TextField();
        saleIDF.setFont(Font.font(15.0));
        grid.add(saleIDF, 1,0);

        Label codeL = new Label("Code");
        codeL.setFont(Font.font(15.0));
        grid.add(codeL, 0,1);
        TextField codeF = new TextField();
        codeF.setFont(Font.font(15.0));
        codeF.setEditable(false);
        grid.add(codeF, 1,1);

        Label nameL = new Label("Name");
        nameL.setFont(Font.font(15.0));
        grid.add(nameL, 0,2);
        TextField nameF = new TextField();
        nameF.setFont(Font.font(15.0));
        nameF.setEditable(false);
        grid.add(nameF, 1,2);

        Label quantityL = new Label("Quantity");
        quantityL.setFont(Font.font(15.0));
        grid.add(quantityL, 0,3);
        TextField quantityF = new TextField();
        quantityF.setFont(Font.font(15.0));
        grid.add(quantityF, 1,3);

        Label priceL = new Label("Price");
        priceL.setFont(Font.font(15.0));
        grid.add(priceL, 0,4);
        TextField priceF = new TextField();
        priceF.setFont(Font.font(15.0));
        grid.add(priceF, 1,4);

        Label dateL = new Label("Date");
        dateL.setFont(Font.font(15.0));
        grid.add(dateL, 0,5);
        TextField dateF = new TextField();
        dateF.setFont(Font.font(15.0));
        dateF.setEditable(false);
        grid.add(dateF, 1,5);
        saleIDF.setOnAction((e) -> {
            ArrayList<String> sale = database.getSale(Integer.parseInt(saleIDF.getText()));
            codeF.setText(sale.get(1));
            nameF.setText(sale.get(2));
            quantityF.setText(sale.get(3));
            priceF.setText(sale.get(4));
            dateF.setText(sale.get(5));
        });

        Label mLabel = new Label("Mark Sale as: ");
        mLabel.setFont(Font.font("Impact", FontWeight.SEMI_BOLD, 18.0));
        AnchorPane.setLeftAnchor(mLabel,100.0);
        AnchorPane.setBottomAnchor(mLabel, 140.0);

        HBox hButtons = new HBox(15.0);
        hButtons.setPadding(new Insets(20.0,20.0,20.0,20.0));
        AnchorPane.setBottomAnchor(hButtons, 70.0);
        AnchorPane.setLeftAnchor(hButtons, 70.0);

        Button asReturn = new Button("RETURN");
        asReturn.setTextFill(Color.WHITE);
        asReturn.setFont(Font.font(17.0));
        asReturn.setBackground(new Background(new BackgroundFill(Color.CRIMSON, CornerRadii.EMPTY, Insets.EMPTY)));
        asReturn.setOnAction((e) -> {
            if (database.updateProduct(nameF.getText(), Integer.parseInt(quantityF.getText()))){
                dialog.showDialog("Success", quantityF.getText()+" units of "+nameF.getText()+" restocked successfully");
                database.deleteTask("code", codeF.getText(), "Sales");
                window.close();
            }
        });

        Button asLoss = new Button("LOSS");
        asLoss.setFont(Font.font(17.0));
        asLoss.setTextFill(Color.WHITE);
        asLoss.setBackground(new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        hButtons.getChildren().addAll(asReturn, asLoss);
        asLoss.setOnAction((e) -> {
            int cost = Integer.parseInt(priceF.getText()) - Integer.parseInt(database.getSale(Integer.parseInt(saleIDF.getText())).get(6));
            if(database.updateSale(Integer.parseInt(saleIDF.getText()), cost)){
                dialog.showDialog("Success", "Sale no "+saleIDF.getText()+" for product "+nameF.getText()+" marked as loss of "+cost);
                window.close();
            }
        });

        root.getChildren().addAll(grid, mLabel, hButtons);
        Scene scene = new Scene(root, 380,450);
        window.setScene(scene);
        window.setTitle("Cancel Sale");
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
        window.showAndWait();
    }
}
