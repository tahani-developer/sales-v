package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.dr7.salesmanmanager.Modles.Account__Statment_Model;
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;
import static com.dr7.salesmanmanager.CustomerListShow.Customer_Name;
import static com.dr7.salesmanmanager.ImportJason.listCustomerInfo;


public class AccountStatment extends AppCompatActivity {
//master
    List<Customer> customerInfoList=new ArrayList<>();
    ArrayList<Account__Statment_Model> listAccountBalance;
    RecyclerView recyclerView_report;
    LinearLayoutManager layoutManager;
    public  static TextView getAccountList_text,total_qty_text;
    Button preview_button_account;
     Spinner customerSpinner;
     String customerId="";
     TextView name,lastVisitDateTime;
     DatabaseHandler databaseHandler;
    int[] listImageIcone=new int[]{R.drawable.ic_playlist_add_black_24dp};
    Transaction transaction;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_statment);

        ImportJason importJason =new ImportJason(AccountStatment.this);
        importJason.getCustomerInfo(0);

        initialView();
        inflateBoomMenu();
//        Log.e("customername",""+customername.size());

//        preview_button_account.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!customerId.equals(""))
//                {
//                    ImportData importData= new ImportData(AccountStatment.this);
//                    importData.getCustomerAccountStatment(customerId);
//                }
////
//            }
//        });
//        if(customername.size()!=0)
//        {
//            Log.e("customername",""+customername.size());
//            fillCustomerSpenner();
//        }


        getAccountList_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                Log.e("customername",""+s.toString()+"\t"+Customer.size());
//                if(s.toString().equals("1"))
//                {
//                    if(customername.size()!=0)
//                    {
//                        Log.e("customername",""+customername.size());
//                        fillCustomerSpenner();
//                    }
//                }
                Log.e("customername",""+s.toString());
                if(s.toString().equals("2"))
                {
                    if(listCustomerInfo.size()!=0)
                {
                    fillAdapter();
                }
                }
            }
        });

//        customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//
//                customerId= listCustomer.get(position).getCustomerNumber();
//                Log.e("onItemSelected",""+customerId);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }
    private void inflateBoomMenu() {
        BoomMenuButton bmb = (BoomMenuButton)findViewById(R.id.bmb);

        bmb.setButtonEnum(ButtonEnum.SimpleCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_1);
//        SimpleCircleButton.Builder b1 = new SimpleCircleButton.Builder();


        for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {
            bmb.addBuilder(new SimpleCircleButton.Builder()
                    .normalImageRes(listImageIcone[i])

                    .listener(new OnBMClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            switch (index)
                            {
                                case 0:
                                    finish();
                                   Intent i=new Intent(AccountStatment.this,Activities.class);
                                   startActivity(i);

                                    break;
//                                case 1:
//                                    exportToEx();
//                                    break;


                            }
                        }
                    }));
//            bmb.addBuilder(builder);


        }
    }
    @SuppressLint("WrongConstant")
    private void initialView() {
        recyclerView_report=findViewById(R.id.recyclerView_report);
        getAccountList_text=findViewById(R.id.getAccountList_text);
        preview_button_account=findViewById(R.id.preview_button_account);
        name=findViewById(R.id.customerName);
        name.setText(Customer_Name);
        customerSpinner = (Spinner) findViewById(R.id.cat);
        listAccountBalance=new ArrayList<>();
        layoutManager = new LinearLayoutManager(AccountStatment.this);
        layoutManager.setOrientation(VERTICAL);
        recyclerView_report.setLayoutManager(layoutManager);
        lastVisitDateTime=findViewById(R.id.last_visit_text);
        databaseHandler=new DatabaseHandler(AccountStatment.this);
        total_qty_text=findViewById(R.id.total_qty_text);
        getLastVaisit();
    }

    private void getLastVaisit() {
        transaction=new Transaction();
        if(!CustomerListShow.Customer_Account.equals(""))
        {
            transaction=databaseHandler.getLastVisitInfo(CustomerListShow.Customer_Account,Login.salesMan);
            if(transaction.getCheckInDate()!=null)
            {
                lastVisitDateTime.setText(transaction.getCheckInDate()+"\t\t"+transaction.getCheckInTime());
                Log.e("getLastVaisit",""+CustomerListShow.Customer_Account+"\t"+Login.salesMan+"\t"+transaction.getCheckInDate());
            }
            else {

                lastVisitDateTime.setText("");
            }

        }
        else {lastVisitDateTime.setText("No Customer Selected");}

    }

//    private void fillCustomerSpenner() {
//        final ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, customername);
//        customerSpinner.setAdapter(ad);
//    }

    private void fillAdapter() {

        AccountStatmentAdapter adapter = new AccountStatmentAdapter(listCustomerInfo, AccountStatment.this);
        recyclerView_report.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent i=new Intent(AccountStatment.this,Activities.class);
        startActivity(i);
    }
}
