package com.dr7.salesmanmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.SalesManPlan;
import com.dr7.salesmanmanager.Modles.SalesManPlanAdapter;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.ImportJason.listCustomerInfo;
import static com.dr7.salesmanmanager.Login.Plan_Kind;
import static com.dr7.salesmanmanager.Login.languagelocalApp;

public class ShowPlan extends AppCompatActivity {
public static RecyclerView planRec;
LinearLayout linearPlan, showlocation_lin;
TextView salManName,date,plantype,showlocation;
RadioButton TYPEPOFPLAN1,TYPEPOFPLAN2;
Spinner status,noteS;
TextView share,pdf;
public static String state="",noteType="";
    public static   PolylineOptions rectLine;
    public static SalesManPlanAdapter planAdapter;
    public static  ArrayList<LatLng>  directionPoint =new ArrayList<>();;
    Spinner mtrl_calendar_days_of_week;
    int NumOfDayWeek;
    ArrayAdapter<String> statusAdapter,noteAdapter;
    List<String> statusList,noteList;
    ArrayList <SalesManPlan> salesTemp=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_plan);

        init();
        mtrl_calendar_days_of_week.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                NumOfDayWeek=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mtrl_calendar_days_of_week.setVisibility(View.GONE);
        date .setVisibility(View.GONE);
        if(Login.Plan_Kind ==1) {
            mtrl_calendar_days_of_week.setVisibility(View.VISIBLE);
            date .setVisibility(View.GONE);
        } else {
            date .setVisibility(View.VISIBLE);
            mtrl_calendar_days_of_week.setVisibility(View.GONE);
        }
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
        fillAdapterData( ShowPlan.this   ,MainActivity.DB_salesManPlanList);

        showlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionPoint.clear();
//          for(int i=0; i<MainActivity.DB_salesManPlanList.size();i++)
//                directionPoint.add(new LatLng( MainActivity.DB_salesManPlanList.get(i).getLatitud(),MainActivity.DB_salesManPlanList.get(i).getLongtude()));

//          directionPoint.add(new LatLng(31.974571023429526, 35.913833717317296));
//                directionPoint.add(new LatLng(31.98185160760809, 35.89726839457524));
//                directionPoint.add(new LatLng(31.986146881335287, 35.87924395014088 ));
//
//                directionPoint.add(new LatLng(31.98782125560162, 35.865768532159 ));
//
//                directionPoint.add(new LatLng(31.955565031943042, 35.866183786712476 ));
//                directionPoint.add(new LatLng(31.94137136465222, 35.88798768856344 ));
//
//                directionPoint.add(new LatLng(31.955565031943042, 35.866183786712476 ));
//                directionPoint.add(new LatLng(31.98185160760809, 35.89726839457524));

              rectLine = new PolylineOptions().width(8).color(
                        Color.BLUE);

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
              /*  for (int i = 0; i < MainActivity.DB_salesManPlanList.size(); i++) {
                    rectLine.add(new LatLng(MainActivity.DB_salesManPlanList.get(i).getLatitud(),
                            MainActivity.DB_salesManPlanList.get(i).getLongtude()));
                }*/
                Intent intent = new Intent(ShowPlan.this, SalesManPlanLocations.class);
                startActivity(intent);
            }
        });


    }
    public void  init() {
        share=findViewById(R.id.share);
        pdf=findViewById(R.id.pdf);

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

        mtrl_calendar_days_of_week=findViewById(R.id.mtrl_calendar_days_of_week);
        showlocation_lin = findViewById(R.id.showlocation_lin );
                showlocation = findViewById(R.id.showlocation );
        status=findViewById(R.id.status);
        noteS=findViewById(R.id.noteS);
        TYPEPOFPLAN1 = findViewById(R.id.TYPEPOFPLAN1 );
                TYPEPOFPLAN2 = findViewById(R.id.TYPEPOFPLAN2 );
        planRec = findViewById(R.id.planRec);
        linearPlan=findViewById(R.id.linearPlan);
        salManName = findViewById(R.id.sales_man_name1);
                date = findViewById(R.id.date);
        plantype = findViewById(R.id.plantype);

        statusList=new ArrayList<>();
        statusList.add("All");
        statusList.add("Visit");
        statusList.add("Not Visit");

        noteList=new ArrayList<>();
        noteList.add("Start Note");
        noteList.add("End Note");


        statusAdapter=new ArrayAdapter<String>(ShowPlan.this, R.layout.spinner_style,statusList);
        status.setAdapter(statusAdapter);
        salesTemp=new ArrayList<>();

        noteAdapter=new ArrayAdapter<String>(ShowPlan.this, R.layout.spinner_style,noteList);
        noteS.setAdapter(noteAdapter);

        noteS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                noteType=noteList.get(i);
                Log.e("list132",""+noteType);
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
                  //  salesTemp=MainActivity.DB_salesManPlanList;
                    for(int r=0;r<MainActivity.DB_salesManPlanList.size();r++){
                        SalesManPlan s=MainActivity.DB_salesManPlanList.get(r);
                            salesTemp.add(s);
                    }
                    fillAdapterData( ShowPlan.this   ,salesTemp);

                    state="All";
                }else {
//                    salesTemp.clear();
                    int a=0;
                   if(i==1){
                       a=1;
                       state="Visit";

                   }else {
//                       salesTemp.clear();
                       a=0;
                       state="not Visit";

                   }

                    for(int r=0;r<MainActivity.DB_salesManPlanList.size();r++){
                        if(MainActivity.DB_salesManPlanList.get(r).getLogoutStatus()==a){
                            SalesManPlan s=MainActivity.DB_salesManPlanList.get(r);

                            salesTemp.add(s);                        }
                    }

                    fillAdapterData(ShowPlan.this,salesTemp);

                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(MainActivity.DB_salesManPlanList.size()!=0) {
            salManName.setText(MainActivity.DB_salesManPlanList.get(0).getSaleManNumber()+"");
            date.setText(MainActivity.DB_salesManPlanList.get(0).getDate());
            if(MainActivity.DB_salesManPlanList.get(0).getTypeOrder()==0)
            {
                plantype.setText(getResources().getString(R.string.TYPEPOFPLAN1));
                TYPEPOFPLAN1.setChecked(true);
                TYPEPOFPLAN2.setChecked(false);
                showlocation_lin.setVisibility(View.GONE);
            }
            else {
                plantype.setText(getResources().getString(R.string.TYPEPOFPLAN2));
                TYPEPOFPLAN1.setChecked(false);
                TYPEPOFPLAN2.setChecked(true);
                showlocation_lin.setVisibility(View.VISIBLE);
            }
        }else
        {
            new SweetAlertDialog(ShowPlan.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getResources().getString(R.string.importplan))
                    .setContentText("")
                    .show();
            //Toast.makeText(ReturnByVoucherNo.this, getResources().getString(R.string.importplan), Toast.LENGTH_LONG).show();

        }

    }

    public static void fillAdapterData(Context context, ArrayList<SalesManPlan>salesManPlans) {
        Log.e("SerialReport2","SerialReport2");
        planRec.setLayoutManager(new LinearLayoutManager(context));
        planAdapter = new SalesManPlanAdapter(salesManPlans,context);
        planRec.setAdapter(planAdapter);


    }


    public void sharwhats() {

        try {
            PdfConverter pdf = new PdfConverter(ShowPlan.this);
            pdf.exportListToPdf(salesTemp, "Vocher", "", 19);
        } catch (Exception e) {
            Log.e("Exception22==", "" + e.getMessage());
        }
    }

    public  void exportToPdf(){

        PdfConverter pdf =new PdfConverter(ShowPlan.this);
        pdf.exportListToPdf(salesTemp,getResources().getString(R.string.AccountStatment),"",20);
    }
}