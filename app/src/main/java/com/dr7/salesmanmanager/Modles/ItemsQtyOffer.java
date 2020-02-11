package com.dr7.salesmanmanager.Modles;

public class ItemsQtyOffer {
    private String item_name;
    private String item_no;
    private double itemQty;
    private double discount_value;
    private String fromDate;
    private String toDate;

    public ItemsQtyOffer(String item_name, String item_no, double itemQty, double discount_value, String fromDate, String toDate) {
        this.item_name = item_name;
        this.item_no = item_no;
        this.itemQty = itemQty;
        this.discount_value = discount_value;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public ItemsQtyOffer() {
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_no() {
        return item_no;
    }

    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }

    public double getItemQty() {
        return itemQty;
    }

    public void setItemQty(double itemQty) {
        this.itemQty = itemQty;
    }

    public double getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(double discount_value) {
        this.discount_value = discount_value;
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
}
