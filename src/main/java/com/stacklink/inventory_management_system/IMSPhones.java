package com.stacklink.inventory_management_system;

import javafx.beans.property.Property;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;

public class IMSPhones {

    public void AddNewPhoneRepaired(){
        Stage window = new Stage(StageStyle.UTILITY);

        AnchorPane root = new AnchorPane();

        IMSDatabase database = new IMSDatabase();

        DialogWindow dialog = new DialogWindow();

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(15);
        grid.setPadding(new Insets(20.0,20.0,20.0,20.0));

        Label name = new Label("Phone Name");
        name.setFont(Font.font(18.0));
        TextField phoneName = new TextField();
        phoneName.setFont(Font.font(20.0));
        grid.add(name, 0, 0);
        grid.add(phoneName,1,0);

        Label imeiL = new Label("Imei");
        imeiL.setFont(Font.font(18.0));
        TextField imeiF = new TextField();
        imeiF.setFont(Font.font(20.0));
        grid.add(imeiL, 0,1);
        grid.add(imeiF, 1,1);

        Label issue = new Label("Problem");
        issue.setFont(Font.font(18.0));
        ComboBox<String> problems = new ComboBox<>();
        problems.getItems().addAll(
                "Charging Port", "Charging Plate", "Flex Cable",
                "MouthPiece", "Earpiece", "Speaker", "Network Issues", "Water Damage",
                "Motherboard Issues", "Screen Replacement", "FRP", "Password Recovery", "Software problems",
                "Camera Lens", "Inflamed ICs", "Service", "Other"
        );
        problems.setValue("Charging Port");
        problems.setEditable(true);
        problems.setMinWidth(57);
        problems.setMinHeight(41);
        grid.add(issue,0,2);
        grid.add(problems, 1,2);

        Label cost = new Label("Cost");
        cost.setFont(Font.font(18.0));
        TextField T_cost = new TextField();
        T_cost.appendText("0");
        T_cost.setOnMouseClicked(e->T_cost.clear());
        T_cost.setPromptText("Enter cost to repair phone");
        T_cost.setFont(Font.font(20.0));
        grid.add(cost, 0,3);
        grid.add(T_cost, 1,3);

        Label paid = new Label("Paid");
        paid.setFont(Font.font(18.0));
        TextField amountPaid = new TextField();
        amountPaid.appendText("0");
        amountPaid.setOnMouseClicked(e->amountPaid.clear());
        amountPaid.setPromptText("amount paid by the client");
        amountPaid.setFont(Font.font(20.0));
        grid.add(paid, 0,4);
        grid.add(amountPaid, 1,4);

        Label profit = new Label("Profit");
        profit.setFont(Font.font(18.0));
        TextField profitGenerated = new TextField();
        profitGenerated.setFont(Font.font(18.0));
        profitGenerated.setOnMouseClicked(event -> {
            profitGenerated.clear();
            int amount_paid = Integer.parseInt(amountPaid.getText());
            int totalCost = Integer.parseInt(T_cost.getText());
            int profit1 = amount_paid - totalCost;
            profitGenerated.appendText(String.valueOf(profit1));
        });
        grid.add(profit, 0,5);
        grid.add(profitGenerated, 1,5);

        Label date = new Label("Date");
        date.setFont(Font.font(18.0));
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Date");
        datePicker.setMinWidth(50);
        datePicker.setMinHeight(41);
        grid.add(date, 0,6);
        grid.add(datePicker, 1,6);

        Label vendor = new Label("Vendor");
        vendor.setFont(Font.font(18.0));
        ComboBox<String> vendorList = new ComboBox<>();
        vendorList.getItems().addAll(
                "Delju Spares",
                "Jascom Spares",
                "HQ",
                "Changa Electronics",
                "Midlands",
                "Sam Ventures",
                "Arafat Ventures",
                "None",
                "Other"
        );
        vendorList.setValue("Delju Spares");
        vendorList.setMinWidth(57);
        vendorList.setMinHeight(41);
        vendorList.setEditable(true);
        grid.add(vendor, 0,7);
        grid.add(vendorList,1,7);

        HBox btnLayout = new HBox(30);
        Button cancel = new Button("Cancel");
        cancel.setFont(Font.font(20.0));
        cancel.setTextFill(Color.WHITE);
        cancel.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        cancel.setOnAction(e-> window.close());
        Button submit = new Button("Submit");
        submit.setFont(Font.font(20.0));
        submit.setTextFill(Color.WHITE);
        submit.setBackground(new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        submit.setOnAction(event -> {

            if (imeiF.getText().length() == 15){
                boolean insert = database.repairedPhoneData(
                        new Phone(
                                "", database.formatDate(String.valueOf(datePicker.getValue())), phoneName.getText(),
                                imeiF.getText(),problems.getValue(), T_cost.getText(),amountPaid.getText(),
                                profitGenerated.getText(),vendorList.getValue()
                        )
                );
                if(insert){
                    dialog.showDialog("Database Operations", phoneName.getText()+" Has been Inserted Successfully");
                    window.close();
                }
                else {
                    dialog.showDialog("Database Operation", "Database Operation Fail, Try Later");
                }
            }else {
                dialog.showDialog("Invalid Input", "Invalid IMEI, must be 15 chars long");
            }
        });
        btnLayout.getChildren().addAll(submit,cancel);
        grid.add(btnLayout,1,9);

        root.getChildren().add(grid);
        AnchorPane.setLeftAnchor(grid, 80.0);

        Scene scene = new Scene(root, 587, 518,true);
        window.setScene(scene);
        window.setTitle("Phone Details");
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.showAndWait();
    }

    public TableView<Phone> viewPhones(Stage stage, ObservableList<Phone> phonesObservableList){
        TableView<Phone> phonesTableView = new TableView<>();
        phonesTableView.setPlaceholder(new Label("No phones repaired"));
        phonesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        phonesTableView.prefHeightProperty().bind(stage.heightProperty());

        TableColumn<Phone, String> indexCol = new TableColumn<>("#");
        indexCol.setCellValueFactory((e) -> e.getValue().indexProperty());

        TableColumn<Phone,String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory((e) -> e.getValue().dateProperty());

        TableColumn<Phone, String> nameCol = new TableColumn<>("Phone Name");
        nameCol.setCellValueFactory((e) -> e.getValue().phoneNameProperty());

        TableColumn<Phone,String> imeiCol = new TableColumn<>("IMEI");
        imeiCol.setCellValueFactory((e) -> e.getValue().imeiProperty());

        TableColumn<Phone, String> problemCol = new TableColumn<>("Problem");
        problemCol.setCellValueFactory((e) -> e.getValue().problemProperty());

        TableColumn<Phone, String> costCol = new TableColumn<>("Cost");
        costCol.setCellValueFactory((e) -> e.getValue().costProperty());

        TableColumn<Phone, String> paid = new TableColumn<>("Paid");
        paid.setCellValueFactory((e) -> e.getValue().paidProperty());

        TableColumn<Phone, String> profitCol = new TableColumn<>("Profit");
        profitCol.setCellValueFactory((e) -> e.getValue().profitProperty());

        TableColumn<Phone, String> vendorCol = new TableColumn<>("Vendor");
        vendorCol.setCellValueFactory((e) -> e.getValue().vendorProperty());

        phonesTableView.getColumns().addAll(indexCol, dateCol, nameCol, imeiCol, problemCol, costCol, paid,
                profitCol, vendorCol);
        phonesTableView.setItems(phonesObservableList);
        return phonesTableView;
    }
}