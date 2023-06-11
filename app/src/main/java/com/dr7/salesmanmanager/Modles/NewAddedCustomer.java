package com.dr7.salesmanmanager.Modles;

public class NewAddedCustomer {
    private String custName;
    private String remark;
    private String latitude;
    private String longtitude;
    private String salesMan;
    private String salesmanNo;
    private String isPosted;
    private String ADRESS_CUSTOMER;
    private String     TELEPHONE;
    private String CONTACT_PERSON;
    private String  MarketName;
    private String  date;
    private String  time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMarketName() {
        return MarketName;
    }

    public void setMarketName(String marketName) {
        MarketName = marketName;
    }

    public String getADRESS_CUSTOMER() {
        return ADRESS_CUSTOMER;
    }

    public void setADRESS_CUSTOMER(String ADRESS_CUSTOMER) {
        this.ADRESS_CUSTOMER = ADRESS_CUSTOMER;
    }

    public String getTELEPHONE() {
        return TELEPHONE;
    }

    public void setTELEPHONE(String TELEPHONE) {
        this.TELEPHONE = TELEPHONE;
    }

    public String getCONTACT_PERSON() {
        return CONTACT_PERSON;
    }

    public void setCONTACT_PERSON(String CONTACT_PERSON) {
        this.CONTACT_PERSON = CONTACT_PERSON;
    }

    public NewAddedCustomer(){

    }


    public NewAddedCustomer(String custName, String remark, String latitude, String longtitude, String salesMan, String salesmanNo, String isPosted, String ADRESS_CUSTOMER, String TELEPHONE, String CONTACT_PERSON,String MarketName,String date,String time) {
        this.custName = custName;
        this.remark = remark;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.salesMan = salesMan;
        this.salesmanNo = salesmanNo;
        this.isPosted = isPosted;
        this.ADRESS_CUSTOMER = ADRESS_CUSTOMER;
        this.TELEPHONE = TELEPHONE;
        this.CONTACT_PERSON = CONTACT_PERSON;
      this.MarketName=  MarketName;
        this.date=date;
        this.time=time;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getSalesMan() {
        return salesMan;
    }

    public void setSalesMan(String salesMan) {
        this.salesMan = salesMan;
    }

    public String getSalesmanNo() {
        return salesmanNo;
    }

    public void setSalesmanNo(String salesmanNo) {
        this.salesmanNo = salesmanNo;
    }

    public String getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(String isPosted) {
        this.isPosted = isPosted;
    }
}
