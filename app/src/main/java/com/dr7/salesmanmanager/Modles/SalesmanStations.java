package com.dr7.salesmanmanager.Modles;

public class SalesmanStations {
    private String salesmanNo;
    private String date;
    private String latitude;
    private String longitude;
    private int serial;
    private String custNo;
    private String custName;

    public SalesmanStations(String salesmanNo, String date, String latitude, String longitude, int serial, String custNo, String custName) {
        this.salesmanNo = salesmanNo;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.serial = serial;
        this.custNo = custNo;
        this.custName = custName;
    }

    public SalesmanStations() {

    }

    public String getSalesmanNo() {
        return salesmanNo;
    }

    public void setSalesmanNo(String salesmanNo) {
        this.salesmanNo = salesmanNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }
}
