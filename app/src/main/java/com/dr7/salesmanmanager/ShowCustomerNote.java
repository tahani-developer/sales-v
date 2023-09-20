package com.dr7.salesmanmanager;

import static com.dr7.salesmanmanager.Login.languagelocalApp;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.SalesManCustomerNoteAdapter;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShowCustomerNote extends AppCompatActivity {
public static RecyclerView planRec;
LinearLayout linearPlan, showlocation_lin;
//TextView salManName,date,plantype,showlocation;
//RadioButton TYPEPOFPLAN1,TYPEPOFPLAN2;
Spinner status,noteS;
TextView share,pdf;
TextView date;
public static String stateN ="", noteTypeN ="",dateAll="";
int satPdf=-1;
    public static   PolylineOptions rectLine;
    public static SalesManCustomerNoteAdapter planAdapter;
//    public static  ArrayList<LatLng>  directionPoint =new ArrayList<>();;
//    Spinner mtrl_calendar_days_of_week;
    int NumOfDayWeek;
    ArrayAdapter<String> statusAdapter,noteAdapter;
    List<String> statusList,noteList;
    ArrayList <Customer> salesTemp=new ArrayList<>();
    DatabaseHandler databaseHandler;
GeneralMethod generalMethod;
EditText custSearch;
   List <Customer> customerNote=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_customer_note);

        init();

        try{
            if(languagelocalApp.equals("ar"))
            {
                linearPlan.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else{
                if(languagelocalApp.equals("en"))
                {
                    linearPlan.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        }
        catch ( Exception e)
        {
            linearPlan.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        fillAdapterData( ShowCustomerNote.this   ,customerNote);



    }
    public void  init() {
        share=findViewById(R.id.share);
        pdf=findViewById(R.id.pdf);
        generalMethod=new GeneralMethod(ShowCustomerNote.this);
        databaseHandler=new DatabaseHandler(ShowCustomerNote.this);
        dateAll=generalMethod.getCurentTimeDate(1);
//        customerNote=databaseHandler.getCustomersBySalesMan(salesMan);
        customerNote=databaseHandler.getAllCustomersNote(generalMethod.getCurentTimeDate(1));
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharwhats();
            }
        });

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportToPdf();
            }
        });

//        mtrl_calendar_days_of_week=findViewById(R.id.mtrl_calendar_days_of_week);
        date=findViewById(R.id.date);
        date.setText(""+generalMethod.getCurentTimeDate(1));
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generalMethod.DateClick(date);
            }
        });

        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals("")){
                    dateAll=date.getText().toString();
                   customerNote= databaseHandler.getAllCustomersNote(date.getText().toString());
                }else {
                    date.setError("Required!");
                }

                fillAdapterData(ShowCustomerNote.this,customerNote);

            }

            @Override
            public void afterTextChanged(Editable editable) {



            }
        });

        custSearch=findViewById(R.id.cust);
        custSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!custSearch.getText().toString().equals("")){
                    salesTemp.clear();
                    for(int t=0;t<customerNote.size();t++){

                        if(customerNote.get(t).getCustName().toUpperCase().contains(custSearch.getText().toString().toUpperCase())){
                            Customer customer=customerNote.get(t);
                            salesTemp.add(customer);
                        }


                    }

                    fillAdapterData(ShowCustomerNote.this,salesTemp);


                }else{
                    //custSearch.setError("Required!");
                    fillAdapterData(ShowCustomerNote.this,customerNote);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        showlocation_lin = findViewById(R.id.showlocation_lin );
//                showlocation = findViewById(R.id.showlocation );
        status=findViewById(R.id.status);
        noteS=findViewById(R.id.noteS);
//        TYPEPOFPLAN1 = findViewById(R.id.TYPEPOFPLAN1 );
//                TYPEPOFPLAN2 = findViewById(R.id.TYPEPOFPLAN2 );
        planRec = findViewById(R.id.planRec);
        linearPlan=findViewById(R.id.linearPlan);
//        salManName = findViewById(R.id.sales_man_name1);
//                date = findViewById(R.id.date);
//        plantype = findViewById(R.id.plantype);

        statusList=new ArrayList<>();
        statusList.add("All");
        statusList.add("Visit");
        statusList.add("Not Visit");

        noteList=new ArrayList<>();
        noteList.add("Start Note");
        noteList.add("End Note");


        statusAdapter=new ArrayAdapter<String>(ShowCustomerNote.this, R.layout.spinner_style,statusList);
        status.setAdapter(statusAdapter);
        salesTemp=new ArrayList<>();

        noteAdapter=new ArrayAdapter<String>(ShowCustomerNote.this, R.layout.spinner_style,noteList);
        noteS.setAdapter(noteAdapter);

        noteS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                noteTypeN =noteList.get(i);
                Log.e("list132",""+ noteTypeN);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                salesTemp.clear();

                if(i==0){
                  //  salesTemp=customerNote;
                    for(int r=0;r<customerNote.size();r++){
                        Customer s=customerNote.get(r);
                            salesTemp.add(s);
                    }
                    fillAdapterData( ShowCustomerNote.this   ,salesTemp);

                    stateN ="All";
                    satPdf=-1;
                }else {
//                    salesTemp.clear();
                    int a=0;
                   if(i==1){
                       a=1;
                       stateN ="Visit";
                       satPdf=1;

                   }else {
//                       salesTemp.clear();
                       a=0;
                       satPdf=0;
                       stateN ="not Visit";

                   }

                    for(int r=0;r<customerNote.size();r++){
                        Log.e("Qa",""+customerNote.get(r).getStatus()+"   a="+a);
                        if(customerNote.get(r).getStatus().equals(""+a)){
                            Log.e("Qa","yes");
                            Customer s=customerNote.get(r);
                            salesTemp.add(s);
                        }
                    }

                    fillAdapterData(ShowCustomerNote.this,salesTemp);

                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        if(customerNote.size()!=0) {
////            salManName.setText(customerNote.get(0).getSaleManNumber()+"");
////            date.setText(customerNote.get(0).getDate());
//            if(customerNote.get(0).getTypeOrder()==0)
//            {
//                plantype.setText(getResources().getString(R.string.TYPEPOFPLAN1));
//                TYPEPOFPLAN1.setChecked(true);
//                TYPEPOFPLAN2.setChecked(false);
//                showlocation_lin.setVisibility(View.GONE);
//            }
//            else {
//                plantype.setText(getResources().getString(R.string.TYPEPOFPLAN2));
//                TYPEPOFPLAN1.setChecked(false);
//                TYPEPOFPLAN2.setChecked(true);
//                showlocation_lin.setVisibility(View.VISIBLE);
//            }
//        }else
//        {
//            new SweetAlertDialog(ShowCustomerNote.this, SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText(getResources().getString(R.string.importplan))
//                    .setContentText("")
//                    .show();
//            //Toast.makeText(ReturnByVoucherNo.this, getResources().getString(R.string.importplan), Toast.LENGTH_LONG).show();
//
//        }

    }

    public static void fillAdapterData(Context context, List<Customer>salesManPlans) {
        Log.e("SerialReport2","SerialReport2");
        planRec.setLayoutManager(new LinearLayoutManager(context));
        planAdapter = new SalesManCustomerNoteAdapter(salesManPlans,context);
        planRec.setAdapter(planAdapter);


    }


    public void sharwhats() {

        try {
            List<Customer> CustNot=databaseHandler.getAllCustomersNoteWithNote(dateAll,satPdf);

            PdfConverter pdf = new PdfConverter(ShowCustomerNote.this);
            pdf.exportListToPdf(CustNot, "Vocher", "", 22);
        } catch (Exception e) {
            Log.e("Exception22==", "" + e.getMessage());
        }
    }

    public  void exportToPdf(){

        List<Customer> CustNot=databaseHandler.getAllCustomersNoteWithNote(dateAll,satPdf);
        PdfConverter pdf =new PdfConverter(ShowCustomerNote.this);
        pdf.exportListToPdf(CustNot,getResources().getString(R.string.AccountStatment),"",23);
    }
}