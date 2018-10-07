package com.dr7.salesmanmanager.Modles;

public class SalesManItemsBalance {

    private int companyNo;
    private String salesManNo;
    private String itemNo;
    private double qty;

    public SalesManItemsBalance(){

    }

    public SalesManItemsBalance(int companyNo, String salesManNo, String itemNo, double qty) {
        this.companyNo = companyNo;
        this.salesManNo = salesManNo;
        this.itemNo = itemNo;
        this.qty = qty;
    }

    public int getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(int companyNo) {
        this.companyNo = companyNo;
    }

    public String getSalesManNo() {
        return salesManNo;
    }

    public void setSalesManNo(String salesManNo) {
        this.salesManNo = salesManNo;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }
}
