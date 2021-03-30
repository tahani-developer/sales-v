package com.dr7.salesmanmanager.Modles;

public class PriceListM {

    private String companyNo;
    private int prNo;
    private String describtion;
    private int isSuspended;

    public PriceListM(){

    }

    public PriceListM(String companyNo, int prNo, String describtion, int isSuspended) {
        this.companyNo = companyNo;
        this.prNo = prNo;
        this.describtion = describtion;
        this.isSuspended = isSuspended;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public int getPrNo() {
        return prNo;
    }

    public void setPrNo(int prNo) {
        this.prNo = prNo;
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    public int getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(int isSuspended) {
        this.isSuspended = isSuspended;
    }
}
