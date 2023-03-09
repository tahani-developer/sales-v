package com.dr7.salesmanmanager.Reports;

import androidx.appcompat.app.AppCompatActivity;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.GeneralMethod;
import com.dr7.salesmanmanager.ImportJason;
import com.dr7.salesmanmanager.MainActivity;
import com.dr7.salesmanmanager.R;
import com.google.android.material.textfield.TextInputLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import static com.dr7.salesmanmanager.Login.languagelocalApp;

public class CustomersPerformanceReport extends AppCompatActivity {
  Spinner MonthSpinner;
    private TextInputLayout customer_textInput;
    private AutoCompleteTextView customerTv;
    DatabaseHandler obj;
    String Cus_selection,cusno ;
    int Cus_pos;
    Button show;
    ImportJason importJason;
 public static TextView differencevalue,Rangevalue,detectivevalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_performance_report);

        init();
        Calendar rightNow = Calendar.getInstance();
        String ym = String.valueOf(rightNow.getTime().getMonth());
        Log.e("ym ==","  "+ym );
        try {
            MonthSpinner.setSelection(Integer.parseInt(ym));
        }
      catch (Exception e){
          MonthSpinner.setSelection(0);
      }
        ArrayAdapter<String> customerSpinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, MainActivity.customersSpinnerArray);

        customerTv.setAdapter(customerSpinnerAdapter);


    }
    void init(){
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


         importJason=new ImportJason(CustomersPerformanceReport.this);
        differencevalue=findViewById(R.id.differencevalue);
        Rangevalue=findViewById(R.id.Rangevalue);
        detectivevalue=findViewById(R.id.detectivevalue);

        show=findViewById(R.id.preview);
        MonthSpinner=findViewById(R.id.MonthSpinner);
        customer_textInput = findViewById(R.id.customer_textInput);
        customerTv = findViewById(R.id.customerTv);
        obj = new DatabaseHandler(CustomersPerformanceReport.this);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Log.e("cusno==",cusno+"  "+MonthSpinner.getSelectedItemId());



                if (Cus_selection != null && !Cus_selection.equals(""))
                importJason.GetPerformanceInfo(cusno,MonthSpinner.getSelectedItemId()+"");
else
                GeneralMethod.showSweetDialog(CustomersPerformanceReport.this,3,""+getResources().getString(R.string.app_no_cust),"");


            }
        });
        customerTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                Cus_selection = (String) parent.getItemAtPosition(position);
                Cus_pos = position;
                Log.e("Cus_pos==", Cus_pos+"");
                Log.e("Cus_selection==", Cus_selection);

                cusno=   obj. getcustomerbyname(Cus_selection);

            }
        });

    }
}