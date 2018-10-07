package com.dr7.salesmanmanager.Modles;

public class PriceListD {

    private int companyNo;
    private int prNo;
    private String itemNo;
    private String unitId;
    private double price;
    private double taxPerc;

    public PriceListD(){

    }

    public PriceListD(int companyNo, int prNo, String itemNo, String unitId,
                      double price, double taxPerc) {
        this.companyNo = companyNo;
        this.prNo = prNo;
        this.itemNo = itemNo;
        this.unitId = unitId;
        this.price = price;
        this.taxPerc = taxPerc;
    }

    public int getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(int companyNo) {
        this.companyNo = companyNo;
    }

    public int getPrNo() {
        return prNo;
    }

    public void setPrNo(int prNo) {
        this.prNo = prNo;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTaxPerc() {
        return taxPerc;
    }

    public void setTaxPerc(double taxPerc) {
        this.taxPerc = taxPerc;
    }
}
