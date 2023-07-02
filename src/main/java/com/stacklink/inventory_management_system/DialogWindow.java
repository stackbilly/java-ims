package com.stacklink.inventory_management_system;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DialogWindow {
    public void showDialog(String title, String message){
        Stage window = new Stage(StageStyle.UTILITY);

        VBox root = new VBox(40);
        root.setPadding(new Insets(30.0,30.0,30.0,30.0));
        HBox pane = new HBox();

        Label mMessage = new Label(message);
        mMessage.setFont(Font.font("Verdana", FontWeight.MEDIUM, 16));
        pane.getChildren().add(mMessage);

        pane.setAlignment(Pos.CENTER);

        HBox btnBox = new HBox(40);
        Button dismiss = new Button("Dismiss");
        dismiss.setTextFill(Color.WHITE);
        dismiss.setFont(Font.font(18.0));
        dismiss.setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        dismiss.setOnAction(e -> window.close());
        btnBox.setAlignment(Pos.BOTTOM_CENTER);
        btnBox.getChildren().add(dismiss);

        root.getChildren().addAll(pane, btnBox);
        Scene scene = new Scene(root, 600, 160, true);
        window.setScene(scene);
        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);
        window.showAndWait();
    }

    public void showAboutDialog(String title, String message){
        Stage window = new Stage(StageStyle.UTILITY);


        VBox root = new VBox(40);
        root.setPadding(new Insets(30.0,30.0,30.0,30.0));
        HBox pane = new HBox();

        Label mMessage = new Label(message);
        mMessage.setFont(Font.font("Verdana", FontWeight.MEDIUM, 16));
        pane.getChildren().add(mMessage);

        pane.setAlignment(Pos.CENTER);

        HBox btnBox = new HBox(40);
        Button accept = new Button("Accept");
        accept.setTextFill(Color.WHITE);
        accept.setFont(Font.font(18.0));
        accept.setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        accept.setOnAction(e -> window.close());

        Button decline = new Button("Decline");
        decline.setTextFill(Color.WHITE);
        decline.setFont(Font.font(18.0));
        decline.setBackground(new Background(new BackgroundFill(Color.CRIMSON, CornerRadii.EMPTY, Insets.EMPTY)));
        decline.setOnAction(e -> window.close());
        btnBox.setAlignment(Pos.BOTTOM_CENTER);
        btnBox.getChildren().addAll(accept, decline);

        root.getChildren().addAll(pane, btnBox);
        Scene scene = new Scene(root, 600, 160, true);
        window.setScene(scene);
        window.setTitle(title);
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
        window.showAndWait();
    }
}
