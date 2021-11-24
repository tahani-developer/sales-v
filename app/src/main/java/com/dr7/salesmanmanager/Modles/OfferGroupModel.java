package com.dr7.salesmanmanager.Modles;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OfferGroupModel {

    @SerializedName("ALL_ITEMS")
    private List<OfferGroupModel> ALL_ITEMS;

    @SerializedName("ItemNo")
    public String ItemNo;

    @SerializedName("Name")
    public String Name;

    public String fromDate;

    public String toDate;

    @SerializedName("F_D")
    public String price;
    public String discount;
    public int discountType;
    public String qtyItem="0";
    public String id_serial;
    public  int groupIdOffer;
    public  int matchOffer=0;

    public List<OfferGroupModel> getALL_ITEMS() {
        return ALL_ITEMS;
    }


    public JSONObject getJsonObject() {

        JSONObject obj = new JSONObject();
        try {

            obj.put("ItemNo", ItemNo);
            obj.put("Name", Name);
            obj.put("fromDate", fromDate);// store
            obj.put("toDate", toDate);
            obj.put("discount", discount);
            obj.put("discountType", discountType);
            obj.put("qtyItem", qtyItem);



        } catch (JSONException e) {
            Log.e("TagserialModel" , "JSONException"+e.getMessage());
        }
        return obj;
    }
}
