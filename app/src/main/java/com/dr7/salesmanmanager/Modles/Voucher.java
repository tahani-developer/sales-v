package com.dr7.salesmanmanager.Modles;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Voucher {


    private int companyNumber;
    private String custName ;
    private String custNumber ;
    private int voucherNumber;
    private int voucherType;
    private String VoucherDate;
    private int saleManNumber;
    private double voucherDiscount;
    private double totalVoucherDiscount;
    private double subTotal;
    private double tax;
    private double netSales;
    private double voucherDiscountPercent;
    private String remark;
    private int payMethod;
    private int isPosted;
    private double totalQty;
    private int voucherYear;

    // Empty constructor
    public Voucher() {
    }

    // constructor
    public Voucher(int companyNumber, int voucherNumber, int voucherType, String VoucherDate, int saleManNumber,
                   double voucherDiscount, double voucherDiscountPercent, String remark, int payMethod, int isPosted ,
                   double totalVoucherDiscount , double subTotal ,double tax , double netSales , String custName ,
                   String custNumber , int voucherYear) {
        this.companyNumber = companyNumber;
        this.voucherNumber = voucherNumber;
        this.voucherType = voucherType;
        this.VoucherDate = VoucherDate;
        this.saleManNumber = saleManNumber;
        this.voucherDiscount = voucherDiscount;
        this.voucherDiscountPercent = voucherDiscountPercent;
        this.remark = remark;
        this.payMethod = payMethod;
        this.isPosted = isPosted;
        this.totalVoucherDiscount = totalVoucherDiscount;
        this.subTotal = subTotal;
        this.tax = tax;
        this.netSales = netSales;
        this.custName = custName;
        this.custNumber = custNumber;
        this.voucherYear = voucherYear;
    }

    public Voucher(int companyNumber, int voucherNumber, String VoucherDate, int saleManNumber,
                   String remark, double totalQty, int isPosted ) {
        this.companyNumber = companyNumber;
        this.voucherNumber = voucherNumber;
        this.VoucherDate = VoucherDate;
        this.saleManNumber = saleManNumber;
        this.remark = remark;
        this.totalQty = totalQty;
        this.isPosted = isPosted;
    }

    public int getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(int companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustNumber() {
        return custNumber;
    }

    public void setCustNumber(String custNumber) {
        this.custNumber = custNumber;
    }

    public int getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(int voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public int getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(int voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherDate() {
        return VoucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        VoucherDate = voucherDate;
    }

    public int getSaleManNumber() {
        return saleManNumber;
    }

    public void setSaleManNumber(int saleManNumber) {
        this.saleManNumber = saleManNumber;
    }

    public double getVoucherDiscount() {
        return voucherDiscount;
    }

    public void setVoucherDiscount(double voucherDiscount) {
        this.voucherDiscount = voucherDiscount;
    }

    public double getVoucherDiscountPercent() {
        return voucherDiscountPercent;
    }

    public void setVoucherDiscountPercent(double voucherDiscountPercent) {
        this.voucherDiscountPercent = voucherDiscountPercent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public int getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(int isPosted) {
        this.isPosted = isPosted;
    }

    public double getTotalVoucherDiscount() {
        return totalVoucherDiscount;
    }

    public void setTotalVoucherDiscount(double totalVoucherDiscount) {
        this.totalVoucherDiscount = totalVoucherDiscount;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getNetSales() {
        return netSales;
    }

    public void setNetSales(double netSales) {
        this.netSales = netSales;
    }

    public double getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(double totalQty) {
        this.totalQty = totalQty;
    }

    public int getVoucherYear() {
        return voucherYear;
    }

    public void setVoucherYear(int voucherYear) {
        this.voucherYear = voucherYear;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
//        String swapedDate = ""+ VoucherDate.charAt(8) + VoucherDate.charAt(9) + "/" + VoucherDate.charAt(5) + VoucherDate.charAt(6)
//                + "/" + VoucherDate.charAt(0) + VoucherDate.charAt(1) + VoucherDate.charAt(2) + VoucherDate.charAt(3);
        try {
            obj.put("companyNumber", companyNumber);
            obj.put("voucherNumber", voucherNumber);
            obj.put("voucherType", voucherType);
            obj.put("voucherDate", VoucherDate);
            obj.put("saleManNumber", saleManNumber);
            obj.put("voucherDiscount", voucherDiscount);
            obj.put("voucherDiscountPercent", voucherDiscountPercent);
            obj.put("remark", remark);
            obj.put("payMethod", payMethod);
            obj.put("isPosted", isPosted);
            obj.put("totalVoucherDiscount", totalVoucherDiscount);
            obj.put("subTotal", subTotal);
            obj.put("tax", tax);
            obj.put("netSales", netSales);
            obj.put("custName", custName);
            obj.put("custNumber", custNumber);
            obj.put("voucherYear", voucherYear);
            obj.put("totalQty", totalQty);

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
            obj.put("voucherDate", VoucherDate);
            obj.put("saleManNumber", saleManNumber);
            obj.put("remark", remark);
            obj.put("totalQty", totalQty);
            obj.put("isPosted", isPosted);

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
}