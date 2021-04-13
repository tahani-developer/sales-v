package com.dr7.salesmanmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Methods {
    public  static  String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }
    public String formatNew(String mydate) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = fmt.parse(mydate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        return convertToEnglish(fmtOut.format(date));
    }
}
