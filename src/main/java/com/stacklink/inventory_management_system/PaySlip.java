package com.stacklink.inventory_management_system;

import javafx.beans.property.SimpleStringProperty;

public class PaySlip {

    private SimpleStringProperty saleID;
    private SimpleStringProperty subTotal;
    private SimpleStringProperty amountPaid;
    private SimpleStringProperty balance;

    PaySlip(){

    }

    PaySlip(String bTotal, String bAmount, String bBalance){
        this.subTotal = new SimpleStringProperty(bTotal);
        this.amountPaid = new SimpleStringProperty(bAmount);
        this.balance = new SimpleStringProperty(bBalance);
    }

    public SimpleStringProperty saleIDProperty() {
        return saleID;
    }

    public SimpleStringProperty amountPaidProperty() {
        return amountPaid;
    }

    public SimpleStringProperty balanceProperty() {
        return balance;
    }

    public SimpleStringProperty subTotalProperty() {
        return subTotal;
    }

    public String getBalance() {
        return balance.get();
    }

    public String getAmountPaid() {
        return amountPaid.get();
    }

    public String getSubTotal() {
        return subTotal.get();
    }

    public String getSaleID() {
        return saleID.get();
    }
}
