package com.stacklink.inventory_management_system;

import javafx.beans.property.SimpleStringProperty;

public class User {
<<<<<<< HEAD
    private final SimpleStringProperty index;
    private final SimpleStringProperty name;
    private final SimpleStringProperty phoneNo;
    private final SimpleStringProperty password;
=======
    private SimpleStringProperty index;
    private SimpleStringProperty name;
    private SimpleStringProperty phoneNo;
    private SimpleStringProperty password;
>>>>>>> da3a5cacc422ab16ab62dcc478c4ab2695cfe559

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

<<<<<<< HEAD
=======
    public String getIndex() {
        return index.get();
    }

>>>>>>> da3a5cacc422ab16ab62dcc478c4ab2695cfe559
    public String getName() {
        return name.get();
    }

<<<<<<< HEAD
=======
    public String getPhoneNo() {
        return phoneNo.get();
    }

    public String getPassword() {
        return password.get();
    }
>>>>>>> da3a5cacc422ab16ab62dcc478c4ab2695cfe559
}