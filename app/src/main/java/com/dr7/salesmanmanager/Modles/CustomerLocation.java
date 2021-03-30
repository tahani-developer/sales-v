package com.dr7.salesmanmanager.Modles;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomerLocation {
    private  String   CUS_NO;
    private  String   LONG ;
    private  String  LATIT ;
    private  int  isPost ;

    public int getIsPost() {
        return isPost;
    }

    public void setIsPost(int isPost) {
        this.isPost = isPost;
    }

    public CustomerLocation() {
    }

    public String getCUS_NO() {
        return CUS_NO;
    }

    public void setCUS_NO(String CUS_NO) {
        this.CUS_NO = CUS_NO;
    }

    public String getLONG() {
        return LONG;
    }

    public void setLONG(String LONG) {
        this.LONG = LONG;
    }

    public String getLATIT() {
        return LATIT;
    }

    public void setLATIT(String LATIT) {
        this.LATIT = LATIT;
    }
    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("CUS_NO", CUS_NO);
            obj.put("LATIT",LATIT );
            obj.put("LONG",  LONG);

        } catch (JSONException e) {
            Log.e("TaggetJSONObject" , "JSONException");
        }
        return obj;
    }
}
