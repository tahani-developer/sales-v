package com.dr7.salesmanmanager.Modles;

public class AddedCustomer {
    private String custName;
    private String remark;
    private double latitude;
    private double longtitude;

    public AddedCustomer(){

    }

    public AddedCustomer(String custName, String remark, double latitude, double longtitude) {
        this.custName = custName;
        this.remark = remark;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
}
