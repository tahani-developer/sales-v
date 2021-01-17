package com.dr7.salesmanmanager.Modles;

public class CustomerPrice {
    private String itemNumber;
    private int customerNumber;
    private double price;
    private double discount;
    private String other_Discount;
    private String fromDate;
    private String toDate;
    private String listNo;
    private String listType;

    public String getOther_Discount() {
        return other_Discount;
    }

    public void setOther_Discount(String other_Discount) {
        this.other_Discount = other_Discount;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getListNo() {
        return listNo;
    }

    public void setListNo(String listNo) {
        this.listNo = listNo;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

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
