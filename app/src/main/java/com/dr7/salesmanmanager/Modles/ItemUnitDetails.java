package com.dr7.salesmanmanager.Modles;

public class ItemUnitDetails {

    private String companyNo;
    private String itemNo;
    private String unitId;
    private double convRate;

    public ItemUnitDetails (){

    }

    public ItemUnitDetails(String companyNo, String itemNo, String unitId, double convRate) {
        this.companyNo = companyNo;
        this.itemNo = itemNo;
        this.unitId = unitId;
        this.convRate = convRate;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
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
