package com.stacklink.inventory_management_system;

import javafx.beans.property.SimpleStringProperty;

public class Sales {

    private SimpleStringProperty date;
    private SimpleStringProperty saleID;
    private SimpleStringProperty code;
    private SimpleStringProperty name;
    private SimpleStringProperty quantity;
    private SimpleStringProperty price;
    private SimpleStringProperty profit;
    private SimpleStringProperty index;

    public Sales(String index, String mDate, String mSaleID,String mCode, String mName, String mQuantity,
                 String mPrice, String mProfit){
        this.index = new SimpleStringProperty(index);
        this.date = new SimpleStringProperty(mDate);
        this.saleID = new SimpleStringProperty(mSaleID);
        this.code = new SimpleStringProperty(mCode);
        this.name = new SimpleStringProperty(mName);
        this.quantity = new SimpleStringProperty(mQuantity);
        this.price = new SimpleStringProperty(mPrice);
        this.profit = new SimpleStringProperty(mProfit);
    }

    public SimpleStringProperty indexProperty() {
        return index;
    }

    public SimpleStringProperty saleIDProperty() {
        return saleID;
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty quantityProperty() {
        return quantity;
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }


    public SimpleStringProperty profitProperty() {
        return profit;
    }
}
