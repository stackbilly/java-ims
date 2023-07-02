package com.stacklink.inventory_management_system;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;


public class InventoryApplication extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        AnchorPane rootLayout = new AnchorPane();
        rootLayout.setPrefHeight(589);
        rootLayout.setPrefWidth(779);
        rootLayout.setBackground(new Background(
                new BackgroundImage(
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/background.jpg"))),
                        BackgroundRepeat.REPEAT,BackgroundRepeat.NO_REPEAT,
                        new BackgroundPosition(Side.LEFT,0,true,Side.BOTTOM,0,true),
                        new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,true,true,false,true)
                )
        ));

        //login layout
        AnchorPane loginLayout = new AnchorPane();
        loginLayout.setBackground(new Background(new BackgroundFill(Color.grayRgb(35), CornerRadii.EMPTY, Insets.EMPTY)));
        loginLayout.setPrefWidth(450);
        loginLayout.setPrefHeight(510);
        AnchorPane.setTopAnchor(loginLayout, 60.0);
        AnchorPane.setLeftAnchor(loginLayout,400.0);

        GridPane grid = new GridPane();
        grid.setVgap(40);
        grid.setHgap(10);
        grid.setPadding(new Insets(25.0,25.0,25.0,25.0));

        //fields
        Label username = new Label("Username");
        username.setFont(Font.font(18.0));
        username.setTextFill(Color.WHEAT);
        TextField userName = new TextField();
        userName.setFont(Font.font(20.0));
        grid.add(username, 0,0);
        grid.add(userName,1,0);

        Label password = new Label("Password");
        password.setTextFill(Color.WHEAT);
        password.setFont(Font.font(18.0));
        PasswordField passWord = new PasswordField();
        passWord.setFont(Font.font(20.0));
        grid.add(password, 0,1);
        grid.add(passWord,1,1);

        AnchorPane.setTopAnchor(grid, 130.0);
        AnchorPane.setLeftAnchor(grid, 20.0);

        //Button layout
        HBox btn_Layout = new HBox(10);
        btn_Layout.setPrefHeight(59);
        btn_Layout.setPrefWidth(269);
        btn_Layout.setLayoutX(122);
        btn_Layout.setLayoutY(429);
        Button login = new Button("LOGIN");
        login.setFont(Font.font(20.0));
        login.setOnAction(e -> {
            primaryStage.close();
            DashBoardUserInterface();
        });
        login.setPrefHeight(50);
        login.setPrefWidth(271);
        login.setAlignment(Pos.BOTTOM_CENTER);
        login.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY )));
        btn_Layout.getChildren().add(login);
        AnchorPane.setBottomAnchor(btn_Layout,90.0);
        AnchorPane.setLeftAnchor(btn_Layout, 100.0);


        HBox hBar = new HBox();
        hBar.setPrefHeight(40.0);
        Text title = new Text("Inventory Management System");
        title.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 16));
        title.setFill(Color.WHITE);
        hBar.setAlignment(Pos.CENTER);
        hBar.getChildren().add(title);
        hBar.setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        AnchorPane.setTopAnchor(hBar,0.0);
        AnchorPane.setLeftAnchor(hBar,0.0);
        AnchorPane.setRightAnchor(hBar,0.0);

        Hyperlink forgot_password = new Hyperlink("Forgot Password?");
        AnchorPane.setBottomAnchor(forgot_password, 180.0);
        AnchorPane.setLeftAnchor(forgot_password, 100.0);

        loginLayout.getChildren().addAll(grid, btn_Layout, hBar, forgot_password);
        rootLayout.getChildren().add(loginLayout);

        Scene scene = new Scene(rootLayout, 1300,690,true);

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(visualBounds.getMinX());
        primaryStage.setY(visualBounds.getMinY());
        primaryStage.setMinWidth(visualBounds.getWidth());
        primaryStage.setMinHeight(visualBounds.getHeight());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void addUser(){
        Stage primaryStage = new Stage(StageStyle.UTILITY);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        AnchorPane root = new AnchorPane();
        IMSDatabase database = new IMSDatabase();

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

        Label passwordL = new Label("Password");
        passwordL.setFont(Font.font(18.0));
        PasswordField passwordF = new PasswordField();
        passwordF.setFont(Font.font(20.0));
        grid.add(passwordL, 0,1);
        grid.add(passwordF, 1,1);

        Button addUser = new Button("ADD USER");
        addUser.setFont(Font.font(20.0));
        addUser.setOnAction(event -> {
            database.addUser(usernameF.getText(), passwordF.getText());
            primaryStage.close();
        });

        AnchorPane.setLeftAnchor(addUser, 150.0);
        AnchorPane.setBottomAnchor(addUser, 50.0);

        root.getChildren().addAll(grid, addUser);
        Scene scene = new Scene(root, 544, 240, true);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Add New User");
        primaryStage.showAndWait();
    }

    public static void DashBoardUserInterface(){
        Stage dashboardStage = new Stage();

        PointOfSale pos = new PointOfSale();
        Dashboard dB = new Dashboard();
        IMSCategory mCategory = new IMSCategory();
        IMSReports reports = new IMSReports();

        AnchorPane root = new AnchorPane();
        root.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        BorderPane pane = new BorderPane();

        VBox sideBar = new VBox(10.0);
        sideBar.setBackground(new Background(new BackgroundFill(Color.grayRgb(35), CornerRadii.EMPTY, Insets.EMPTY)));

        sideBar.setPrefWidth(280);
        pane.setCenter(dB.arrangeDashboard(dashboardStage));

        HBox topBarBox = new HBox();
        Calendar mCal = Calendar.getInstance();
        Text date = new Text(new SimpleDateFormat("E, dd MMM yyy").format(mCal.getTime()));
        date.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 15));
        date.setFill(Color.WHITE);
        topBarBox.getChildren().addAll(date);
        topBarBox.setAlignment(Pos.CENTER);
        topBarBox.setPadding(new Insets(15.0,15.0,15.0,15.0));
        topBarBox.setPrefHeight(60);
        topBarBox.setBackground(new Background(new BackgroundFill(Color.STEELBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        HBox lblBox = new HBox();
        lblBox.setAlignment(Pos.CENTER);
        lblBox.setPrefHeight(60);
        lblBox.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        Label adminLabel = new Label("Inventory Management");
        adminLabel.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 17));
        adminLabel.setTextFill(Color.WHITE);
        lblBox.getChildren().add(adminLabel);

        ImageView db_icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.getResourceAsStream("/assets/dashboard.png"))));
        db_icon.setFitHeight(20);
        db_icon.setFitWidth(20);
        Label dashboard = new Label("Dashboard", db_icon);
        dashboard.setPadding(new Insets(10.0,10.0,10.0,10.0));
        dashboard.setContentDisplay(ContentDisplay.LEFT);
        dashboard.setPrefWidth(280);
        dashboard.setPrefHeight(60);
        dashboard.setFont(Font.font("Verdana", FontWeight.MEDIUM, 17));
        dashboard.setTextFill(Color.WHITE);
        dashboard.setOnMouseClicked(e -> pane.setCenter(dB.arrangeDashboard(dashboardStage)));
        dashboard.setOnMouseExited(e -> dashboard.setBackground(null));
        dashboard.setOnMouseEntered(e -> dashboard.setBackground(new Background(new BackgroundFill(Color.grayRgb(40), CornerRadii.EMPTY, Insets.EMPTY))));

        ImageView expense_icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.getResourceAsStream("/assets/expense.png"))));
        expense_icon.setFitHeight(20);
        expense_icon.setFitWidth(20);
        Label expenses = new Label("Expenses", expense_icon);
        expenses.setContentDisplay(ContentDisplay.LEFT);
        expenses.setPadding(new Insets(10.0,10.0,10.0,10.0));
        expenses.setPrefWidth(280);
        expenses.setPrefHeight(60);
        expenses.setFont(Font.font("Verdana", FontWeight.MEDIUM, 16));
        expenses.setTextFill(Color.WHITE);
        expenses.setOnMouseClicked(e -> pane.setCenter(dB.expensesRoot(dashboardStage)));
        expenses.setOnMouseExited(e -> expenses.setBackground(null));
        expenses.setOnMouseEntered(e -> expenses.setBackground(new Background(new BackgroundFill(Color.grayRgb(40), CornerRadii.EMPTY, Insets.EMPTY))));

        ImageView category_icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.getResourceAsStream("/assets/category.png"))));
        category_icon.setFitHeight(20);
        category_icon.setFitWidth(20);
        Label category = new Label("Category", category_icon);
        category.setPadding(new Insets(10.0,10.0,10.0,10.0));
        category.setContentDisplay(ContentDisplay.LEFT);
        category.setPrefWidth(280);
        category.setPrefHeight(60);
        category.setFont(Font.font("Verdana", FontWeight.MEDIUM, 17));
        category.setTextFill(Color.WHITE);
        category.setOnMouseClicked(e -> pane.setCenter(mCategory.categoryRoot()));
        category.setOnMouseExited(e -> category.setBackground(null));
        category.setOnMouseEntered(e -> category.setBackground(new Background(new BackgroundFill(Color.grayRgb(40), CornerRadii.EMPTY, Insets.EMPTY))));

        ImageView product_icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.getResourceAsStream("/assets/cube.png"))));
        product_icon.setFitHeight(20);
        product_icon.setFitWidth(20);
        Label product = new Label("Products", product_icon);
        product.setPadding(new Insets(10.0,10.0,10.0,10.0));
        product.setContentDisplay(ContentDisplay.LEFT);
        product.setPrefWidth(280);
        product.setPrefHeight(60);
        product.setFont(Font.font("Verdana", FontWeight.MEDIUM, 16));
        product.setTextFill(Color.WHITE);
        product.setOnMouseClicked(e -> pane.setCenter(dB.productRoot()));
        product.setOnMouseExited(e -> product.setBackground(null));
        product.setOnMouseEntered(e -> product.setBackground(new Background(new BackgroundFill(Color.grayRgb(40), CornerRadii.EMPTY, Insets.EMPTY))));

        ImageView pos_icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.getResourceAsStream("/assets/sales.png"))));
        pos_icon.setFitHeight(20);
        pos_icon.setFitWidth(20);
        Label lPos = new Label("POS", pos_icon);
        lPos.setPadding(new Insets(10.0,10.0,10.0,10.0));
        lPos.setContentDisplay(ContentDisplay.LEFT);
        lPos.setPrefWidth(280);
        lPos.setPrefHeight(60);
        lPos.setFont(Font.font("Verdana", FontWeight.MEDIUM, 17));
        lPos.setTextFill(Color.WHITE);
        lPos.setOnMouseClicked(e -> pane.setCenter(pos.pointOfSale()));
        lPos.setOnMouseExited(e -> lPos.setBackground(null));
        lPos.setOnMouseEntered(e -> lPos.setBackground(new Background(new BackgroundFill(Color.grayRgb(40), CornerRadii.EMPTY, Insets.EMPTY))));

        ImageView sales_icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.getResourceAsStream("/assets/money.png"))));
        sales_icon.setFitHeight(20);
        sales_icon.setFitWidth(20);
        Label sales = new Label("Sales", sales_icon);
        sales.setPadding(new Insets(10.0,10.0,10.0,10.0));
        sales.setContentDisplay(ContentDisplay.LEFT);
        sales.setPrefWidth(280);
        sales.setPrefHeight(60);
        sales.setFont(Font.font("Verdana", FontWeight.MEDIUM, 17));
        sales.setTextFill(Color.WHITE);
        sales.setOnMouseClicked(e -> pane.setCenter(dB.salesRoot(dashboardStage)));
        sales.setOnMouseExited(e -> sales.setBackground(null));
        sales.setOnMouseEntered(e -> sales.setBackground(new Background(new BackgroundFill(Color.grayRgb(40), CornerRadii.EMPTY, Insets.EMPTY))));

        ImageView phone_icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.getResourceAsStream("/assets/phone.png"))));
        phone_icon.setFitHeight(20);
        phone_icon.setFitWidth(20);
        Label phone = new Label("Phones", phone_icon);
        phone.setPadding(new Insets(10.0,10.0,10.0,10.0));
        phone.setContentDisplay(ContentDisplay.LEFT);
        phone.setPrefWidth(280);
        phone.setPrefHeight(60);
        phone.setFont(Font.font("Verdana", FontWeight.MEDIUM, 17));
        phone.setTextFill(Color.WHITE);
        phone.setOnMouseClicked(e -> pane.setCenter(dB.phonesRoot(dashboardStage)));
        phone.setOnMouseExited(e -> phone.setBackground(null));
        phone.setOnMouseEntered(e -> phone.setBackground(new Background(new BackgroundFill(Color.grayRgb(40), CornerRadii.EMPTY, Insets.EMPTY))));

        ImageView report_icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.getResourceAsStream("/assets/sales1.png"))));
        report_icon.setFitHeight(20);
        report_icon.setFitWidth(20);
        Label report = new Label("Reports", report_icon);
        report.setPadding(new Insets(10.0,10.0,10.0,10.0));
        report.setContentDisplay(ContentDisplay.LEFT);
        report.setPrefWidth(280);
        report.setPrefHeight(60);
        report.setFont(Font.font("Verdana", FontWeight.MEDIUM, 17));
        report.setTextFill(Color.WHITE);
        report.setOnMouseExited(e -> report.setBackground(null));
        report.setOnMouseClicked((e) -> reports.generateDailyReport());
        report.setOnMouseEntered(e -> report.setBackground(new Background(new BackgroundFill(Color.grayRgb(40), CornerRadii.EMPTY, Insets.EMPTY))));

        ImageView user_icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.getResourceAsStream("/assets/users.png"))));
        user_icon.setFitHeight(20);
        user_icon.setFitWidth(20);
        Label user = new Label("Users", user_icon);
        user.setPadding(new Insets(10.0,10.0,10.0,10.0));
        user.setContentDisplay(ContentDisplay.LEFT);
        user.setPrefWidth(280);
        user.setPrefHeight(60);
        user.setFont(Font.font("Verdana", FontWeight.MEDIUM, 17));
        user.setTextFill(Color.WHITE);
        user.setOnMouseExited(e -> user.setBackground(null));
        user.setOnMouseClicked((e) -> addUser());
        user.setOnMouseEntered(e -> user.setBackground(new Background(new BackgroundFill(Color.grayRgb(40), CornerRadii.EMPTY, Insets.EMPTY))));

        Label dateL = new Label("Date");
        dateL.setFont(Font.font(18.0));
        DatePicker filterDatePicker = new DatePicker();
        filterDatePicker.setMinWidth(50);
        filterDatePicker.setMinHeight(41);

        sideBar.getChildren().addAll(lblBox,dashboard, category, product, lPos, sales, phone, expenses, report, user);
        pane.setLeft(sideBar);
        pane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        dashboardStage.setX(visualBounds.getMinX());
        dashboardStage.setY(visualBounds.getMinY());
        dashboardStage.setMinWidth(visualBounds.getWidth());
        dashboardStage.setMinHeight(visualBounds.getHeight());
        Scene scene = new Scene(pane);
        dashboardStage.setScene(scene);
        dashboardStage.setTitle("IMS Dashboard");
        dashboardStage.show();
    }
}