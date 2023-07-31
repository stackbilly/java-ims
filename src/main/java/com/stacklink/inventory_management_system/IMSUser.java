package com.stacklink.inventory_management_system;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class IMSUser {

    static IMSDatabase database = new IMSDatabase();
    static DialogWindow dialog = new DialogWindow();
    public AnchorPane addUser(){
        AnchorPane root = new AnchorPane();

        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(15);

        AnchorPane.setTopAnchor(grid, 20.0);
        AnchorPane.setLeftAnchor(grid, 50.0);

        Label username = new Label("Username");
        username.setFont(Font.font(18.0));
        TextField usernameF = new TextField();
        usernameF.setFont(Font.font(20.0));
        grid.add(username, 0, 0);
        grid.add(usernameF, 1,0);

        Label phoneNoL = new Label("Phone Number");
        phoneNoL.setFont(Font.font(18.0));
        TextField phoneNoF = new TextField();
        phoneNoF.setFont(Font.font(20.0));
        grid.add(phoneNoL, 0, 1);
        grid.add(phoneNoF, 1,1);

        Label passwordL = new Label("Password");
        passwordL.setFont(Font.font(18.0));
        PasswordField passwordF = new PasswordField();
        passwordF.setFont(Font.font(20.0));
        grid.add(passwordL, 0,2);
        grid.add(passwordF, 1,2);

        Button addUser = new Button("ADD USER");
        addUser.setFont(Font.font(20.0));
        addUser.setOnAction(event -> {
            database.addUser(usernameF.getText(), passwordF.getText(), phoneNoF.getText());
            usernameF.clear();
            phoneNoF.clear();
            passwordF.clear();
        });
        grid.add(addUser,1,3);

        root.getChildren().add(grid);

        return root;
    }

    public AnchorPane userInterface(){
        AnchorPane root = new AnchorPane();

        ImageView icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.
                getResourceAsStream("/assets/users.png"))));
        icon.setFitWidth(35);
        icon.setFitHeight(35);
        Label usersL = new Label("ALL USERS", icon);
        usersL.setContentDisplay(ContentDisplay.LEFT);
        usersL.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        HBox hRight = new HBox(usersL);
        hRight.setAlignment(Pos.CENTER_LEFT);
        hRight.setPadding(new Insets(25.0,25.0,25.0,25.0));

        Button forgotPassword = new Button("FORGOT PASSWORD");
        forgotPassword.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 16));
        forgotPassword.setTextFill(Color.WHITE);
        forgotPassword.setBackground(new Background(new BackgroundFill(Color.ORANGERED, CornerRadii.EMPTY, Insets.EMPTY)));
        HBox hLeft = new HBox(forgotPassword);
        AnchorPane.setRightAnchor(hLeft,12.0);
        AnchorPane.setTopAnchor(hLeft, 6.0);
        hLeft.setPadding(new Insets(25.0,25.0,25.0,25.0));
        forgotPassword.setOnAction(e->forgotPassword());

        TableView<User> userTable = new TableView<>();

        TableColumn<User, String> indexCol = new TableColumn<>("#");
        indexCol.setCellValueFactory((e) -> e.getValue().indexProperty());
        indexCol.setPrefWidth(130.0);

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory((e) -> e.getValue().nameProperty());
        nameCol.setPrefWidth(135.0);

        TableColumn<User, String> phoneCol = new TableColumn<>("Phone Number");
        phoneCol.setCellValueFactory((e) -> e.getValue().phoneNoProperty());
        phoneCol.setPrefWidth(135);
        userTable.getColumns().addAll(indexCol, nameCol, phoneCol);
        userTable.itemsProperty().setValue(database.getUsers());
        userTable.setMinSize(400,100);
        userTable.setMaxWidth(400);

        Button delete = new Button("DELETE USER");
        delete.setFont(Font.font(20.0));
        delete.setTextFill(Color.WHITE);
        delete.setBackground(new Background(new BackgroundFill(Color.CRIMSON, CornerRadii.EMPTY, Insets.EMPTY)));
        delete.setOnAction((e) -> {
            String username = userTable.getSelectionModel().getSelectedItem().getName();
            System.out.println(username);
            if (database.deleteTask("username", username, "Users")){
                dialog.showDialog("Success", "Successfully deleted "+username+" from database");
                userTable.getItems().remove(userTable.getSelectionModel().getSelectedItem());
            }
            else
                dialog.showDialog("Failure", "Error deleting "+username+" from database");
        });

        HBox tableBox = new HBox(25.0);
        tableBox.getChildren().addAll(addUser(),userTable, delete);
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

    public static void forgotPassword(){
        AnchorPane root = new AnchorPane();

        Stage window = new Stage(StageStyle.UTILITY);
        window.initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setVgap(12.0);
        grid.setHgap(12.0);

        Label usernameL = new Label("Username");
        usernameL.setFont(Font.font(18.0));
        grid.add(usernameL, 0,0);
        TextField usernameF = new TextField();
        usernameF.setFont(Font.font(20.0));
        grid.add(usernameF, 1,0);

        Label passwordL = new Label("New Password");
        passwordL.setFont(Font.font(18.0));
        grid.add(passwordL, 0,1);
        PasswordField passwordF = new PasswordField();
        passwordF.setFont(Font.font(20.0));
        grid.add(passwordF, 1,1);

        Button changePassword = new Button("CHANGE PASSWORD");
        changePassword.setFont(Font.font(20.0));
        changePassword.setOnAction((e) -> {
            if(!usernameF.getText().isEmpty() && !passwordF.getText().isEmpty()){
                if(database.updatePassword(usernameF.getText(), passwordF.getText()))
                    dialog.showDialog("Success", usernameF.getText()+" password changed successfully");
                else
                    dialog.showDialog("Failure!", usernameF.getText()+" password change fail");
                window.close();
            }else {
                dialog.showDialog("User Error", "Please fill all the fields to proceed");
            }
        });
        grid.add(changePassword, 1,2);

        root.getChildren().add(grid);
        AnchorPane.setTopAnchor(grid, 20.0);
        AnchorPane.setLeftAnchor(grid, 30.0);

        Scene scene = new Scene(root, 450,250,true);
        window.setScene(scene);
        window.setResizable(false);
        window.setTitle("Change Password");
        window.showAndWait();
    }

}
