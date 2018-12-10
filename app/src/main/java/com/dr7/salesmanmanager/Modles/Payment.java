package com.dr7.salesmanmanager.Modles;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Payment {


    private int companyNumber;
    private int voucherNumber;
    private String payDate;
    private String custNumber;
    private String custName;
    private double amount;
    private String remark;
    private int saleManNumber;
    private int isPosted;
    private String bank;
    private int checkNumber;
    private String dueDate;
    private int payMethod;
    private int year;

    // Empty constructor
    public Payment() {
    }

    // constructor for payment
    public Payment(int companyNumber, int voucherNumber, int saleManNumber,
                   String payDate, String remark, double amount, int isPosted, String custNumber ,
                   String custName,int payMethod , int year) {
        this.companyNumber = companyNumber;
        this.voucherNumber = voucherNumber;
        this.saleManNumber = saleManNumber;
        this.remark = remark;
        this.amount = amount;
        this.isPosted = isPosted;
        this.payDate = payDate;
        this.custNumber = custNumber;
        this.custName = custName;
        this.payMethod = payMethod;
        this.year = year;
    }

    // constructor for payment paper
    public Payment(int companyNumber, int voucherNumber, int checkNumber,
                   String bank, String dueDate, double amount, int isPosted , int year) {
        this.companyNumber = companyNumber;
        this.voucherNumber = voucherNumber;
        this.checkNumber = checkNumber;
        this.bank = bank;
        this.dueDate = dueDate;
        this.amount = amount;
        this.isPosted = isPosted;
        this.year = year;
    }


    public int getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(int voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public int getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(int companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getCustNumber() {
        return custNumber;
    }

    public void setCustNumber(String custNumber) {
        this.custNumber = custNumber;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public int getSaleManNumber() {
        return saleManNumber;
    }

    public void setSaleManNumber(int saleManNumber) {
        this.saleManNumber = saleManNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(int isPosted) {
        this.isPosted = isPosted;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public int getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(int checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("companyNumber", companyNumber);
            obj.put("voucherNumber", voucherNumber);
            obj.put("payDate", payDate);
            obj.put("custNumber", custNumber);
            obj.put("amount", amount);
            obj.put("remark", remark);
            obj.put("saleManNumber", saleManNumber);
            obj.put("isPosted", isPosted);
            obj.put("payMethod", payMethod);
            obj.put("custName", custName);
            obj.put("payYear", year);

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }

    public JSONObject getJSONObject2() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("companyNumber", companyNumber);
            obj.put("voucherNumber", voucherNumber);
            obj.put("checkNumber", checkNumber);
            obj.put("bank", bank);
            obj.put("dueDate", dueDate);
            obj.put("amount", amount);
            obj.put("isPosted", isPosted);
            obj.put("payYear", year);

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
}