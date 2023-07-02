package com.stacklink.inventory_management_system;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Category {
    private SimpleStringProperty categoryName;
    private SimpleStringProperty status;
    private SimpleStringProperty index;

    public Category(String index, String categoryName, String status){
        this.categoryName = new SimpleStringProperty(categoryName);
        this.index = new SimpleStringProperty(index);
        this.status = new SimpleStringProperty(status);
    }

    public StringProperty categoryNameProperty(){return categoryName;}
    public StringProperty getIndexProperty(){return index;}

    public SimpleStringProperty statusProperty() {
        return status;
    }
}
