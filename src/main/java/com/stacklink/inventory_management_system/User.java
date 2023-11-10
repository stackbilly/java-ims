package com.stacklink.inventory_management_system;

import javafx.beans.property.SimpleStringProperty;

public class User {
    private final SimpleStringProperty index;
    private final SimpleStringProperty name;
    private final SimpleStringProperty phoneNo;
    private final SimpleStringProperty password;

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

    public String getName() {
        return name.get();
    }

}