package com.dr7.salesmanmanager.Modles;

public class CustomerPrice {
    private String itemNumber;
    private int customerNumber;
    private double price;
    private double discount;

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public CustomerPrice(){

    }

    public CustomerPrice(String itemNumber, int customerNumber, double price) {
        this.itemNumber = itemNumber;
        this.customerNumber = customerNumber;
        this.price = price;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
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
