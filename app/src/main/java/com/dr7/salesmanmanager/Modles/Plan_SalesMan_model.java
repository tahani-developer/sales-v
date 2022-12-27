package com.dr7.salesmanmanager.Modles;

import org.json.JSONException;
import org.json.JSONObject;

public class Plan_SalesMan_model {
    private  String customerName;
    private  String customerNumber;
    private  String latit_customer;
    private  String long_customer;
    private  String plan_date;
    private  String salesNo;
    private  int orderd;
    private int type_orderd;
    private  String areaPlan;

    public String getAreaPlan() {
        return areaPlan;
    }

    public void setAreaPlan(String areaPlan) {
        this.areaPlan = areaPlan;
    }

    public Plan_SalesMan_model() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getLatit_customer() {
        return latit_customer;
    }

    public void setLatit_customer(String latit_customer) {
        this.latit_customer = latit_customer;
    }

    public String getLong_customer() {
        return long_customer;
    }

    public void setLong_customer(String long_customer) {
        this.long_customer = long_customer;
    }

    public String getPlan_date() {
        return plan_date;
    }

    public void setPlan_date(String plan_date) {
        this.plan_date = plan_date;
    }

    public String getSalesNo() {
        return salesNo;
    }

    public void setSalesNo(String salesNo) {
        this.salesNo = salesNo;
    }

    public int getOrderd() {
        return orderd;
    }

    public void setOrderd(int orderd) {
        this.orderd = orderd;
    }

    public int getType_orderd() {
        return type_orderd;
    }

    public void setType_orderd(int type_orderd) {
        this.type_orderd = type_orderd;
    }
    public JSONObject getJsonObject2(){
         JSONObject jsonObject=new JSONObject();

        try {
            jsonObject.put("SALESNO", salesNo);
            jsonObject.put("TRDATE", plan_date);
            jsonObject.put("CUSTNO", customerNumber);
            jsonObject.put("CUSNAME", customerName);
//            jsonObject.put("LA", "32.5555");
//            jsonObject.put("LO", "35.2222");
            jsonObject.put("LA", latit_customer);
            jsonObject.put("LO", long_customer);
            jsonObject.put("ORDERD", orderd);
            jsonObject.put("TYPEORDER", type_orderd);
            jsonObject.put("AREAPLAN", areaPlan);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
