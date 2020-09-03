package com.dr7.salesmanmanager.Modles;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class serialModel {
    private String  serialCode;
    private  int    counterSerial;
    private String voucherNo;
    private String itemNo;
    private String dateVoucher;
    private String kindVoucher;

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

            obj.put("SERIAL_CODE_NO", serialCode);
            obj.put("COUNTER_SERIAL", counterSerial);
            obj.put("VOUCHER_NO", voucherNo);
            obj.put("ITEMNO_SERIAL", itemNo);
            obj.put("DATE_VOUCHER", dateVoucher);
            obj.put("KIND_VOUCHER", kindVoucher);



        } catch (JSONException e) {
            Log.e("TagserialModel" , "JSONException"+e.getMessage());
        }
        return obj;
    }
}
