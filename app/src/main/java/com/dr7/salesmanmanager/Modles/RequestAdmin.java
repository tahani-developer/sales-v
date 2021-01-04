package com.dr7.salesmanmanager.Modles;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestAdmin {
   private String  salesman_name;
    private String salesman_no;
    private String customer_name;
    private String customer_no;
    private String request_type;
    private String amount_value;
    private String voucher_no;
    private String total_voucher;
    private String status;
    private String key_validation ;
    private String date;
    private String time ;
    private String note ;
    private String seen_row ;

    public RequestAdmin() {
    }

    public String getSalesman_name() {
        return salesman_name;
    }

    public void setSalesman_name(String salesman_name) {
        this.salesman_name = salesman_name;
    }

    public String getSalesman_no() {
        return salesman_no;
    }

    public void setSalesman_no(String salesman_no) {
        this.salesman_no = salesman_no;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_no() {
        return customer_no;
    }

    public void setCustomer_no(String customer_no) {
        this.customer_no = customer_no;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getAmount_value() {
        return amount_value;
    }

    public void setAmount_value(String amount_value) {
        this.amount_value = amount_value;
    }

    public String getVoucher_no() {
        return voucher_no;
    }

    public void setVoucher_no(String voucher_no) {
        this.voucher_no = voucher_no;
    }

    public String getTotal_voucher() {
        return total_voucher;
    }

    public void setTotal_voucher(String total_voucher) {
        this.total_voucher = total_voucher;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey_validation() {
        return key_validation;
    }

    public void setKey_validation(String key_validation) {
        this.key_validation = key_validation;
    }

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSeen_row() {
        return seen_row;
    }

    public void setSeen_row(String seen_row) {
        this.seen_row = seen_row;
    }
    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {

            obj.put("salesman_name", salesman_name);
            obj.put("salesman_no", salesman_no);
            obj.put("customer_name", customer_name);// store
            obj.put("customer_no", customer_no);
            obj.put("request_type", request_type);
            obj.put("amount_value", amount_value);
            obj.put("voucher_no", voucher_no);
            obj.put("total_voucher", total_voucher);
            obj.put("status", status);
            obj.put("key_validation", key_validation);
            obj.put("date_request", date);
            obj.put("time_request", time);
//            obj.put("note", "");
            obj.put("note", note);
            obj.put("seen_row", seen_row);


        } catch (JSONException e) {
            Log.e("TagserialModel" , "JSONException"+e.getMessage());
        }
        return obj;
    }
}
