package com.dr7.salesmanmanager.Modles;

import org.json.JSONException;
import org.json.JSONObject;

public class TargetDetalis {
   String SalManNo;
   String SalManName;
   String Date;
   int TargetType;
   String TargetNetSale;
   String OrignalNetSale;
   String ItemNo;
   String ItemName;
   String  PERC;
   String ItemTarget;

   public String getPERC() {
      return PERC;
   }

   public void setPERC(String PERC) {
      this.PERC = PERC;
   }

   public String getTargetNetSale() {
      return TargetNetSale;
   }

   public void setTargetNetSale(String targetNetSale) {
      TargetNetSale = targetNetSale;
   }

   public String getOrignalNetSale() {
      return OrignalNetSale;
   }

   public void setOrignalNetSale(String orignalNetSale) {
      OrignalNetSale = orignalNetSale;
   }

   public String getItemTarget() {
      return ItemTarget;
   }

   public void setItemTarget(String itemTarget) {
      ItemTarget = itemTarget;
   }

   public String getSalManNo() {
      return SalManNo;
   }

   public void setSalManNo(String salManNo) {
      SalManNo = salManNo;
   }

   public String getSalManName() {
      return SalManName;
   }

   public void setSalManName(String salManName) {
      SalManName = salManName;
   }

   public String getDate() {
      return Date;
   }

   public void setDate(String date) {
      Date = date;
   }

   public int getTargetType() {
      return TargetType;
   }

   public void setTargetType(int targetType) {
      TargetType = targetType;
   }



   public String getItemNo() {
      return ItemNo;
   }

   public void setItemNo(String itemNo) {
      ItemNo = itemNo;
   }

   public String getItemName() {
      return ItemName;
   }

   public void setItemName(String itemName) {
      ItemName = itemName;
   }




   public JSONObject getJsonObject(){

      JSONObject jsonObject=new JSONObject();

      try {

         jsonObject.put("SALESMANNO", SalManNo);
         jsonObject.put("ITEMOCODE", ItemNo);
         jsonObject.put("Month", Date);
         jsonObject.put("Targettype", TargetType);
         jsonObject.put("Targetnetsale", TargetNetSale);

         jsonObject.put("Targetnetsale", TargetNetSale);
         jsonObject.put("ItemTarget", ItemTarget);

      } catch (JSONException e) {
         e.printStackTrace();
      }

      return jsonObject;
   }

}
