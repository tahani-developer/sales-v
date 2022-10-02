package com.dr7.salesmanmanager.Modles;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static com.dr7.salesmanmanager.ExportJason.CONO;

/**
 * Created by mohd darras on 29/12/2017.
 */

public class Item implements Serializable {
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
    private  String cust;
    private  String serialCode;
    private  String vouchDate;
    private String itemHasSerial;
    private  double discountCustomer;
    private  double currentQty;


    private String which_unit;
    private String which_unit_str;
    private String whichu_qty;
    private String enter_qty;
    private String enter_price;
    private String unit_barcode;
    private String oneUnitItem;
    private float Avi_Qty;
    private int   IS_RETURNED;
    private int ORIGINALvoucherNo;
    private  int vivible=0;
    private  int noTax_item=0;
    private  float price_noTax;

    public int getNoTax_item() {
        return noTax_item;
    }

    public void setNoTax_item(int noTax_item) {
        this.noTax_item = noTax_item;
    }

    public float getPrice_noTax() {
        return price_noTax;
    }

    public void setPrice_noTax(float price_noTax) {
        this.price_noTax = price_noTax;
    }

    public int getVivible() {
        return vivible;
    }

    public void setVivible(int vivible) {
        this.vivible = vivible;
    }

    public int getORIGINALvoucherNo() {
        return ORIGINALvoucherNo;
    }

    public void setORIGINALvoucherNo(int ORIGINALvoucherNo) {
        this.ORIGINALvoucherNo = ORIGINALvoucherNo;
    }
    public int getIS_RETURNED() {
        return IS_RETURNED;
    }

    public void setIS_RETURNED(int IS_RETURNED) {
        this.IS_RETURNED = IS_RETURNED;
    }

    public float getAvi_Qty() {
        return Avi_Qty;
    }

    public void setAvi_Qty(float avi_Qty) {
        Avi_Qty = avi_Qty;
    }

    public String getOneUnitItem() {
        return oneUnitItem;
    }

    public void setOneUnitItem(String oneUnitItem) {
        this.oneUnitItem = oneUnitItem;
    }

    public String getWhich_unit() {
        return which_unit;
    }

    public void setWhich_unit(String which_unit) {
        this.which_unit = which_unit;
    }

    public String getWhich_unit_str() {
        return which_unit_str;
    }

    public void setWhich_unit_str(String which_unit_str) {
        this.which_unit_str = which_unit_str;
    }

    public String getWhichu_qty() {
        return whichu_qty;
    }

    public void setWhichu_qty(String whichu_qty) {
        this.whichu_qty = whichu_qty;
    }

    public String getEnter_qty() {
        return enter_qty;
    }

    public void setEnter_qty(String enter_qty) {
        this.enter_qty = enter_qty;
    }

    public String getEnter_price() {
        return enter_price;
    }

    public void setEnter_price(String enter_price) {
        this.enter_price = enter_price;
    }

    public String getUnit_barcode() {
        return unit_barcode;
    }

    public void setUnit_barcode(String unit_barcode) {
        this.unit_barcode = unit_barcode;
    }

    public double getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(double currentQty) {
        this.currentQty = currentQty;
    }

    public double getDiscountCustomer() {
        return discountCustomer;
    }

    public void setDiscountCustomer(double discountCustomer) {
        this.discountCustomer = discountCustomer;
    }

    public String getItemPhoto() {
        return itemPhoto;
    }

    public void setItemPhoto(String itemPhoto) {
        this.itemPhoto = itemPhoto;
    }

//    private Bitmap itemPhoto;
    private String itemPhoto;
//    public String getItemPhoto() {
//        return itemPhoto;
//    }
//
//    public void setItemPhoto(String itemPhoto) {
//        this.itemPhoto = itemPhoto;
//    }

    public String getItemHasSerial() {
        return itemHasSerial;
    }

    public void setItemHasSerial(String itemHasSerial) {
        this.itemHasSerial = itemHasSerial;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public String getVouchDate() {
        return vouchDate;
    }

    public void setVouchDate(String vouchDate) {
        this.vouchDate = vouchDate;
    }

    public String getCust() {
        return cust;
    }

    public void setCust(String cust) {
        this.cust = cust;
    }

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
                float taxPercent,int isPosted,String description, String serial_code,String which_unit,String which_unit_str
    ,String whichu_qty,String enter_qty,String enter_price,String unit_barcode,int originVo) {
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
        this.serialCode=serial_code;
        this.which_unit=which_unit;
        this.which_unit_str=which_unit_str;
        this.whichu_qty=whichu_qty;
        this.enter_qty=enter_qty;
        this.enter_price=enter_price;
        this.unit_barcode=unit_barcode;
        this.ORIGINALvoucherNo=originVo;
    }

    //constructor for stock request
    public Item( int companyNumber,int voucherNumber , String itemNo, String itemName, float qty , String date,double cureQty) {
        this.companyNumber = companyNumber;
        this.voucherNumber = voucherNumber;
        this.itemNo = itemNo;
        this.itemName = itemName;
        this.qty = qty;
        this.date = date;
        this.currentQty=cureQty;
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
            obj.put("SERIAL_CODE", serialCode);
            obj.put("VoucherDate",date);
            obj.put("currentQty",currentQty);
//            obj.put("ITEMHASSERIAL", itemHasSerial);



        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
    public JSONObject getJSONObjectDelphi() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("VOUCHERNO", voucherNumber+"");
            obj.put("VOUCHERTYPE", voucherType+"");
            obj.put("ITEMNO", itemNo);
            try {
                if(unit!=null)
                obj.put("UNIT", unit);
                else     obj.put("UNIT", "1");
            }catch ( Exception e){
                obj.put("UNIT", "1");
            }

            obj.put("QTY", qty);
            obj.put("UNITPRICE", price);
            obj.put("BONUS", bonus);
            obj.put("ITEMDISCOUNTVALUE", disc);
            try {
                if(discPerc!=null)
                obj.put("ITEMDISCOUNTPRC", discPerc);
                else   obj.put("ITEMDISCOUNTPRC", "0");
            }
            catch (Exception e){
                obj.put("ITEMDISCOUNTPRC", "0");
            }
            obj.put("VOUCHERDISCOUNT", voucherDiscount);
            obj.put("TAXVALUE", taxValue);
            obj.put("TAXPERCENT", taxPercent);
            obj.put("COMAPNYNO", CONO+"");
            obj.put("ISPOSTED", "0");
            obj.put("VOUCHERYEAR", year);
            obj.put("ITEM_DESCRITION", description);
            obj.put("SERIAL_CODE", serialCode);
            obj.put("ITEM_SERIAL_CODE", "");

            try {
                if(which_unit!=null)
                obj.put("WHICHUNIT", which_unit);
                else   obj.put("WHICHUNIT", "0");
            }catch (Exception e){
                obj.put("WHICHUNIT", "0");
            }
            try {
                if(which_unit_str!=null)
                obj.put("WHICHUNITSTR", which_unit_str);
                else   obj.put("WHICHUNITSTR", "0");
            }catch (Exception e){
                obj.put("WHICHUNITSTR", "0");
            }


            try {
                if(!whichu_qty.equals(""))
                    obj.put("WHICHUQTY", whichu_qty);
                else {
                    obj.put("WHICHUQTY", "0");
                }
            }catch (Exception e){
                obj.put("WHICHUQTY", "0");
            }


            try {
                if(enter_qty!=null)
                obj.put("ENTERQTY", enter_qty);
                else  obj.put("ENTERQTY", "0");
            }catch (Exception e){
                obj.put("ENTERQTY", "0");
            }


            try {
                if(enter_price!=null)
                obj.put("ENTERPRICE", enter_price);
                else  obj.put("ENTERPRICE", "0");
            }catch (Exception e){
                obj.put("ENTERPRICE", "0");
            }



            try {
                if(unit_barcode!=null)
                obj.put("UNITBARCODE", unit_barcode);
                else obj.put("UNITBARCODE", "");
            }catch (Exception e){
                obj.put("UNITBARCODE", "");
            }


            try {
                if(enter_qty!=null)
                obj.put("CALCQTY", enter_qty);
                else obj.put("CALCQTY", "0");
            }catch (Exception e){
                obj.put("CALCQTY", "0");
            }







            obj.put("ORGVHFNO", ORIGINALvoucherNo);






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
    public JSONObject getJSONObject3() {
        JSONObject obj = new JSONObject();
        try {

            obj.put("VHFNO", ORIGINALvoucherNo);
            obj.put("IS_RETURNED", 1);
            obj.put("AVLQTY", qty);
            obj.put("ITEMCODE", itemNo);


        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
    public JSONObject getJSONObject_VanLoad() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("LOADTYPE", "2");
            obj.put("VANCODE", salesmanNo);
            obj.put("LOADQTY", qty);
            obj.put("ITEMCODE", itemNo);
            obj.put("NETQTY", qty);
            obj.put("EXPORTED", "0");
            obj.put("LOADDATE", date);
            obj.put("ADD_SALESMEN", "0");


        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
}
