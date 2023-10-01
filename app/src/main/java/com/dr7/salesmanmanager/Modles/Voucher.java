package com.dr7.salesmanmanager.Modles;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

import static com.dr7.salesmanmanager.ExportJason.CONO;

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
    private String time;
    private int ORIGINALvoucherNo;
    private  int taxTypa;
    private  int vocherDamage;

    public int getVocherDamage() {
        return vocherDamage;
    }

    public void setVocherDamage(int vocherDamage) {
        this.vocherDamage = vocherDamage;
    }

    public int getTaxTypa() {
        return taxTypa;
    }

    public void setTaxTypa(int taxTypa) {
        this.taxTypa = taxTypa;
    }

    public int getORIGINALvoucherNo() {
        return ORIGINALvoucherNo;
    }

    public void setORIGINALvoucherNo(int ORIGINALvoucherNo) {
        this.ORIGINALvoucherNo = ORIGINALvoucherNo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // Empty constructor
    public Voucher() {
    }

    // constructor
    public Voucher(int companyNumber, int voucherNumber, int voucherType, String VoucherDate, int saleManNumber,
                   double voucherDiscount, double voucherDiscountPercent, String remark, int payMethod, int isPosted ,
                   double totalVoucherDiscount , double subTotal ,double tax , double netSales , String custName ,
                   String custNumber , int voucherYear, String Time) {
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
        this.time=Time;
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
    public JSONObject getJSONObjectDelphi() {
        JSONObject obj = new JSONObject();
        String voucherDateFormet="";
        //"JSN":[{"COMAPNYNO":290,"VOUCHERYEAR":"2021","VOUCHERNO":"1212","VOUCHERTYPE":"3","VOUCHERDATE":"24/03/2020",
          //      "SALESMANNO":"5","CUSTOMERNO":"123456","VOUCHERDISCOUNT":"50",
            //    "VOUCHERDISCOUNTPERCENT":"10","NOTES":"AAAAAA","CACR":"1","ISPOSTED":"0","PAYMETHOD":"1","NETSALES":"150.720"}]}
        try {
            obj.put("COMAPNYNO", CONO);
            obj.put("VOUCHERNO", voucherNumber);
            obj.put("VOUCHERTYPE", voucherType);

            obj.put("VOUCHERDATE", VoucherDate);
            obj.put("SALESMANNO", saleManNumber);
            obj.put("VOUCHERDISCOUNT", voucherDiscount);
            obj.put("VOUCHERDISCOUNTPERCENT", voucherDiscountPercent);
            if(remark==null)
            {
                obj.put("NOTES", "");
            }else obj.put("NOTES", remark);

            obj.put("CACR", payMethod);
            obj.put("ISPOSTED", "0");
            obj.put("NETSALES", netSales);
            obj.put("CUSTOMERNO", custNumber);
            obj.put("VOUCHERYEAR", voucherYear);

            obj.put("PAYMETHOD", payMethod);
            obj.put("EXCINC", taxTypa);


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