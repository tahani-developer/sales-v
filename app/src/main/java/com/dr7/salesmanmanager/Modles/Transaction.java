package com.dr7.salesmanmanager.Modles;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Transaction {

    //private variables

    int salesManId;
    String cusCode;
    String cusName;
    String checkInDate;
    String checkInTime;
    String checkOutDate;
    String checkOutTime;
    int status;

    // Empty constructor
    public Transaction() { }

    // constructor
    public Transaction(int salesManId , String cusCode, String cusName, String checkInDate,
                       String checkInTime, String checkOutDate, String checkOutTime, int status) {
        this.salesManId = salesManId ;
        this.cusCode = cusCode ;
        this.cusName = cusName ;
        this.checkInDate = checkInDate ;
        this.checkInTime = checkInTime ;
        this.checkOutDate = checkOutDate;
        this.checkOutTime = checkOutTime ;
        this.status = status ;
    }

    public int getSalesManId() {
        return salesManId;
    }

    public void setSalesManId(int salesManId) {
        this.salesManId = salesManId;
    }

    public String getCusCode() {
        return cusCode;
    }

    public void setCusCode(String cusCode) {
        this.cusCode = cusCode;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("salesManId", salesManId);
            obj.put("cusCode", cusCode);
            obj.put("cusName", cusName);
            obj.put("checkInDate", checkInDate);
            obj.put("checkInTime", checkInTime);
            obj.put("checkOutDate", checkOutDate);
            obj.put("checkOutTime", checkOutTime);
            obj.put("status", status);

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
}
