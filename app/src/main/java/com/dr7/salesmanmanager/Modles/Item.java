package com.dr7.salesmanmanager.Modles;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mohd darras on 29/12/2017.
 */

public class Item {
    private String itemNo ,itemName , unit;
    private String date ;
    private float  qty;
    private String category;
    private String barcode;
    private float price;
    private float bonus;
    private float disc;
    private float amount;
    private String discPerc;
    private float voucherDiscount;
    private int filling , voucherNumber , voucherType;
    private double taxValue;
    private float taxPercent;
    private double totalDiscVal;
    private int discType;
    private String description;
    private int companyNumber;
    private String year;
    private int isPosted;
    private double itemL;
    private double minSalePrice;
    private double posPrice;
    private String salesmanNo;
    private  String Kind_item;

    public Item()
    {

    }

    public String getKind_item() {
        return Kind_item;
    }

    public void setKind_item(String kind_item) {
        Kind_item = kind_item;
    }

    public Item(String itemNo, String itemName, String unit, String date, float qty, String category, String barcode,
                float price, float bonus, float disc, float amount, String discPerc, float voucherDiscount, int filling,
                int voucherNumber, int voucherType, double taxValue, float taxPercent, double totalDiscVal, int discType, String description,
                int companyNumber, String year, int isPosted,
                double itemL, double minSalePrice, double posPrice, String salesmanNo, String kind_item) {
        this.itemNo = itemNo;
        this.itemName = itemName;
        this.unit = unit;
        this.date = date;
        this.qty = qty;
        this.category = category;
        this.barcode = barcode;
        this.price = price;
        this.bonus = bonus;
        this.disc = disc;
        this.amount = amount;
        this.discPerc = discPerc;
        this.voucherDiscount = voucherDiscount;
        this.filling = filling;
        this.voucherNumber = voucherNumber;
        this.voucherType = voucherType;
        this.taxValue = taxValue;
        this.taxPercent = taxPercent;
        this.totalDiscVal = totalDiscVal;
        this.discType = discType;
        this.description = description;
        this.companyNumber = companyNumber;
        this.year = year;
        this.isPosted = isPosted;
        this.itemL = itemL;
        this.minSalePrice = minSalePrice;
        this.posPrice = posPrice;
        this.salesmanNo = salesmanNo;
        Kind_item = kind_item;
    }

    //constructor for add item recycler

    //constructor for sales invoice
    public Item(int companyNumber , String year ,int voucherNumber , int voucherType , String unit ,String itemNo, String itemName,
                float qty, float price,float disc, String discPerc, float bonus, float voucherDiscount, double taxValue,
                float taxPercent,int isPosted,String description) {
        this.companyNumber = companyNumber;
        this.year = year;
        this.voucherNumber = voucherNumber;
        this.voucherType = voucherType;
        this.itemNo = itemNo;
        this.itemName = itemName;
        this.unit = unit;
        this.qty = qty;
        this.price = price;
        this.disc = disc;
        this.discPerc = discPerc;
        this.bonus = bonus;
        this.voucherDiscount = voucherDiscount;
        this.taxValue = taxValue;
        this.taxPercent = taxPercent;
        this.isPosted = isPosted;
        this.description=description;
    }

    //constructor for stock request
    public Item( int companyNumber,int voucherNumber , String itemNo, String itemName, float qty , String date) {
        this.companyNumber = companyNumber;
        this.voucherNumber = voucherNumber;
        this.itemNo = itemNo;
        this.itemName = itemName;
        this.qty = qty;
        this.date = date;
    }




    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDiscPerc() {
        return discPerc;
    }

    public void setDiscPerc(String discPerc) {
        this.discPerc = discPerc;
    }

    public float getVoucherDiscount() {
        return voucherDiscount;
    }

    public void setVoucherDiscount(float voucherDiscout) {
        this.voucherDiscount = voucherDiscout;
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

    public void setTaxPercent(float taxPercent) {
        this.taxPercent = taxPercent;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getDescription() {
        return description;
    }

    public void setDescreption(String description) {
        this.description = description;
    }
    public void setDiscType(int discType){ this.discType = discType;}

    public int getDiscType(){ return discType; }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public void setTotalDiscVal(double totalDiscVal)
    {
        this.totalDiscVal = totalDiscVal;
    }

    public double getTotalDiscVal()
    {
        return this.totalDiscVal;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getQty() {
        return qty;
    }

    public float getTaxPercent() {
        return taxPercent;
    }

    public double getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(double taxValue) {
        this.taxValue = taxValue;
    }

    public float getFilling() {
        return filling;
    }

    public float getDisc() {
        return disc;
    }

    public float getAmount() {
        return amount;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setDisc(float disc) {
        this.disc = disc;
    }

    public void setTax(float taxPercent) {
        this.taxPercent = taxPercent;
    }

    public void setFilling(int filling) {
        this.filling = filling;
    }

    public float getPrice() {
        Log.e("price Item",""+price);
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getBonus() {
        return bonus;
    }

    public void setBonus(float bonus) {
        this.bonus = bonus;
    }

    public int getCompanyNumber() {
        return companyNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setCompanyNumber(int companyNumber) {
        this.companyNumber = companyNumber;
    }

    public int getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(int isPosted) {
        this.isPosted = isPosted;
    }

    public double getItemL() {
        return itemL;
    }

    public void setItemL(double itemL) {
        this.itemL = itemL;
    }

    public double getMinSalePrice() {
        return minSalePrice;
    }

    public void setMinSalePrice(double minSalePrice) {
        this.minSalePrice = minSalePrice;
    }

    public double getPosPrice() {
        return posPrice;
    }

    public void setPosPrice(double posPrice) {
        this.posPrice = posPrice;
    }

    public String getSalesmanNo() {
        return salesmanNo;
    }

    public void setSalesmanNo(String salesmanNo) {
        this.salesmanNo = salesmanNo;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("voucherNumber", voucherNumber);
            obj.put("voucherType", voucherType);
            obj.put("itemNo", itemNo);
            obj.put("itemName", itemName);
            obj.put("unit", unit);
            obj.put("unitQty", qty);
            obj.put("price", price);
            obj.put("bonus", bonus);
            obj.put("itemDiscount", disc);
            obj.put("itemDiscountPercent", discPerc);
            obj.put("voucherDiscount", voucherDiscount);
            obj.put("taxValue", taxValue);
            obj.put("taxPercent", taxPercent);
            obj.put("companyNumber", companyNumber);
            obj.put("isPosted", isPosted);
            obj.put("itemYear", year);
            obj.put("ITEM_DESCRITION", description);


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
            obj.put("itemNo", itemNo);
            obj.put("itemName", itemName);
            obj.put("unitQty", qty);
            obj.put("voucherDate", date);

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
}
