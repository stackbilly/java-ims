package com.stacklink.inventory_management_system;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;

public class IMSExpense {
    IMSDatabase database = new IMSDatabase();
    DialogWindow dialog = new DialogWindow();

    Button delete;

    public AnchorPane addExpense(){
        AnchorPane root = new AnchorPane();

        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(10);
        grid.setPadding(new Insets(20.0,20.0,20.0,20.0));

        Label name = new Label("Expense Name");
        name.setFont(Font.font(18.0));
        ComboBox<String> expenseName = new ComboBox<>();
        expenseName.getItems().addAll(database.getCategoryData("Expense"));
        expenseName.setEditable(true);
        expenseName.setMinHeight(41);
        expenseName.setMinWidth(50);
        expenseName.setPromptText("Select One");
        grid.add(name,0,0);
        grid.add(expenseName,1,0);

        Label cost = new Label("Cost");
        cost.setFont(Font.font(18.0));
        TextField totalCost = new TextField();
        totalCost.appendText("0");
        totalCost.setMaxWidth(170);
        totalCost.setOnMouseClicked(e->totalCost.clear());
        totalCost.setFont(Font.font(20.0));
        grid.add(cost,0,1);
        grid.add(totalCost, 1,1);

        Label date = new Label("Date");
        date.setFont(Font.font(18.0));
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.setMinWidth(50);
        datePicker.setMinHeight(41);
        grid.add(date, 0,2);
        grid.add(datePicker, 1,2);

        HBox btnLayout = new HBox(30);
        Button submit = new Button("Submit");
        submit.setFont(Font.font(20.0));
        submit.setTextFill(Color.WHITE);
        submit.setBackground(new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        submit.setOnAction(event -> {
            boolean isAdded = database.addExpense(new Expenses(
                    "", "",
                    String.valueOf(datePicker.getValue()),
                            expenseName.getValue(),
                            totalCost.getText()
            ));
            if (isAdded){
                dialog.showDialog("Expenses Addition", expenseName.getValue()+" expense has been added successfully");
            }else {
                dialog.showDialog("Expense Addition", expenseName.getValue()+" expense addition FAIL!");
            }
        });
        delete = new Button("Delete");
        delete.setFont(Font.font(20.0));
        delete.setTextFill(Color.WHITE);
        delete.setBackground(new Background(new BackgroundFill(Color.CRIMSON, CornerRadii.EMPTY, Insets.EMPTY)));
        btnLayout.getChildren().addAll(submit, delete);
        grid.add(btnLayout, 1,3);

        AnchorPane.setLeftAnchor(grid, 40.0);
        root.getChildren().add(grid);
        root.setPrefHeight(300.0);
        root.setPrefWidth(499);
        return root;
    }

    public TableView<Expenses> viewExpenses(Stage stage, ObservableList<Expenses> list){
        TableView<Expenses> expensesTableView = new TableView<>();
        expensesTableView.setPlaceholder(new Label("No expenses"));
        expensesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        expensesTableView.prefHeightProperty().bind(stage.heightProperty());
        expensesTableView.setPrefWidth(500.0);

        TableColumn<Expenses, String> indexCol = new TableColumn<>("#");
        indexCol.setCellValueFactory((e) -> e.getValue().indexProperty());
        indexCol.setPrefWidth(100);

        TableColumn<Expenses, String> idCol = new TableColumn<>("Expense ID");
        idCol.setCellValueFactory((e) -> e.getValue().expenseIDProperty());

        TableColumn<Expenses, String> nameCol = new TableColumn<>("Expense Name");
        nameCol.setCellValueFactory((e) -> e.getValue().expenseNameProperty());

        TableColumn<Expenses, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory((e) -> e.getValue().dateProperty());

        TableColumn<Expenses, String> costCol = new TableColumn<>("Cost");
        costCol.setCellValueFactory((e) -> e.getValue().costProperty());

        expensesTableView.getColumns().addAll(indexCol, idCol, dateCol, nameCol, costCol);

        delete.setOnAction((e) -> {
            String expenseID = expensesTableView.getSelectionModel().getSelectedItem().getExpenseID();
            if (database.deleteExpense(expenseID)){
                dialog.showDialog("Information!", "Expense deleted successfully");
                expensesTableView.getItems().remove(expensesTableView.getSelectionModel().getSelectedItem());
            }
        });

        expensesTableView.setItems(list);
        return expensesTableView;
    }
}
