package com.stacklink.inventory_management_system;

import javafx.beans.property.SimpleStringProperty;

public class Product {
    private SimpleStringProperty productName;
    private SimpleStringProperty productCode;
    private SimpleStringProperty category;
    private SimpleStringProperty quantity;
    private SimpleStringProperty costPrice;
    private SimpleStringProperty salePrice;
    private SimpleStringProperty dateAdded;
    private SimpleStringProperty index;
    private SimpleStringProperty total;
    private SimpleStringProperty expectedSales;

    public SimpleStringProperty description;

    public Product() {}

    public Product(String index, String productName, String description, String productCode, String category, int quantity, int costPrice
    ,int salePrice, String dateAdded, int total, int expectedSales){
        this.productName = new SimpleStringProperty(productName);
        this.productCode = new SimpleStringProperty(productCode);
        this.category = new SimpleStringProperty(category);
        this.quantity = new SimpleStringProperty(String.valueOf(quantity));
        this.salePrice = new SimpleStringProperty(String.valueOf(salePrice));
        this.costPrice = new SimpleStringProperty(String.valueOf(costPrice));
        this.dateAdded = new SimpleStringProperty(dateAdded);
        this.index = new SimpleStringProperty(index);
        this.total = new SimpleStringProperty(String.valueOf(total));
        this.expectedSales = new SimpleStringProperty(String.valueOf(expectedSales));
        this.description = new SimpleStringProperty(description);
    }

    public SimpleStringProperty salePriceProperty() {
        return salePrice;
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public SimpleStringProperty quantityProperty() {
        return quantity;
    }

    public SimpleStringProperty costPriceProperty() {
        return costPrice;
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public SimpleStringProperty productCodeProperty() {
        return productCode;
    }


    public SimpleStringProperty dateAddedProperty() {
        return dateAdded;
    }

    public SimpleStringProperty expectedSalesProperty() {
        return expectedSales;
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public SimpleStringProperty totalProperty() {
        return total;
    }

    public SimpleStringProperty indexProperty() {
        return index;
    }

    public String getProductName() {
        return productName.get();
    }

    public String getProductCode() {
        return productCode.get();
    }

    public String getQuantity() {
        return quantity.get();
    }

    public String getCategory() {
        return category.get();
    }

    public String getCostPrice() {
        return costPrice.get();
    }

    public String getDateAdded() {
        return dateAdded.get();
    }


    public String getSalePrice() {
        return salePrice.get();
    }

    public String getDescription(){
        return description.get();
    }

}
