package com.dr7.salesmanmanager;

import android.content.Context;
import android.util.Log;

import java.text.DecimalFormat;

public class GeneralMethod {
    Context  context;
    private DecimalFormat decimalFormat;

    DatabaseHandler databaseHandler;

    public GeneralMethod(Context context) {
        this.context = context;
        databaseHandler=new DatabaseHandler(context);
        decimalFormat = new DecimalFormat("00.000");
    }

    public  String getSalesManLogin(){
        return  databaseHandler.getAllUserNo();
    }
    public  String getDecimalFormat(double item)
    {
        try {
          return   decimalFormat.format(item);
        }catch (Exception e){
            return "";
        }

    }
    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }
}
