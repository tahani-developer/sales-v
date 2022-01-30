package com.dr7.salesmanmanager.Modles;

public class SMItemAvailability {
    int SalManNo;
    String itemOcode;
    int availability;

    public int getSalManNo() {
        return SalManNo;
    }

    public void setSalManNo(int salManNo) {
        SalManNo = salManNo;
    }

    public String getItemOcode() {
        return itemOcode;
    }

    public void setItemOcode(String itemOcode) {
        this.itemOcode = itemOcode;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }
}
