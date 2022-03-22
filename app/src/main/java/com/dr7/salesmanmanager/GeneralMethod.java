package com.dr7.salesmanmanager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import org.apache.http.impl.cookie.DateUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GeneralMethod {
    Context  context;
    private DecimalFormat decimalFormat;

    DatabaseHandler databaseHandler;
    private Calendar myCalendar;

    public GeneralMethod(Context context) {
        this.context = context;
        databaseHandler=new DatabaseHandler(context);
        decimalFormat = new DecimalFormat("00.000");
        myCalendar = Calendar.getInstance();
    }
    public String getCurentTimeDate(int flag){
        String dateCurent,timeCurrent,dateTime="";
        Date currentTimeAndDate;
        SimpleDateFormat dateFormat, timeformat;
        currentTimeAndDate = Calendar.getInstance().getTime();
        if(flag==1)// return date
        {

            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateCurent = dateFormat.format(currentTimeAndDate);
            dateTime=convertToEnglish(dateCurent);

        }
        else {
            if(flag==2)// return time
            {
                timeformat = new SimpleDateFormat("hh:mm:ss");
                dateCurent = timeformat.format(currentTimeAndDate);
                dateTime=convertToEnglish(dateCurent);
            }
        }
        return dateTime;

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
    public void DateClick(TextView dateText){

        new DatePickerDialog(context, openDatePickerDialog(dateText), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final TextView DateText) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                DateText.setText(sdf.format(myCalendar.getTime()));
            }

        };
        return date;
    }

    /////B
    public boolean checkDeviceDate() throws ParseException {

        Date deviceDateAndTime = Calendar.getInstance().getTime();

//        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
//        String deviceTime = timeFormat.format(deviceDateAndTime);
//        Log.e("time", "" + deviceTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String deviceDate = dateFormat.format(deviceDateAndTime);
        Date deviceDate_d = dateFormat.parse(deviceDate);
        Log.e("Device_Date", deviceDate + "");

        boolean valid = false;
        Date lastV_Date_d = null;
        String lastV_Date = databaseHandler.getLastVoucherDate();
        if(!lastV_Date.equals(""))
         lastV_Date_d = dateFormat.parse(lastV_Date);

//        Log.e("LastV_Date", lastV_Date + "");

        if (lastV_Date.equals(""))
            valid = true;
        else {

            if (deviceDate_d.after(lastV_Date_d) || deviceDate_d.equals(lastV_Date_d))
                valid = true;
//            valid = deviceDate_d.after(lastV_Date_d);

//            int lastV_Day = Integer.parseInt(lastV_Date.substring(0, 2));
//            int lastV_Month = Integer.parseInt(lastV_Date.substring(3, 5));
//            int lastV_year = Integer.parseInt(lastV_Date.substring(6, 10));
//            Log.e("LastV_Date2", lastV_Day + "/" + lastV_Month + "/" + lastV_year);
//
//
//            Log.e("Device_Date", deviceDate + "");
//            int device_day = Integer.parseInt(deviceDate.substring(0, 2));
//            int device_Month = Integer.parseInt(deviceDate.substring(3, 5));
//            int device_year = Integer.parseInt(deviceDate.substring(6, 10));
//            Log.e("Device_Date2", device_day + "/" + device_Month + "/" + device_year);
//
//
//            if (device_year > lastV_year)
//                valid = true;
//            else if (device_year == lastV_year) {
//
//                if (device_Month > lastV_Month)
//                    valid = true;
//                else if (device_Month == lastV_Month) {
//
//                    if (device_day >= lastV_Day)
//                        valid = true;
//
//                }
//
//            }
        }
        return valid;

    }

}
