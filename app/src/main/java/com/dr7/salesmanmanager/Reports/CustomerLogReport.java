package com.dr7.salesmanmanager.Reports;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.ExportToExcel;
import com.dr7.salesmanmanager.GeneralMethod;
import com.dr7.salesmanmanager.LocaleAppUtils;
import com.dr7.salesmanmanager.MainActivity;
import com.dr7.salesmanmanager.PdfConverter;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.google.android.material.textfield.TextInputEditText;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.dr7.salesmanmanager.Login.SalsManTripFlage;
import static com.dr7.salesmanmanager.Login.languagelocalApp;


public class CustomerLogReport extends AppCompatActivity {
    private static final String TAG = "CustomerLogReport";
    Calendar myCalendar;
    List<Transaction> transactionList ;
    List<Transaction> FilterdtransactionList =new ArrayList<>();
    CircleImageView expotTpExcel,expotTpPdf;
  TextView  from_date,to_date;
  AutoCompleteTextView customer_name;
  TableRow header_row;
  Button preview;
    DatabaseHandler obj;
   TextView Triptime ;
    int[] listImageIcone = new int[]{R.drawable.pdf_icon, R.drawable.excel_small};
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LocaleAppUtils().changeLayot(CustomerLogReport.this);
        setContentView(R.layout.customer_log_report);
//

        LinearLayout linearMain = findViewById(R.id.linearMain);

        try {
            if (languagelocalApp.equals("ar")) {
                linearMain.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else {
                if (languagelocalApp.equals("en")) {
                    linearMain.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        } catch (Exception e) {
            linearMain.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        Triptime=findViewById(R.id. Triptime);
        header_row=findViewById(R.id. header);
        transactionList = new ArrayList<Transaction>();
         obj = new DatabaseHandler(CustomerLogReport.this);
        transactionList = obj.getAlltransactions();
        expotTpExcel=findViewById(R.id.expotTpExcel);
        expotTpPdf=findViewById(R.id.expotTpPdf);
        preview =findViewById(R.id.preview);
        from_date=findViewById(R.id.from_date);
        customer_name=findViewById(R.id.customer_name);
        to_date=findViewById(R.id.to_date);
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String today = df.format(currentTimeAndDate);
        from_date.setText(today);
        to_date.setText(today);
        myCalendar = Calendar.getInstance();
        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CustomerLogReport.this, openDatePickerDialog(0), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CustomerLogReport.this, openDatePickerDialog(1), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        preview.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           filter();
                                       }
                                   }
        );
        expotTpPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        expotTpExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

     //   filltabel( transactionList);
        filter();
       // Toast.makeText(CustomerLogReport.this, transactionList.get(1).cusCode, Toast.LENGTH_LONG).show();

        if(SalsManTripFlage==0)
        { {

            Triptime.setVisibility(View.GONE);
        }
        }

        ArrayAdapter<String> customerSpinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, MainActivity.customersSpinnerArray);

        customer_name.setAdapter(customerSpinnerAdapter);


        inflateBoomMenu();
    }

    private void inflateBoomMenu() {
        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);

        bmb.setButtonEnum(ButtonEnum.SimpleCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_2);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_2_2);
//        SimpleCircleButton.Builder b1 = new SimpleCircleButton.Builder();


        for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {
            bmb.addBuilder(new SimpleCircleButton.Builder()
                    .normalImageRes(listImageIcone[i])

                    .listener(new OnBMClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            switch (index) {
                                case 0:
                                    if(FilterdtransactionList.size()!=0)
                                        exportToPdf(FilterdtransactionList);
                                    else
                                        exportToPdf(transactionList);

                                    break;
                                case 1:
                                    if(FilterdtransactionList.size()!=0)
                                        exportToEx(FilterdtransactionList);
                                    else
                                        exportToEx(transactionList);
                                    break;


                            }
                        }
                    }));
//            bmb.addBuilder(builder);


        }
    }

    private void exportToEx( List<Transaction> transactionList) {
        ExportToExcel exportToExcel=new ExportToExcel();
        exportToExcel.createExcelFile(CustomerLogReport.this,"ReportCustomer.xls",1,transactionList);

    }
    public  void exportToPdf( List<Transaction> transactionList){
        Log.e("exportToPdf",""+transactionList.size());
        PdfConverter pdf =new PdfConverter(CustomerLogReport.this);
        pdf.exportListToPdf(transactionList,"Customer Log Report","21/12/2020",1);
    }
    public Date formatDate(String date) throws ParseException {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date d = sdf.parse(date);
        return d;
    }

    void  filter(){
        try {
            GeneralMethod generalMethod=new GeneralMethod(CustomerLogReport.this);
            FilterdtransactionList.clear();
            String fromDate = generalMethod.convertToEnglish(from_date.getText().toString().trim());
            String toDate = generalMethod.convertToEnglish(to_date.getText().toString());
            for (int i = 0; i < transactionList.size(); i++) {

                String date = transactionList.get(i).getCheckInDate();
                String name = customer_name.getText().toString().trim();

                Log.e("date=",date+"");
                Log.e("fromDate=",fromDate+"");
                Log.e("toDate=",toDate+"");
                Log.e("name=",name+"");
                if (
                        (formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                                (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate)))
//                        formatDate(date).after(formatDate(fromDate))
//                        || formatDate(date).equals(formatDate(fromDate)) &&
//                        formatDate(date).before(formatDate(toDate))
//                        || formatDate(date).equals(formatDate(toDate))

                ) {
                    Log.e("transactionList.get(i)",transactionList.get(i).getCheckInDate()+" ");
                    if (!name.equals("")) {
                        Log.e("name2=",transactionList.get(i).getCusName()+"");
                        if (transactionList.get(i).getCusName().trim().contains(name.trim()))
                            FilterdtransactionList.add(transactionList.get(i));
                    } else
                        FilterdtransactionList.add(transactionList.get(i));

                }

            }
        }catch (Exception exception){

        }
        filltabel( FilterdtransactionList);
    }
void filltabel( List<Transaction> transactionList){
    try {
       // Log.e("getSaleManTrip=",obj.getSaleManTrip(transactionList.get(transactionList.size()-1).getCheckOutDate(),transactionList.get(transactionList.size()-1).getCheckInTime())+"");
        if(SalsManTripFlage==1) {


            String difftime = "";
            Log.e("transactionList=", transactionList.size() + "");
            TableLayout TableCustomerLogReport = (TableLayout) findViewById(R.id.TableCustomerLogReport);
            TableCustomerLogReport.removeAllViews();
            header_row.setPadding(5, 10, 5, 10);
            TableCustomerLogReport.addView(header_row);
            for (int n = 0; n < transactionList.size(); n++) {
                String starttime = obj.getSaleManTrip(transactionList.get(n).getCheckOutDate(), transactionList.get(n).getCheckOutTime());
                Log.e("starttime=", starttime + "");
                TableRow row = new TableRow(this);
                row.setPadding(5, 10, 5, 10);

                if (n % 2 == 0)
                    row.setBackgroundColor(ContextCompat.getColor(this, R.color.layer3));
                else
                    row.setBackgroundColor(ContextCompat.getColor(this, R.color.layer7));

                for (int i = 0; i < 9; i++) {

                    String[] record = {transactionList.get(n).getSalesManId() + "",
                            transactionList.get(n).getCusCode() + "",
                            transactionList.get(n).getCusName(),
                            transactionList.get(n).getCheckInDate(),
                            transactionList.get(n).getCheckInTime(),
                            transactionList.get(n).getCheckOutDate(),
                            transactionList.get(n).getCheckOutTime(),
                            transactionList.get(n).getStatus() + ""
                            , getTimeDiff(starttime, transactionList.get(n).getCheckInTime()) + ""
                    };

                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(this);
                    textView.setText(record[i]);
                    textView.setTextColor(ContextCompat.getColor(CustomerLogReport.this, R.color.colorblue_dark));
                    textView.setGravity(Gravity.CENTER);

                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(130, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                    textView.setLayoutParams(lp2);

                    row.addView(textView);

                }
                TableCustomerLogReport.addView(row);

            }

        }else{

            Log.e("transactionList=", transactionList.size() + "");
            TableLayout TableCustomerLogReport = (TableLayout) findViewById(R.id.TableCustomerLogReport);
            TableCustomerLogReport.removeAllViews();
            header_row.setPadding(5, 10, 5, 10);
            TableCustomerLogReport.addView(header_row);
            for (int n = 0; n < transactionList.size(); n++) {
                TableRow row = new TableRow(this);
                row.setPadding(5, 10, 5, 10);

                if (n % 2 == 0)
                    row.setBackgroundColor(ContextCompat.getColor(this, R.color.layer3));
                else
                    row.setBackgroundColor(ContextCompat.getColor(this, R.color.layer7));

                for (int i = 0; i < 8; i++) {

                    String[] record = {transactionList.get(n).getSalesManId() + "",
                            transactionList.get(n).getCusCode() + "",
                            transactionList.get(n).getCusName(),
                            transactionList.get(n).getCheckInDate(),
                            transactionList.get(n).getCheckInTime(),
                            transactionList.get(n).getCheckOutDate(),
                            transactionList.get(n).getCheckOutTime(),
                            transactionList.get(n).getStatus() + ""

                    };

                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(this);
                    textView.setText(record[i]);
                    textView.setTextColor(ContextCompat.getColor(CustomerLogReport.this, R.color.colorblue_dark));
                    textView.setGravity(Gravity.CENTER);

                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(130, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                    textView.setLayoutParams(lp2);

                    row.addView(textView);

                }
                TableCustomerLogReport.addView(row);

            }
        }


    }catch (Exception e){

    }
}
    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final int flag) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(flag);
            }

        };
        return date;
    }
    private void updateLabel(int flag) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (flag == 0)
            from_date.setText(sdf.format(myCalendar.getTime()));
        else
            to_date.setText(sdf.format(myCalendar.getTime()));
    }
  String getTimeDiff(String startTime,String endTime){
        try {

//            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm", Locale.ENGLISH);
//
////      String startTime = "08:00 AM";
////      String endTime = "04:00 PM";
//
//            LocalTime start = LocalTime.parse(startTime, timeFormatter);
//            LocalTime end = LocalTime.parse(endTime, timeFormatter);
//
//            Duration diff = Duration.between(start, end);
//
//            long hours = diff.toHours();
//            long minutes = diff.minusHours(hours).toMinutes();
//            String totalTimeString = String.format("%02d:%02d", hours, minutes);
//            System.out.println("TotalTime in Hours and Mins Format is " + totalTimeString);
//            return totalTimeString;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date startDate = simpleDateFormat.parse(startTime);
            Date endDate = simpleDateFormat.parse(endTime);

            long difference = endDate.getTime() - startDate.getTime();
            if(difference<0)
            {
                Date dateMax = simpleDateFormat.parse("24:00");
                Date dateMin = simpleDateFormat.parse("00:00");
                difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
            }
            int days = (int) (difference / (1000*60*60*24));
            int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);

            Log.e("log_tag","Hours: "+hours+", Mins: "+min);
           if(hours>0) return hours+" hours "+min+" Min";
           else return min+" Min";

        }
   catch (Exception e){
       Log.e("Exception",e.getMessage()+"");
       return "0 Min";
   }
  }
}
