package com.stacklink.inventory_management_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class Dashboard {

    IMSProduct ims = new IMSProduct();
    IMSDatabase database = new IMSDatabase();

    IMSSales sales = new IMSSales();

    IMSExpense expenses = new IMSExpense();

    String date = "";
    public static HBox Container(String iconPath, Label mTitle, Text mSubtitle, Color color){
        ImageView icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.getResourceAsStream(iconPath))));
        icon.setFitHeight(40);
        icon.setFitWidth(40);
        VBox _vIconBox = new VBox(icon);
        _vIconBox.setAlignment(Pos.CENTER_LEFT);
        _vIconBox.setPadding(new Insets(20.0,20.0,20.0,20.0));

        mSubtitle.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
        mSubtitle.setFill(Color.WHITE);

        mTitle.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 15));
        mTitle.setTextFill(Color.WHITE);
        mTitle.setContentDisplay(ContentDisplay.BOTTOM);
        VBox _vTextBox = new VBox(mTitle);
        _vTextBox.setAlignment(Pos.CENTER);

        HBox _box = new HBox(10.0);
        _box.setMaxHeight(120);
        _box.setMaxWidth(200);
        _box.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        _box.getChildren().addAll(_vIconBox, _vTextBox);
        _box.setPadding(new Insets(25.0,25.0,25.0,25.0));
        return _box;
    }

    public AnchorPane arrangeDashboard(Stage primaryStage){
        InventoryApplication app = new InventoryApplication();
        AnchorPane root = new AnchorPane();
        root.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        VBox vRoot = new VBox(10.0);
        AnchorPane.setTopAnchor(vRoot, 0.0);
        AnchorPane.setLeftAnchor(vRoot, 0.0);
        AnchorPane.setRightAnchor(vRoot, 0.0);

        HBox fBox = new HBox(10.0);
        fBox.setAlignment(Pos.CENTER_LEFT);


        HBox hBox = new HBox(400.0);
        hBox.setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        hBox.setMinHeight(60.0);
        Button logout = new Button("Logout");
        logout.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 16));
        logout.setOnMouseClicked(e -> {
            primaryStage.close();
            app.start(primaryStage);
        });
        hBox.getChildren().addAll(fBox, logout);
        hBox.setPadding(new Insets(25.0,25.0,25.0,25.0));
        hBox.setAlignment(Pos.CENTER_RIGHT);

        HBox hRoot = new HBox(15.0);

        Text tRevenue = new Text("Revenue");
        Label lRevenue = new Label(String.valueOf(database.getTotalRevenue(date)), tRevenue);

        Text tProducts = new Text("Products");
        Label lProducts = new Label(String.valueOf(database.getCollectionCount("Product")), tProducts);

        Text tSales = new Text("Sales");
        Label lSales = new Label(String.valueOf(database.getTotalSales(date)), tSales);

        Text tUsers = new Text("Users");
        Label lUsers = new Label(String.valueOf(database.getCollectionCount("Users")), tUsers);

        Text tExpense = new Text("Expenses");
        Label lExpenses = new Label(String.valueOf(database.getRevenueOfTask("Expenses", "cost", date)), tExpense);

        hRoot.getChildren().addAll(
                Container("/assets/category.png", lRevenue, tRevenue, Color.PALEVIOLETRED),
                Container("/assets/cube.png", lProducts, tProducts, Color.FIREBRICK),
                Container("/assets/money.png", lSales, tSales, Color.DARKSLATEBLUE),
                Container("/assets/users.png", lUsers, tUsers, Color.ORANGE),
                Container("/assets/expense.png",lExpenses, tExpense, Color.CORAL));
        hRoot.setPadding(new Insets(25.0,25.0,25.0,25.0));

        AnchorPane revenueProgressLayout = new AnchorPane();
        revenueProgressLayout.setPrefHeight(200);
        revenueProgressLayout.setPrefWidth(170);
        revenueProgressLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        Label profit = new Label("PROFIT");
        AnchorPane.setTopAnchor(profit, 30.0);
        AnchorPane.setLeftAnchor(profit, 50.0);
        profit.setFont(Font.font(null, FontWeight.BOLD, 15));
        ProgressIndicator profitProgress = new ProgressIndicator();
        AnchorPane.setTopAnchor(profitProgress, 60.0);
        AnchorPane.setLeftAnchor(profitProgress, 30.0);
        profitProgress.setProgress(database.getProfitProgress(date));
        profitProgress.setTooltip(new Tooltip("Total Profit"));
        profitProgress.setMinSize(100, 80);
        revenueProgressLayout.getChildren().addAll(profit, profitProgress);

        AnchorPane salesProgressLayout = new AnchorPane();
        salesProgressLayout.setPrefWidth(170);
        salesProgressLayout.setPrefHeight(200);
        salesProgressLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        Label salesL = new Label("SALES");
        salesL.setFont(Font.font("Verdana", FontWeight.BOLD, 17));
        AnchorPane.setTopAnchor(salesL, 30.0);
        AnchorPane.setLeftAnchor(salesL, 50.0);
        ProgressIndicator salesProgress = new ProgressIndicator();
        AnchorPane.setTopAnchor(salesProgress, 60.0);
        AnchorPane.setLeftAnchor(salesProgress, 30.0);
        double sales = database.getTotalSales(date);
        double costOfProducts = database.getTotalCostOfProducts();
        salesProgress.setProgress(sales / costOfProducts);
        salesProgress.setTooltip(new Tooltip("Sales Progress"));
        salesProgress.setMinSize(100,80);
        ColorAdjust adjust = new ColorAdjust();
        adjust.setHue(0.9);
        salesProgress.setEffect(adjust);
        salesProgressLayout.getChildren().addAll(salesL, salesProgress);

        Group imsChartLayout = new Group();
        AnchorPane.setRightAnchor(imsChartLayout, 0.0);
        AnchorPane.setBottomAnchor(imsChartLayout, 90.0);

        ObservableList<PieChart.Data> imsValueList = FXCollections.observableArrayList(
                new PieChart.Data("Total Revenue", database.getTotalRevenue(date)),
                new PieChart.Data("Product Cost", database.getTotalCostOfProducts()),
                new PieChart.Data("Total Sales", database.getTotalSales(date)),
                new PieChart.Data("Total Expenses", database.getRevenueOfTask("Expenses", "cost", date))
        );
        PieChart imsChart = new PieChart(imsValueList);
        imsChart.setTitle("IMS Performance Analysis");
        imsChart.setPrefSize(450, 350);
        imsChart.setLegendSide(Side.LEFT);
        imsChart.setLabelLineLength(5.0);
        imsChartLayout.getChildren().add(imsChart);
        imsChart.getData().forEach(data -> {
            String percentage = String.format("%.2f%%", data.getPieValue() / 100);
            Tooltip tooltip = new Tooltip(percentage);
            Tooltip.install(data.getNode(), tooltip);
        });

        Label filter = new Label("Filter by date");
        filter.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 17));
        filter.setTextFill(Color.WHITE);

        DatePicker filterDatePicker = new DatePicker();
        filterDatePicker.setMinWidth(50);
        filterDatePicker.setMinHeight(41);
        filterDatePicker.setEditable(false);
        filterDatePicker.setTooltip(new Tooltip("Filter by date"));
        filterDatePicker.setOnAction((e) -> {
            lRevenue.setText(String.valueOf(database.getTotalRevenue(String.valueOf(filterDatePicker.getValue()))));
            lSales.setText(String.valueOf(database.getTotalSales(String.valueOf(filterDatePicker.getValue()))));
            lExpenses.setText(String.valueOf(database.getRevenueOfTask("Expenses", "cost",
                    String.valueOf(filterDatePicker.getValue()))));
            salesProgress.setProgress(database.getTotalSales(String.valueOf(filterDatePicker.getValue())) / database.getTotalCostOfProducts());
            profitProgress.setProgress(database.getProfitProgress(String.valueOf(filterDatePicker.getValue())));

            ObservableList<PieChart.Data> imsFValueList = FXCollections.observableArrayList(
                    new PieChart.Data("Total Revenue", database.getTotalRevenue(String.valueOf(filterDatePicker.getValue()))),
                    new PieChart.Data("Product Cost", database.getTotalCostOfProducts()),
                    new PieChart.Data("Total Sales", database.getTotalSales(String.valueOf(filterDatePicker.getValue()))),
                    new PieChart.Data("Total Expenses", database.getRevenueOfTask("Expenses", "cost", String.valueOf(filterDatePicker.getValue())))
            );
            imsChart.setData(imsFValueList);
        });
        fBox.getChildren().addAll(filter, filterDatePicker);

        HBox progressLayout = new HBox(30.0);
        progressLayout.getChildren().addAll(salesProgressLayout, revenueProgressLayout);
        progressLayout.setPadding(new Insets(25.0,25.0,25.0,25.0));

        vRoot.getChildren().addAll(hBox,hRoot, progressLayout);
        root.getChildren().addAll(vRoot, imsChartLayout);
        return root;
    }

    public AnchorPane productRoot(){
        AnchorPane root = new AnchorPane();

        VBox vRoot = new VBox(25.0);

        ImageView icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.
                getResourceAsStream("/assets/menu.png"))));
        icon.setFitWidth(35);
        icon.setFitHeight(35);
        Label productL = new Label("ALL PRODUCTS", icon);
        productL.setContentDisplay(ContentDisplay.LEFT);
        productL.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        HBox hRight = new HBox(productL);
        hRight.setAlignment(Pos.CENTER_LEFT);
        hRight.setPadding(new Insets(25.0,25.0,25.0,25.0));

        Label searchL = new Label("Search Product");
        searchL.setFont(Font.font(16.0));
        TextField search = new TextField();
        search.setPromptText("product code");
        search.setPrefWidth(150);
        search.setFont(Font.font(18.0));
        HBox hCenter = new HBox(8.0);
        hCenter.getChildren().addAll(searchL, search);
        hCenter.setAlignment(Pos.CENTER);

        Button addProduct = new Button("ADD NEW");
        addProduct.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 16));
        addProduct.setTextFill(Color.WHITE);
        addProduct.setBackground(new Background(new BackgroundFill(Color.CRIMSON, CornerRadii.EMPTY, Insets.EMPTY)));
        addProduct.setOnAction(e -> ims.AddNewProduct());
        HBox hLeft = new HBox(addProduct);
        AnchorPane.setRightAnchor(hLeft,12.0);
        AnchorPane.setTopAnchor(hLeft, 6.0);
        hLeft.setPadding(new Insets(25.0,25.0,25.0,25.0));

        AnchorPane htopRoot = new AnchorPane(hRight, hCenter, hLeft);
        AnchorPane.setLeftAnchor(hCenter, 400.0);
        AnchorPane.setTopAnchor(hCenter, 30.0);
        htopRoot.setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        VBox table = new VBox(ims.viewProducts(database.getProducts()));
        table.setPadding(new Insets(15.0));
        vRoot.getChildren().addAll(htopRoot, table);

        search.setOnKeyTyped((e) ->{
            if (search.getText().length() > 1){
                table.getChildren().clear();
                table.getChildren().add(ims.viewProducts(database.getProducts(search.getText())));
            }
        });

        AnchorPane.setTopAnchor(vRoot, 0.0);
        AnchorPane.setLeftAnchor(vRoot, 0.0);
        AnchorPane.setRightAnchor(vRoot, 0.0);

        root.getChildren().addAll(vRoot);

        return root;
    }

    public AnchorPane salesRoot(Stage stage){
        AnchorPane root = new AnchorPane();

        VBox vRoot = new VBox(15.0);
        VBox table = new VBox(sales.viewSales(stage, database.getAllSales()));

        ImageView icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.
                getResourceAsStream("/assets/cart.png"))));
        icon.setFitWidth(35);
        icon.setFitHeight(35);
        Label salesL = new Label("ALL SALES", icon);
        salesL.setContentDisplay(ContentDisplay.LEFT);
        salesL.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        HBox hRight = new HBox(salesL);
        hRight.setAlignment(Pos.CENTER_LEFT);
        hRight.setPadding(new Insets(25.0,25.0,25.0,25.0));

        DatePicker datePicker = new DatePicker();
        datePicker.setPrefHeight(41);
        datePicker.setPrefWidth(180);
        datePicker.setEditable(true);
        datePicker.setPromptText("Filter By Date");
        datePicker.setOnAction((e) -> {
            table.getChildren().clear();
            table.getChildren().add(sales.viewSales(stage, database.getSalesByDate(String.valueOf(datePicker.getValue()))));
        });
        HBox hCenter = new HBox(datePicker);
        AnchorPane.setLeftAnchor(hCenter,200.0);
        AnchorPane.setTopAnchor(hCenter, 6.0);
        hCenter.setPadding(new Insets(25.0,25.0,25.0,25.0));

        TextField search = new TextField();
        search.setPromptText("search sale id");
        search.setPrefWidth(150);
        search.setFont(Font.font(18.0));
        search.setOnKeyTyped((e) ->{
            if (search.getText().length() > 1){
                table.getChildren().clear();
                table.getChildren().add(sales.viewSales(stage, database.getSalesBySaleID(search.getText())));
            }
        });

        HBox hLeft = new HBox(search);
        AnchorPane.setRightAnchor(hLeft,300.0);
        AnchorPane.setTopAnchor(hLeft, 6.0);
        hLeft.setPadding(new Insets(25.0,25.0,25.0,25.0));

        Button cancel_sale = new Button("CANCEL SALE");
        cancel_sale.setFont(Font.font(15.0));
        cancel_sale.setBackground(new Background(new BackgroundFill(Color.CRIMSON, CornerRadii.EMPTY, Insets.EMPTY)));
        cancel_sale.setTextFill(Color.WHITE);
        cancel_sale.setOnAction((e) -> sales.cancelSale());
        HBox hCancel = new HBox(cancel_sale);
        hCancel.setPadding(new Insets(25.0,25.0,25.0,25.0));
        AnchorPane.setRightAnchor(hCancel, 20.0);
        AnchorPane.setTopAnchor(hCancel, 6.0);

        AnchorPane htopRoot = new AnchorPane(hRight, hCenter, hLeft, hCancel);
        htopRoot.setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        table.setPadding(new Insets(15.0));
        vRoot.getChildren().addAll(htopRoot, table);

        AnchorPane.setTopAnchor(vRoot, 0.0);
        AnchorPane.setLeftAnchor(vRoot, 0.0);
        AnchorPane.setRightAnchor(vRoot, 0.0);

        root.getChildren().addAll(vRoot);

        return root;
    }
    public AnchorPane expensesRoot(Stage stage){
        AnchorPane root = new AnchorPane();

        VBox vRoot = new VBox(15.0);
        HBox table = new HBox(50.0);
        table.getChildren().addAll(expenses.addExpense(), expenses.viewExpenses(stage, database.getAllExpenses()));

        ImageView icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.
                getResourceAsStream("/assets/expense.png"))));
        icon.setFitWidth(35);
        icon.setFitHeight(35);
        Label expenseL = new Label("ALL EXPENSES", icon);
        expenseL.setContentDisplay(ContentDisplay.LEFT);
        expenseL.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        HBox hRight = new HBox(expenseL);
        hRight.setAlignment(Pos.CENTER_LEFT);
        hRight.setPadding(new Insets(25.0,25.0,25.0,25.0));

        DatePicker datePicker = new DatePicker();
        datePicker.setPrefHeight(41);
        datePicker.setPrefWidth(180);
        datePicker.setEditable(true);
        datePicker.setPromptText("Filter By Date");
        datePicker.setOnAction((e) -> {
            table.getChildren().clear();
            table.getChildren().addAll(expenses.addExpense(),expenses.viewExpenses(stage, database.getExpensesByDate(String.valueOf(datePicker.getValue()))));
        });
        HBox hCenter = new HBox(datePicker);
        AnchorPane.setRightAnchor(hCenter,20.0);
        AnchorPane.setTopAnchor(hCenter, 6.0);
        hCenter.setPadding(new Insets(25.0,25.0,25.0,25.0));

        AnchorPane htopRoot = new AnchorPane(hRight, hCenter);
        htopRoot.setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        table.setPadding(new Insets(15.0));
        vRoot.getChildren().addAll(htopRoot, table);

        AnchorPane.setTopAnchor(vRoot, 0.0);
        AnchorPane.setLeftAnchor(vRoot, 0.0);
        AnchorPane.setRightAnchor(vRoot, 0.0);

        root.getChildren().addAll(vRoot);
        return root;
    }
}
