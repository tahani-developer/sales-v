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
    int isPosted;
    double latitud;
    double longtude;

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongtude() {
        return longtude;
    }

    public void setLongtude(double longtude) {
        this.longtude = longtude;
    }

    // Empty constructor
    public Transaction() { }

    // constructor
    public Transaction(int salesManId , String cusCode, String cusName, String checkInDate,
                       String checkInTime, String checkOutDate, String checkOutTime, int status, int isPosted) {
        this.salesManId = salesManId ;
        this.cusCode = cusCode ;
        this.cusName = cusName ;
        this.checkInDate = checkInDate ;
        this.checkInTime = checkInTime ;
        this.checkOutDate = checkOutDate;
        this.checkOutTime = checkOutTime ;
        this.status = status ;
        this.isPosted = isPosted;
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

    public int getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(int isPosted) {
        this.isPosted = isPosted;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("SALES_MAN_ID", salesManId);
            obj.put("CUS_CODE", cusCode);
            obj.put("CUS_NAME", cusName);
            obj.put("CHECK_IN_DATE", checkInDate);
            obj.put("CHECK_IN_TIME", checkInTime);
            obj.put("CHECK_OUT_DATE", checkOutDate);
            obj.put("CHECK_OUT_TIME", checkOutTime);
            obj.put("STATUS", status);

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
}
