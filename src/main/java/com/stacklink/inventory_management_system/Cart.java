package com.stacklink.inventory_management_system;

import javafx.beans.property.SimpleStringProperty;

public class Cart {

    private SimpleStringProperty saleID;
    private SimpleStringProperty code;
    private SimpleStringProperty name;
    private SimpleStringProperty quantity;
    private SimpleStringProperty price;

    private SimpleStringProperty date;
    private SimpleStringProperty total;

    public Cart(String pSaleID, String pCode, String pName, String pQuantity, String pPrice, String total, String date){
        this.saleID = new SimpleStringProperty(pSaleID);
        this.code = new SimpleStringProperty(pCode);
        this.name = new SimpleStringProperty(pName);
        this.quantity = new SimpleStringProperty(pQuantity);
        this.price = new SimpleStringProperty(pPrice);
        this.total = new SimpleStringProperty(total);
        this.date = new SimpleStringProperty(date);
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public SimpleStringProperty totalProperty() {
        return total;
    }

    public SimpleStringProperty saleIDProperty() {
        return saleID;
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public SimpleStringProperty quantityProperty() {
        return quantity;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public String getTotal() {
        return total.get();
    }

    public String getQuantity() {
        return quantity.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getCode() {
        return code.get();
    }

    public String getSaleID() {
        return saleID.get();
    }

    public String getName() {
        return name.get();
    }

    public String getPrice() {
        return price.get();
    }
}
