package com.stacklink.inventory_management_system;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Category {
    private final SimpleStringProperty categoryName;
    private final SimpleStringProperty type;
    private final SimpleStringProperty index;

    public Category(String index, String categoryName, String type){
        this.categoryName = new SimpleStringProperty(categoryName);
        this.index = new SimpleStringProperty(index);
        this.type = new SimpleStringProperty(type);
    }

    public StringProperty categoryNameProperty(){return categoryName;}
    public StringProperty getIndexProperty(){return index;}

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public String getCategoryName() {
        return categoryName.get();
    }

}
