package com.dr7.salesmanmanager.Reports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.dr7.salesmanmanager.Adapters.ItemsTargetAdapter;
import com.dr7.salesmanmanager.Adapters.NetsaleTargetAdapter;
import com.dr7.salesmanmanager.ImportJason;
import com.dr7.salesmanmanager.Login;
import com.dr7.salesmanmanager.R;
import com.google.android.material.textfield.TextInputLayout;

public class TargetReport extends AppCompatActivity {

    RadioGroup TargettypeRG;
    Spinner dateEdt;
AppCompatButton previewButton;
ImportJason importJason;
public  static TextView NetsalTargetRespon,itemTargetRespon,total_value;
String Month;
RecyclerView NteSal_targetrec,itemtargetrec;
    int  TargetType=1;double sum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_report);
        init();
        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Month=dateEdt.getSelectedItem().toString();
                if(TargetType==0)
                {  importJason. salesGoalsList.clear();
                    fillAdapterNetsalTarget();
                    importJason.getSalesmanGoal(Login.salesMan,Month.substring(0, Month.indexOf(" ")));

                }
else {
                    importJason. ItemsGoalsList.clear();
                    fillAdapterItemsTarget();
                    importJason.getSaleGoalItems(Login.salesMan, Month.substring(0, Month.indexOf(" ")));
                }
            }
        });


    }
    void init(){
        importJason=new ImportJason(TargetReport.this);
        previewButton= findViewById(R.id.previewButton);
        itemtargetrec= findViewById(R.id.itemtargetrec);
        NteSal_targetrec= findViewById(R.id.NteSal_targetrec);
        dateEdt=findViewById(R.id.dateEdt);
        itemTargetRespon=findViewById(R.id.itemTargetRespon);
        NetsalTargetRespon=findViewById(R.id.NetsalTargetRespon);
        total_value=findViewById(R.id.total_value);
        itemTargetRespon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().trim().equals(""))
                {
                    fillAdapterItemsTarget();
                }
            }
        });
        NetsalTargetRespon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
if(!editable.toString().trim().equals(""))
{
    fillAdapterNetsalTarget();
}
            }
        });
        TargettypeRG=findViewById(R.id.TargettypeRG);
        NteSal_targetrec.setLayoutManager(new LinearLayoutManager(TargetReport.this));
        itemtargetrec.setLayoutManager(new LinearLayoutManager(TargetReport.this));
        TargettypeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = TargettypeRG.findViewById(checkedId);
                int index = TargettypeRG.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0: //// secondbutton items

                        TargetType =1;
                        Log.e("case0TargetType","TargetType"+TargetType);



                        //     Toast.makeText(getApplicationContext(), "Selected button number " + index, 500).show();
                        break;
                    case 1: //first button  netsale
                        TargetType = 0;
                        Log.e("case1TargetType","TargetType"+TargetType);


                        //      Toast.makeText(getApplicationContext(), "Selected button number " + index, 500).show();
                        break;
                }
            }
        });
    }
  void  fillAdapterNetsalTarget(){

        Log.e("fillAdapterNetsalTarget","fillAdapterNetsalTarget"+ImportJason.salesGoalsList.size());
        NteSal_targetrec.setAdapter(new NetsaleTargetAdapter(ImportJason.salesGoalsList,TargetReport.this));

    }
    void  fillAdapterItemsTarget(){

        Log.e("fillAdapterItemsTarget","fillAdapterItemsTarget"+ImportJason.ItemsGoalsList.size());
        itemtargetrec.setAdapter(new ItemsTargetAdapter(ImportJason.ItemsGoalsList,TargetReport.this));
        fillTotal();

    }

    private void fillTotal() {
        sum=0;
        if(ImportJason.ItemsGoalsList.size()!=0)
        {
            for(int i=0;i<ImportJason.ItemsGoalsList.size();i++)
            {
                if(!ImportJason.ItemsGoalsList.get(i).getOrignalNetSale().equals(""))
                {
                    try {
                        sum+=Double.parseDouble(ImportJason.ItemsGoalsList.get(i).getPERC());

                    }catch (Exception e){

                    }
                }

            }
        }
        Log.e("fillTotal","ItemsGoalsList="+ImportJason.ItemsGoalsList.size());
        total_value.setText(sum+"");
    }
}