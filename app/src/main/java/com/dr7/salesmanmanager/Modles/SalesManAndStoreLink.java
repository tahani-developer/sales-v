package com.dr7.salesmanmanager.Modles;

public class SalesManAndStoreLink {

    private int companyNo;
    private int salesManNo;
    private int storeNo;

    public SalesManAndStoreLink(){

    }

    public SalesManAndStoreLink(int companyNo, int salesManNo, int storeNo) {
        this.companyNo = companyNo;
        this.salesManNo = salesManNo;
        this.storeNo = storeNo;
    }

    public int getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(int companyNo) {
        this.companyNo = companyNo;
    }

    public int getSalesManNo() {
        return salesManNo;
    }

    public void setSalesManNo(int salesManNo) {
        this.salesManNo = salesManNo;
    }

    public int getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(int storeNo) {
        this.storeNo = storeNo;
    }
}
