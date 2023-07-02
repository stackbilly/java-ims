package com.stacklink.inventory_management_system;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Expenses {
    private SimpleStringProperty expenseID;
    private SimpleStringProperty expenseName;
    private SimpleStringProperty cost;
    private SimpleStringProperty date;
    private SimpleStringProperty index;

    public Expenses(String index, String expenseID, String date, String expenseName, String cost){
        this.index = new SimpleStringProperty(index);
        this.expenseID = new SimpleStringProperty(expenseID);
        this.expenseName = new SimpleStringProperty(expenseName);
        this.cost = new SimpleStringProperty(cost);
        this.date = new SimpleStringProperty(date);
    }

    public SimpleStringProperty expenseIDProperty() {
        return expenseID;
    }

    public SimpleStringProperty indexProperty() {
        return index;
    }

    public StringProperty expenseNameProperty(){
        return expenseName;
    }

    public StringProperty costProperty(){
        return cost;
    }

    public StringProperty dateProperty(){
        return date;
    }

    public String getExpenseName(){
        return expenseName.get();
    }

    public String getExpenseID() {
        return expenseID.get();
    }

    public String getCost(){
        return cost.get();
    }

    public String getDate(){
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }
}
