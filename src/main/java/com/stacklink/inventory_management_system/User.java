package com.stacklink.inventory_management_system;

import javafx.beans.property.SimpleStringProperty;

public class User {
    private SimpleStringProperty index;
    private SimpleStringProperty name;
    private SimpleStringProperty phoneNo;
    private SimpleStringProperty password;

    public User(String mIndex, String mName, String mPhone, String mPassword){
        this.index = new SimpleStringProperty(mIndex);
        this.name = new SimpleStringProperty(mName);
        this.phoneNo = new SimpleStringProperty(mPhone);
        this.password = new SimpleStringProperty(mPassword);
    }

    public SimpleStringProperty indexProperty() {
        return index;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty phoneNoProperty() {
        return phoneNo;
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public String getIndex() {
        return index.get();
    }

    public String getName() {
        return name.get();
    }

    public String getPhoneNo() {
        return phoneNo.get();
    }

    public String getPassword() {
        return password.get();
    }
}