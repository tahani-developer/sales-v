package com.dr7.salesmanmanager.Modles;

public class CustomerPrice {
    private int itemNumber;
    private int customerNumber;
    private double price;

    public CustomerPrice(){

    }

    public CustomerPrice(int itemNumber, int customerNumber, double price) {
        this.itemNumber = itemNumber;
        this.customerNumber = customerNumber;
        this.price = price;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public int getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
