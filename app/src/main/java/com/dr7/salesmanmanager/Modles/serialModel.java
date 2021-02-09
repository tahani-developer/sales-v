package com.dr7.salesmanmanager.Modles;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class serialModel {
    private String  serialCode;
    private  int  counterSerial;
    private String voucherNo;
    private String itemNo;
    private String dateVoucher;
    private String kindVoucher;
    private String storeNo;
    private  String isPosted;
    private  String isBonus;
    private  String qty;

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getIsBonus() {
        return isBonus;
    }

    public void setIsBonus(String isBonus) {
        this.isBonus = isBonus;
    }

    public String getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(String isPosted) {
        this.isPosted = isPosted;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getDateVoucher() {
        return dateVoucher;
    }

    public void setDateVoucher(String dateVoucher) {
        this.dateVoucher = dateVoucher;
    }

    public String getKindVoucher() {
        return kindVoucher;
    }

    public void setKindVoucher(String kindVoucher) {
        this.kindVoucher = kindVoucher;
    }

    public serialModel() {
    }

    public serialModel(String serialCode, int counterSerial) {
        this.serialCode = serialCode;
        this.counterSerial = counterSerial;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public int getCounterSerial() {
        return counterSerial;
    }

    public void setCounterSerial(int counterSerial) {
        this.counterSerial = counterSerial;
    }



    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {

            obj.put("SERIAL_CODE", serialCode);
            obj.put("QTY", "1");
            obj.put("STORENO", storeNo);// store
            obj.put("VSERIAL", counterSerial);
            obj.put("VHFNO", voucherNo);
            obj.put("ITEMNO", itemNo);
            obj.put("TRNSDATE", dateVoucher);
            obj.put("TRANSKIND", kindVoucher);
            obj.put("ISPOSTED", "0");


        } catch (JSONException e) {
            Log.e("TagserialModel" , "JSONException"+e.getMessage());
        }
        return obj;
    }
}
