package com.dr7.salesmanmanager.Reports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CursorTreeAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dr7.salesmanmanager.Adapters.CustomertransAdapter;
import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Modles.TransactionsInfo;
import com.dr7.salesmanmanager.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.dr7.salesmanmanager.Login.languagelocalApp;

public class CustomerWithoutTrasn_Report extends AppCompatActivity {
TextView fromdate,todate;
Button preview;
    Date currentTimeAndDate;
    SimpleDateFormat df;
    DatabaseHandler databaseHandler;
ArrayList<TransactionsInfo> transactionsInfos=new ArrayList<>();
    ArrayList<TransactionsInfo> SearchtransactionsInfos=new ArrayList<>();
RecyclerView recyclerView_report;
    Calendar myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_without_trasn_report);
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
        init();
    }
    void init(){
        transactionsInfos.clear();
        SearchtransactionsInfos.clear();
        preview=findViewById(R.id.preview);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SearchtransactionsInfos.clear();
                filters();
            }
        });
        recyclerView_report=findViewById(R.id.recyclerView_report);
        recyclerView_report.setLayoutManager(new LinearLayoutManager(CustomerWithoutTrasn_Report.this));
        fromdate=findViewById(R.id.from_date);
        databaseHandler= new DatabaseHandler(CustomerWithoutTrasn_Report.this);
        todate=findViewById(R.id.to_date);
        currentTimeAndDate = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd/MM/yyyy");
        String today = df.format(currentTimeAndDate);
        myCalendar = Calendar.getInstance();
        fromdate.setText(convertToEnglish(today));
        todate.setText(convertToEnglish(today));
        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CustomerWithoutTrasn_Report.this, openDatePickerDialog(0), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CustomerWithoutTrasn_Report.this, openDatePickerDialog(1), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        transactionsInfos=databaseHandler.getTrans_info();
        fillAdapter(transactionsInfos);

    }
void     fillAdapter(ArrayList<TransactionsInfo> transactionsInfos){
    Log.e("transactionsInfos==",transactionsInfos.size()+"");
        CustomertransAdapter customertransAdapter=new CustomertransAdapter(transactionsInfos,CustomerWithoutTrasn_Report.this);
        recyclerView_report.setAdapter(customertransAdapter);
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
    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

    private void updateLabel(int flag) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (flag == 0)
            fromdate.setText(sdf.format(myCalendar.getTime()));
  else
            todate.setText(sdf.format(myCalendar.getTime()));
    }


public Date formatDate(String date) throws ParseException {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date d = sdf.parse(date);
        return d;
        }
    public boolean filters() {

        String fromDate = fromdate.getText().toString().trim();
        String toDate = todate.getText().toString();




        try {

             for(int i=0;i<transactionsInfos.size();i++)
                if (
                        (formatDate(transactionsInfos.get(i).getDate()).after(formatDate(fromDate)) || formatDate(transactionsInfos.get(i).getDate()).equals(formatDate(fromDate))) &&
                        (formatDate(transactionsInfos.get(i).getDate()).before(formatDate(toDate)) || formatDate(transactionsInfos.get(i).getDate()).equals(formatDate(toDate)))
                       )
                    SearchtransactionsInfos.add(transactionsInfos.get(i));
            fillAdapter(SearchtransactionsInfos);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
}