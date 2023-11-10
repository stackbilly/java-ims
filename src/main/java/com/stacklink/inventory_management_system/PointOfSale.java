package com.stacklink.inventory_management_system;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;


public class PointOfSale {

    IMSDatabase ims = new IMSDatabase();
    DialogWindow dialog = new DialogWindow();


    TextField subTotalF = new TextField();
    TextField payF = new TextField();
    TextField balanceF = new TextField();

    public AnchorPane pointOfSale(){
        AnchorPane root = new AnchorPane();
        VBox vRoot = new VBox(25.0);

        ImageView icon = new ImageView(new Image(Objects.requireNonNull(InventoryApplication.class.
                getResourceAsStream("/assets/sales.png"))));
        icon.setFitWidth(35);
        icon.setFitHeight(35);
        Label saleL = new Label("POS", icon);
        saleL.setContentDisplay(ContentDisplay.LEFT);
        saleL.setFont(Font.font("Verdana", FontWeight.BOLD, 20.0));
        HBox hRight = new HBox(saleL);
        hRight.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        hRight.setPadding(new Insets(25.0,25.0,25.0,25.0));
        hRight.setPrefHeight(50.0);
        hRight.setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        HBox hField = new HBox(10.0);
        hField.setPadding(new Insets(0.0,0.0,15.0,15.0));

        Label codeL = new Label("Code");
        codeL.setFont(Font.font(12.0));
        TextField codeF = new TextField();
        codeF.setFont(Font.font(15.0));

        Label nameL = new Label("Name");
        nameL.setFont(Font.font(12.0));
        TextField nameF = new TextField();
        nameF.setFont(Font.font(15.0));
        nameF.setEditable(false);

        Label priceL = new Label("Price");
        priceL.setFont(Font.font(12.0));
        TextField priceF = new TextField();
        priceF.setFont(Font.font(15.0));

        Label quantityL = new Label("Qty");
        quantityL.setFont(Font.font(12.0));
        TextField quantityF = new TextField();
        quantityF.setFont(Font.font(15.0));

        codeF.setOnAction(e -> {
            nameF.clear();
            quantityF.clear();
            priceF.clear();
            ArrayList<String> saleData = ims.getSaleData(codeF.getText());
            if (!saleData.isEmpty()) {
                nameF.setText(saleData.get(0));
                quantityF.setText(saleData.get(1));
                priceF.setText(saleData.get(2));
            }else {
                dialog.showDialog("Exception", "No Product with code " +codeF.getText()+
                        " found on database");
            }
        });

        Button add = new Button("Add");
        add.setFont(Font.font(15.0));
        add.setPrefWidth(100);

        hField.getChildren().addAll(codeL, codeF, nameL, nameF, quantityL, quantityF,priceL, priceF, add);

        Label title = new Label("Add Products");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 18.0));

        Button delete = new Button("Delete");
        delete.setFont(Font.font(18.0));
        delete.setPrefWidth(100);
        delete.setTextFill(Color.WHITE);
        delete.setBackground(new Background(new BackgroundFill(Color.CRIMSON, CornerRadii.EMPTY, Insets.EMPTY)));

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        HBox tBox = new HBox(visualBounds.getWidth() / 1.72);
        tBox.setPadding(new Insets(0.0, 15.0,15.0,15.0));
        tBox.getChildren().addAll(title, delete);

        TableView<Cart> cartTableView = new TableView<>();
        cartTableView.setEditable(true);
        cartTableView.setPlaceholder(new Label("Add new products"));
        cartTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox tableBox = new VBox(cartTableView);
        tableBox.setPadding(new Insets(0.0,15.0,15.0,15.0));
        cartTableView.setPrefHeight(300.0);

        TableColumn<Cart, String> saleIDCol = new TableColumn<>("Sale ID");
        saleIDCol.setCellValueFactory((e) -> e.getValue().saleIDProperty());

        TableColumn<Cart, String> codeCol = new TableColumn<>("Product Code");
        codeCol.setCellValueFactory((e) -> e.getValue().codeProperty());

        TableColumn<Cart, String> nameCol = new TableColumn<>("Product Name");
        nameCol.setCellValueFactory((e) -> e.getValue().nameProperty());

        TableColumn<Cart, String> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory((e) -> e.getValue().quantityProperty());

        TableColumn<Cart, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory((e) -> e.getValue().priceProperty());
        priceCol.setCellFactory(TextFieldTableCell.forTableColumn());

        cartTableView.getColumns().addAll(saleIDCol, codeCol, nameCol, qtyCol, priceCol);

        add.setOnAction(e -> {
            if (Integer.parseInt(quantityF.getText()) > ims.getProductQuantity(nameF.getText())){
                dialog.showDialog("Alert", "Product demanded cannot be greater than supply");
            }
            else if (Integer.parseInt(quantityF.getText()) == 0){
                dialog.showDialog("Alert", "You cannot demand nothing!");
            }
            else {
                Cart cart = new Cart("",
                        codeF.getText(), nameF.getText(),
                        quantityF.getText(), priceF.getText(), "0", String.valueOf(LocalDate.now())
                );
                ims.updateProductOnSale(codeF.getText(), Integer.parseInt(quantityF.getText()));
                ArrayList<String> saleData = ims.getSaleData(codeF.getText());
                if(ims.addSaleData(cart, saleData.get(3))){
                    dialog.showDialog("Success", nameF.getText()+" added to database");
                    cartTableView.getItems().add(cart);
                    codeF.clear();
                    nameF.clear();
                    quantityF.clear();
                    priceF.clear();
                    new PointOfSale();
                }
            }
        });

        cartTableView.setItems(ims.getSaleData());
        int total = 0;
        for (Cart cart : ims.getSaleData())
            total += Integer.parseInt(cart.getTotal());
        delete.setOnAction((e) -> {
            String saleID = cartTableView.getSelectionModel().getSelectedItem().getSaleID();
            System.out.println(saleID);
            ims.updateProduct(cartTableView.getSelectionModel().getSelectedItem().getName(),
                    Integer.parseInt(cartTableView.getSelectionModel().getSelectedItem().getQuantity()));
            cartTableView.getItems().remove(cartTableView.getSelectionModel().getSelectedItem());
            ims.deleteCartItem(Integer.parseInt(saleID));
        });

        HBox hBox = new HBox(15.0);
        hBox.setPadding(new Insets(15.0,15.0,15.0,15.0));
        Label suTotalL = new Label("Sub Total");
        suTotalL.setFont(Font.font(15.0));
        subTotalF.setFont(Font.font(18.0));
        subTotalF.setText(String.valueOf(total));
        subTotalF.setEditable(false);

        Label payL = new Label("Pay");
        payL.setFont(Font.font(15.0));
        payF.setFont(Font.font(18.0));

        Label balanceL = new Label("Balance");
        balanceL.setFont(Font.font(15.0));
        balanceF.setFont(Font.font(18.0));
        payF.setOnKeyTyped((e) -> {
            if (!subTotalF.getText().isEmpty() && !payF.getText().isEmpty()){
                balanceF.setText(String.valueOf(Integer.parseInt(payF.getText()) - Integer.parseInt(subTotalF.getText())));
            }
        });
        balanceF.setEditable(false);

        Button invoice = new Button("Pay Invoice");
        invoice.setFont(Font.font(18.0));
        invoice.setOnAction((e) -> {
            boolean isOpen = false;
            String path = "";
            if(payF.getText().isEmpty() || balanceF.getText().isEmpty()) {
                dialog.showDialog("Alert", "Amount paid cannot be empty");
            }
            if (!(payF.getText().isEmpty() || balanceF.getText().isEmpty())){
                path = generateInvoice(ims.getSaleData(), subTotalF, payF, balanceF);
                dialog.showDialog("File", "Saved at "+path);
                isOpen = true;
                ims.updateSellState();
                subTotalF.clear();
                payF.clear();
                balanceF.clear();
            }
            if(isOpen && path.length() > 1){
                openPDF(path);
            }
        });

        hBox.getChildren().addAll(suTotalL, subTotalF, payL, payF, balanceL, balanceF,invoice);

        vRoot.getChildren().addAll(hRight, hField, tBox, tableBox, hBox);
        AnchorPane.setTopAnchor(vRoot, 0.0);
        AnchorPane.setLeftAnchor(vRoot, 0.0);
        AnchorPane.setRightAnchor(vRoot, 0.0);
        root.getChildren().add(vRoot);
        return root;
    }

    public String generateInvoice(ObservableList<Cart> product, TextField totalF, TextField paidF, TextField balance) {
        PDDocument receiptDocument = new PDDocument();

        PDPage receiptPage = new PDPage(new PDRectangle());
        receiptDocument.addPage(receiptPage);
        PDPage newPage = receiptDocument.getPage(0);
        PDFont font = PDType1Font.HELVETICA_BOLD;
        ims.addPaySlip(new PaySlip(totalF.getText(), paidF.getText(), balance.getText()));
        String receiptPath;
        ArrayList<String> businessInfo = ims.getBusinessInfo();
        try {
            PDPageContentStream cs = new PDPageContentStream(receiptDocument, newPage);
            cs.beginText();
            cs.setFont(font, 20);
            cs.newLineAtOffset(150, 750);
            cs.showText(businessInfo.get(0));
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ITALIC, 12);
            cs.newLineAtOffset(200, 730);
            cs.showText(businessInfo.get(1));
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ITALIC, 12);
            cs.newLineAtOffset(190, 710);
            cs.showText(businessInfo.get(2));
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ITALIC, 12);
            cs.newLineAtOffset(215, 690);
            cs.showText("Tel Phone No "+businessInfo.get(3));
            cs.endText();

            cs.beginText();
            cs.setFont(font, 15);
            cs.newLineAtOffset(200, 650);
            cs.showText("PRODUCT INFORMATION");
            cs.endText();


            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.newLineAtOffset(150, 610);
            cs.showText("DATE: ");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.newLineAtOffset(200, 610);
            Date date = new Date();
            cs.showText(String.valueOf(String.valueOf(date)));
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
            cs.newLineAtOffset(250, 580);
            cs.showText("ITEM DESCRIPTION");
            cs.endText();

            int yPos = 0;
            for (Cart item : product) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.newLineAtOffset(250, 560 - yPos);
                cs.showText(item.getName());
                cs.endText();
                yPos += 20;
            }

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
            cs.newLineAtOffset(390, 580);
            cs.showText("QTY   x   PRICE");
            cs.endText();

            int xPos = 0;
            for (Cart value : product) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.newLineAtOffset(390, 560 - xPos);
                cs.showText(value.getQuantity() + "              " + value.getPrice());
                cs.endText();
                xPos += 20;
            }

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
            cs.newLineAtOffset(150, 580);
            cs.showText("SALE ID");
            cs.endText();

            int zPos = 0;
            for (Cart cart : product) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 14);
                cs.newLineAtOffset(150, 560 - zPos);
                cs.showText(String.valueOf(cart.getSaleID()));
                cs.endText();
                zPos += 20;
            }

            int _yPos = 560 - product.size() * 20;
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 15);
            cs.newLineAtOffset(250, _yPos - 20);
            cs.showText("Total Amount");
            cs.endText();


            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 15);
            cs.newLineAtOffset(380, _yPos - 20);
            cs.showText(String.valueOf(totalF.getText()));
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 15);
            cs.newLineAtOffset(250, _yPos - 40);
            cs.showText("Paid");
            cs.endText();


            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 15);
            cs.newLineAtOffset(380, _yPos - 40);
            cs.showText(String.valueOf(paidF.getText()));
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 15);
            cs.newLineAtOffset(250, _yPos - 70);
            cs.showText("Change");
            cs.endText();


            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 15);
            cs.newLineAtOffset(380, _yPos - 70);
            cs.showText(String.valueOf(balance.getText()));
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_BOLD_ITALIC, 12);
            cs.newLineAtOffset(150, _yPos - 120);
            cs.showText("Prices inclusive of VAT where applicable");
            cs.endText();

            cs.close();

            String home = System.getProperty("user.home");
            String receiptDIR = home + "\\Documents\\Receipt";
            File file = new File(receiptDIR);
            receiptPath = receiptDIR + "\\receipt" + ThreadLocalRandom.current().nextInt(11111,99999) + ".pdf";
            if (!file.exists()) {
                if (file.mkdir()) {
                    receiptDocument.save(receiptPath);
                    receiptDocument.close();
                }
            } else if (file.exists()) {
                receiptDocument.save(receiptPath);
                receiptDocument.close();
            } else {
                dialog.showDialog("Error", "Error saving " + file.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return receiptPath;
    }

    public void openPDF(String path){
        try {
            File file = new File(path);
            if(file.exists()){
                Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " +file);
            }else {
                dialog.showDialog("Message", "File not located on system");
            }
            dialog.showDialog("Success", "File "+file.getName()+" opened successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
