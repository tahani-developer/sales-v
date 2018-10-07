package com.dr7.salesmanmanager.Modles;

public class ItemsMaster {

    private int companyNo;
    private String itemNo;
    private String name;
    private String categoryId;
    private String barcode;
    private int isSuspended;

    public ItemsMaster (){

    }

    public ItemsMaster(int companyNo, String itemNo, String name, String categoryId,
                       String barcode, int isSuspended) {
        this.companyNo = companyNo;
        this.itemNo = itemNo;
        this.name = name;
        this.categoryId = categoryId;
        this.barcode = barcode;
        this.isSuspended = isSuspended;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(int isSuspended) {
        this.isSuspended = isSuspended;
    }
}
