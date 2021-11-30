package com.dr7.salesmanmanager.Modles;

public class ItemUnitDetails {

    private String companyNo;
    private String itemNo;
    private String unitId;
    private double convRate;
    private String unitPrice;
    private String itemBarcode;
    private String priceClass_1;
    private String priceClass_2;
    private String priceClass_3;

    public String getPriceClass_1() {
        return priceClass_1;
    }

    public void setPriceClass_1(String priceClass_1) {
        this.priceClass_1 = priceClass_1;
    }

    public String getPriceClass_2() {
        return priceClass_2;
    }

    public void setPriceClass_2(String priceClass_2) {
        this.priceClass_2 = priceClass_2;
    }

    public String getPriceClass_3() {
        return priceClass_3;
    }

    public void setPriceClass_3(String priceClass_3) {
        this.priceClass_3 = priceClass_3;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

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
