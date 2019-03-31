package com.dr7.salesmanmanager.Modles;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AddedCustomer {
    private String custName;
    private String remark;
    private double latitude;
    private double longtitude;
    private String salesMan;
    private String salesmanNo;
    private int isPosted;

    public AddedCustomer(){

    }

    public AddedCustomer(String custName, String remark, double latitude, double longtitude ,String salesMan ,
                         int isPosted, String salesmanNo) {
        this.custName = custName;
        this.remark = remark;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.salesMan = salesMan;
        this.isPosted = isPosted;
        this.salesmanNo = salesmanNo;
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

    public int getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(int isPosted) {
        this.isPosted = isPosted;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("custName", custName);
            obj.put("remark", remark);
            obj.put("latitude", latitude);
            obj.put("longtitude", longtitude);
            obj.put("salesMan", salesMan);
            obj.put("isPosted", isPosted);
            obj.put("salesmanNo", salesmanNo);

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
}
