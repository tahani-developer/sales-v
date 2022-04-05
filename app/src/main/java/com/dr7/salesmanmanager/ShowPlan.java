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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.dr7.salesmanmanager.Adapters.ReturnItemAdapter;
import com.dr7.salesmanmanager.Modles.SalesManPlan;
import com.dr7.salesmanmanager.Modles.SalesManPlanAdapter;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.Login.languagelocalApp;

public class ShowPlan extends AppCompatActivity {
public static RecyclerView planRec;
LinearLayout linearPlan, showlocation_lin;
TextView salManName,date,plantype,showlocation;
RadioButton TYPEPOFPLAN1,TYPEPOFPLAN2;
    public static   PolylineOptions rectLine;
    public static SalesManPlanAdapter planAdapter;
    public static  ArrayList<LatLng>  directionPoint =new ArrayList<>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_plan);

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
        fillAdapterData( ShowPlan.this   ,MainActivity.DB_salesManPlanList);

        showlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionPoint.clear();
//          for(int i=0; i<MainActivity.DB_salesManPlanList.size();i++)
//                directionPoint.add(new LatLng( MainActivity.DB_salesManPlanList.get(i).getLatitud(),MainActivity.DB_salesManPlanList.get(i).getLongtude()));

          directionPoint.add(new LatLng(31.974571023429526, 35.913833717317296));
                directionPoint.add(new LatLng(31.98185160760809, 35.89726839457524));
                directionPoint.add(new LatLng(31.986146881335287, 35.87924395014088 ));

                directionPoint.add(new LatLng(31.98782125560162, 35.865768532159 ));

                directionPoint.add(new LatLng(31.955565031943042, 35.866183786712476 ));
                directionPoint.add(new LatLng(31.94137136465222, 35.88798768856344 ));

                directionPoint.add(new LatLng(31.955565031943042, 35.866183786712476 ));
                directionPoint.add(new LatLng(31.98185160760809, 35.89726839457524));

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
        showlocation_lin = findViewById(R.id.showlocation_lin );
                showlocation = findViewById(R.id.showlocation );
        TYPEPOFPLAN1 = findViewById(R.id.TYPEPOFPLAN1 );
                TYPEPOFPLAN2 = findViewById(R.id.TYPEPOFPLAN2 );
        planRec = findViewById(R.id.planRec);
        linearPlan=findViewById(R.id.linearPlan);
        salManName = findViewById(R.id.sales_man_name1);
                date = findViewById(R.id.date);
        plantype = findViewById(R.id.plantype);
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
}