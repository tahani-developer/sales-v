package com.dr7.salesmanmanager.Modles;

public class ItemUnitDetails {

    private int companyNo;
    private String itemNo;
    private String unitId;
    private double convRate;

    public ItemUnitDetails (){

    }

    public ItemUnitDetails(int companyNo, String itemNo, String unitId, double convRate) {
        this.companyNo = companyNo;
        this.itemNo = itemNo;
        this.unitId = unitId;
        this.convRate = convRate;
    }

    public int getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(int companyNo) {
        this.companyNo = companyNo;
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

    public double getConvRate() {
        return convRate;
    }

    public void setConvRate(double convRate) {
        this.convRate = convRate;
    }
}
