package com.stacklink.inventory_management_system;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Objects;

public class IMSCategory {

    IMSDatabase database = new IMSDatabase();
    DialogWindow dialog = new DialogWindow();
    public AnchorPane categoryRoot(){
        AnchorPane root = new AnchorPane();

        ImageView icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.
                getResourceAsStream("/assets/category.png"))));
        icon.setFitWidth(35);
        icon.setFitHeight(35);
        Label productL = new Label("CATEGORIES", icon);
        productL.setContentDisplay(ContentDisplay.LEFT);
        productL.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 15));
        HBox hRight = new HBox(productL);
        hRight.setAlignment(Pos.CENTER_LEFT);
        hRight.setPadding(new Insets(25.0,25.0,25.0,25.0));

        Button addCategory = new Button("ADD CATEGORY");
        addCategory.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 16));
        addCategory.setTextFill(Color.WHITE);
        addCategory.setBackground(new Background(new BackgroundFill(Color.CRIMSON, CornerRadii.EMPTY, Insets.EMPTY)));
        addCategory.setOnAction(e -> addCategoryInterface());
        HBox hLeft = new HBox(addCategory);
        AnchorPane.setRightAnchor(hLeft,12.0);
        AnchorPane.setTopAnchor(hLeft, 6.0);
        hLeft.setPadding(new Insets(25.0,25.0,25.0,25.0));

        TableView<Category> categoryTable = new TableView<>();

        TableColumn<Category, String> indexCol = new TableColumn<>("#");
        indexCol.setCellValueFactory((e) -> e.getValue().getIndexProperty());
        indexCol.setPrefWidth(130.0);

        TableColumn<Category, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory((e) -> e.getValue().categoryNameProperty());
        categoryColumn.setPrefWidth(135.0);
//        categoryColumn.setResizable(true);

        TableColumn<Category, String> status = new TableColumn<>("Status");
        status.setCellValueFactory((e) -> e.getValue().statusProperty());
        status.setPrefWidth(135);
//        status.setEditable(true);
        status.setResizable(false);
        status.setOnEditCommit((e) -> dialog.showDialog("Try", "You are trying to edit me"));

        categoryTable.getColumns().addAll(indexCol, categoryColumn, status);
        categoryTable.itemsProperty().setValue(database.getCategories());
        categoryTable.setMinSize(400,100);
        categoryTable.setMaxWidth(400);
        categoryTable.setEditable(true);


        HBox tableBox = new HBox(25.0);
        tableBox.getChildren().addAll(addCategoryInterface(),categoryTable);
        tableBox.setAlignment(Pos.CENTER);

        AnchorPane.setTopAnchor(tableBox, 100.0);

        AnchorPane htopRoot = new AnchorPane(hRight,hLeft);
        htopRoot.setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        AnchorPane.setTopAnchor(htopRoot, 0.0);
        AnchorPane.setLeftAnchor(htopRoot, 0.0);
        AnchorPane.setRightAnchor(htopRoot, 0.0);

        root.getChildren().addAll(htopRoot, tableBox);

        return root;
    }
    public AnchorPane addCategoryInterface(){
        AnchorPane root = new AnchorPane();

        GridPane grid = new GridPane();
        grid.setHgap(10.0);
        grid.setVgap(10.0);
        grid.setPadding(new Insets(20.0,20.0,20.0,20.0));

        Label nameL = new Label("Category name");
        nameL.setFont(Font.font(18));
        TextField nameF = new TextField();
        nameF.setFont(Font.font(20.0));
        grid.add(nameL, 0,0);
        grid.add(nameF, 1,0);

        Label statusL = new Label("Status");
        statusL.setFont(Font.font(18.0));
        grid.add(statusL, 0,1);
        ComboBox<String> status = new ComboBox<>();
        status.getItems().addAll("Active", "Inactive", "Deactivated", "Other");
        status.setValue("Active");
        status.setEditable(false);
        status.setMinWidth(57);
        status.setMinHeight(41);
        grid.add(status,1,1);

        Button add = new Button("ADD");
        add.setFont(Font.font(20.0));
        add.setPrefWidth(100);
        add.setOnAction(e -> {
            if (nameF.getText().isEmpty()){
                dialog.showDialog("Alert", "Category value cannot be empty");
            }else{
                if (database.addCategory(nameF.getText(), status.getValue()))
                {
                    dialog.showDialog("Success", nameF.getText()+ " category added successfully");
                    nameF.clear();
                }
            }
        });
        add.setBackground(new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        add.setTextFill(Color.WHITE);
        grid.add(add,1,2);

        root.getChildren().add(grid);

        return root;
    }
}
