package com.dr7.salesmanmanager.Modles;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SalesManItemsBalance {

    private int companyNo;
    private String salesManNo;
    private String itemNo;
    private double qty;

    public SalesManItemsBalance(){

    }

    public SalesManItemsBalance(int companyNo, String salesManNo, String itemNo, double qty) {
        this.companyNo = companyNo;
        this.salesManNo = salesManNo;
        this.itemNo = itemNo;
        this.qty = qty;
    }

    public int getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(int companyNo) {
        this.companyNo = companyNo;
    }

    public String getSalesManNo() {
        return salesManNo;
    }

    public void setSalesManNo(String salesManNo) {
        this.salesManNo = salesManNo;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }
    Date currentTimeAndDate = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    String today = convertToEnglish( df.format(currentTimeAndDate));


//    public JSONObject getObj() {
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("date",today);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return obj;
//    }

    // VANCODE,LOADDATE,LOADQTY,ITEMCODE,NETQTY".
    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("VANCODE", salesManNo);
            obj.put("ITEMCODE", itemNo);
            obj.put("LOADQTY", qty);
            obj.put("LOADDATE",today);
        } catch (JSONException e) {
            Log.e("TagSalesmanBalance" , "JSONException");
        }
        return obj;
    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0"));
        return newValue;
    }
}
